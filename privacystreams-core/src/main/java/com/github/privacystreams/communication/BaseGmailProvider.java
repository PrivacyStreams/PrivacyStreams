package com.github.privacystreams.communication;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.easilydo.sift.api.ApiException;
import com.easilydo.sift.crypto.Signatory;
import com.easilydo.sift.model.Sift;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.AppUtils;
import com.github.privacystreams.utils.ConnectionUtils;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.Logging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;


/**
 * Base class for Gmail-related Providers.
 */

abstract class BaseGmailProvider extends MStreamProvider implements GmailResultListener {
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    private Gmail mService;
    int mMaxResult = Globals.EmailConfig.defaultMaxNumberOfReturnResults;
    long mBegin = 0;
    long mEnd = 0;
    boolean authorized = false;
    long mLastEmailTime = 0;
    public static final String API_ENDPOINT = "https://api.easilydo.com";
    private String apiKey = "9aed6c22c1623a1bfd6473a4328f85b0";
    private String apiSecret = "fa53d475c36bc18ea66196afa23cc2f35ea6012c";
    private Signatory signatory;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    protected JsonNode executeRequest(BaseRequest request) {
        try {
            HttpResponse<String> response = request.asString();
            if(response.getStatus() >= 400) {
                String msg = String.format("Bad HTTP response from sift server. code: %d message: %s", response.getStatus(), response.getStatusText());
                Logging.debug(msg);
                throw new ApiException(msg, response.getStatus());
            }

            JsonNode root = objectMapper.readTree(response.getBody());

            Logging.debug(root.toString());

            int code = root.get("code").asInt();
            if(code >= 400) {
                String id = root.get("id").asText();
                String msg = String.format("Json rpc error response from sift server. requestId: %s code: %d message: %s", id, code, root.get("message").asText());
                Logging.debug(msg);
                throw new ApiException(msg, code, id);
            }

            return root.get("result");
        } catch(IOException ioex) {
            Logging.debug("Failed to parse json response from sift server");
            throw new ApiException("Failed to parse json response from sift server", ioex);
        } catch(UnirestException uniex) {
            Logging.debug("Exception making REST api call");
            throw new ApiException("Exception making REST api call", uniex);
        }
    }

    static JsonNode convertJsonFormat(JSONObject json) {
        ObjectNode ret = JsonNodeFactory.instance.objectNode();

        @SuppressWarnings("unchecked")
        Iterator<String> iterator = json.keys();
        for (; iterator.hasNext();) {
            String key = iterator.next();
            Object value;
            try {
                value = json.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (json.isNull(key))
                ret.putNull(key);
            else if (value instanceof String)
                ret.put(key, (String) value);
            else if (value instanceof Integer)
                ret.put(key, (Integer) value);
            else if (value instanceof Long)
                ret.put(key, (Long) value);
            else if (value instanceof Double)
                ret.put(key, (Double) value);
            else if (value instanceof Boolean)
                ret.put(key, (Boolean) value);
            else if (value instanceof JSONObject)
                ret.put(key, convertJsonFormat((JSONObject) value));
            else if (value instanceof JSONArray)
                ret.put(key, convertJsonFormat((JSONArray) value));
            else
                throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
        }
        return ret;
    }


    static JsonNode convertJsonFormat(JSONArray json) {
        ArrayNode ret = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < json.length(); i++) {
            Object value;
            try {
                value = json.get(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (json.isNull(i))
                ret.addNull();
            else if (value instanceof String)
                ret.add((String) value);
            else if (value instanceof Integer)
                ret.add((Integer) value);
            else if (value instanceof Long)
                ret.add((Long) value);
            else if (value instanceof Double)
                ret.add((Double) value);
            else if (value instanceof Boolean)
                ret.add((Boolean) value);
            else if (value instanceof JSONObject)
                ret.add(convertJsonFormat((JSONObject) value));
            else if (value instanceof JSONArray)
                ret.add(convertJsonFormat((JSONArray) value));
            else
                throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
        }
        return ret;
    }



    /**
     * Extracts available domain data from the provided eml file.
     * @param emailData	a stream of eml data
     * @return 	list of sifts objects with extracted data
     */
//    public List<Sift> discovery(String emailData) {
//        List<Sift> sifts = new ArrayList<>();
//        String path = "/v1/discovery";
//
//        Map<String,Object> params = new HashMap<>();
//        params.put("email", emailData.trim());
//
//        addCommonParams("POST", path, params);
//
////        JsonNode result = executeRequest(Unirest.post(API_ENDPOINT + path).fields(params));
////
////        Iterator<JsonNode> iter = result.elements();
////        while(iter.hasNext()) {
////            sifts.add(Sift.unmarshallSift(iter.next()));
////        }
//
//        return sifts;
//    }



    protected HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,
            Object> params) {
        params.put("api_key", apiKey);
        params.put("timestamp", System.currentTimeMillis() / 1000);
        params.put("signature", signatory.generateSignature(method, path, params));

        return params;
    }


    BaseGmailProvider() {
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
        signatory = new Signatory(apiSecret);
    }

    @Override
    protected void provide() {
        checkGmailApiRequirements();
    }

    /**
     * When the app just got the authorization and permission from the activity, it goes to this callback.
     */
    @Override
    public void onSuccess(Gmail service) {
        mService = service;
    }


    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail canceled."));
    }

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }


    public Map getMessageDetails(String messageId) {
        Map<String, Object> messageDetails = new HashMap<>();
        try {
            Message message = mService.users().messages().get("me", messageId).setFormat("raw").execute();

            byte[] emailBytes = Base64.decodeBase64(message.getRaw());

            if(message.getRaw()!=null){
                request(message.getRaw());
//                Logging.debug("-------");
//                for(Sift sift: sifts) {
//                        Logging.debug(sift.toString());
//                }
            }
            else{
                Logging.debug("null");
            }


            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

            messageDetails.put("subject", email.getSubject());
            messageDetails.put("from", email.getSender() != null ? email.getSender().toString() : "None");
            messageDetails.put("time", email.getSentDate() != null ? email.getSentDate().toString() : "None");
            messageDetails.put("snippet", message.getSnippet());
            messageDetails.put("threadId", message.getThreadId());
            messageDetails.put("id", message.getId());
            messageDetails.put("body", getText(email));
            Logging.debug(email.getSender() != null ? email.getSender().toString() : "None");
            Logging.debug(getText(email));
            Logging.debug(email.getSentDate() != null ? email.getSentDate().toString() : "None");

        } catch (MessagingException | IOException ex) {
            Logging.error(ex.getMessage());
        }
        return messageDetails;

    }


    private List<String> getDataFromApi(String query) throws IOException {
        List<String> messageList = new ArrayList<>();
        String user = "me";
        ListMessagesResponse response = mService.users().messages().list(user).setQ(query).execute();
        int total = 1;
        String deliverTo = "";
        String from = "";
        String subject = "";
        String content = "";
        long timestamp = 0;
        if (response.getMessages() != null) {
            for(int i = response.getMessages().size()-1;i>=0;i--){
                Message item = response.getMessages().get(i);


                if (total > mMaxResult) {
                    break;
                }
//                Message message = mService.users().messages().get(user, item.getId()).setFormat("raw").execute();
                getMessageDetails(item.getId());
//                List<MessagePart> messageParts = message.getPayload().getParts();
//                List<MessagePartHeader> headers = message.getPayload().getHeaders();
//                if(message.getRaw()!=null){
//                    List<Sift> sifts = discovery(message.getRaw());
//                    Logging.debug("-------");
//                    for(Sift sift: sifts) {
//                        Logging.debug(sift.toString());
//                    }
//                }
//                else{
//                    Logging.debug("null");
//                }
//
//                if (!headers.isEmpty()) {
//                    for (MessagePartHeader header : headers) {
//                        String name = header.getName();
//                        switch (name) {
//                            case "From":
//                                from = header.getValue();
//                                break;
//                            case "To":
//                                deliverTo = header.getValue();
//                                break;
//                            case "Subject":
//                                subject = header.getValue();
//                                break;
//                            case "Date":
//                                String date = header.getValue();
//                                if(date.contains(","))
//                                    date = date.substring(date.indexOf(",")+2,date.length());;
//                                String timestampFormat = "dd MMM yyyy HH:mm:ss Z";
//                                timestamp = TimeUtils.fromFormattedString(timestampFormat,date)/1000;
//                                break;
//                        }
//                    }
//                }
//                if (messageParts != null && !messageParts.isEmpty()) {
//                    byte[] bytes = Base64.decodeBase64(messageParts.get(0).getBody().getData());
//                    if (bytes != null) {
//                        String mailText = new String(bytes);
//                        if (!mailText.isEmpty()) {
//                            total++;
//                            content = mailText;
//                            messageList.add(mailText);
//                        }
//                    }
//                }
//                if(mLastEmailTime<timestamp) mLastEmailTime = timestamp;
                this.output(new Email(content, AppUtils.APP_PACKAGE_GMAIL, from, deliverTo, subject, timestamp));
            }
        }

        //Reset the value for from and to
        mBegin = 0;
        mEnd = 0;
        return messageList;
    }


    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    class FetchEmailTask extends AsyncTask<String, Void, List<String>> {

        /**
         * Background task to call Gmail API.
         * @param queries the gmail api query.
         */
        @Override
        protected List<String> doInBackground(String... queries) {

            try {
                return getDataFromApi(queries[0]);
            } catch (Exception e) {
                if (e instanceof UserRecoverableAuthIOException) {
                    Intent authorizationIntent = new Intent(getContext(),
                            GmailAuthorizationActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    authorizationIntent.setAction("UserRecoverableAuthIOException");
                    authorizationIntent.putExtra("request_authorization",
                            ((UserRecoverableAuthIOException) e).getIntent());

                    getContext().startActivity(authorizationIntent);
                } else {
                    Logging.error("The following error occurred:\n"
                            + e.getMessage());
                }
                return null;
            }

        }

    }

    String buildTimeQuery(Long... timeVar) {
        Long after = timeVar.length > 0 ? timeVar[0] : 0;
        Long before = timeVar.length > 1 ? timeVar[1] : 0;
        StringBuilder query = new StringBuilder(" ");
        if (after != 0) {
            query.append("after:");
            query.append(after);
            query.append(" ");
        }
        if (before != 0) {
            query.append("before:");
            query.append(before);
            query.append(" ");
        }
         query.append(" -category:{social promotions} ");
        Logging.debug(query.toString());
        return query.toString();
    }

    class PostAsync extends AsyncTask<HashMap<String,Object>, Void, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String LOGIN_URL = API_ENDPOINT+"/v1/discovery";


        @Override
        protected JSONObject doInBackground(HashMap<String, Object>... params) {

            try {

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params[0]);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {

            JsonNode result = convertJsonFormat(json);
            List<Sift> sifts = new ArrayList<>();
            Iterator<JsonNode> iter = result.elements();
            while(iter.hasNext()) {
                sifts.add(Sift.unmarshallSift(iter.next()));
            }
            for(Sift sift: sifts) {
                Logging.debug(sift.toString());
            }
        }

    }



    private void request(String emailData){

        String path = "/v1/discovery";
        HashMap<String,Object> params = new HashMap<>();
        params.put("email", emailData.trim());

        params = addCommonParams("POST", path, params);
        new PostAsync().execute(params);

    }
    private void checkGmailApiRequirements() {


        String accountName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(PREF_ACCOUNT_NAME, null);

        if (accountName != null) {
            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
                    getContext().getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            mCredential.setSelectedAccountName(accountName);

            if (!ConnectionUtils.isGooglePlayServicesAvailable(getContext())) {
                ConnectionUtils.acquireGooglePlayServices(getContext());
            }
            else{
                mService = new Gmail.Builder(
                        AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), mCredential)
                        .setApplicationName(AppUtils.getApplicationName(getContext()))
                        .build();
                authorized = true;
            }


        } else {
            GmailAuthorizationActivity.setListener(this);
            getContext().startActivity(new Intent(getContext(), GmailAuthorizationActivity.class));
        }

    }

}
