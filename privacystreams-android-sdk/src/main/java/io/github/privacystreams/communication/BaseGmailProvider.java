package io.github.privacystreams.communication;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.AppUtils;
import io.github.privacystreams.utils.DeviceUtils;
import io.github.privacystreams.utils.Logging;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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

abstract class BaseGmailProvider extends PStreamProvider implements GmailResultListener {
    static final String GMAIL_PREF_ACCOUNT_NAME = "accountName";
    static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};

    private Gmail mService;
    int mMaxResult = Integer.MAX_VALUE;
    long mBegin = 0;
    long mEnd = 0;
    boolean authorized = false;
    long mLastEmailTime = 0;

    public static final String API_ENDPOINT = "https://api.easilydo.com";
    private String API_KEY = "e6b0dc00388a72b4d39043fa447660f2";
    private String API_SECRET = "8705f9438b3c1c59522afbc430189c57329418a3";

    private String userToken;

    BaseGmailProvider() {
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);

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
            return (String) p.getContent();
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


    private List<String> getDataFromApi(String query)  throws IOException{
        Logging.error("start get data");

        Gmail.Users.Messages.List request = mService.users().messages().list("me")
                // or setQ("is:sent after:yyyy/MM/dd before:yyyy/MM/dd")
                .setLabelIds(Collections.singletonList("SENT"))
                .setQ("from:----");

        List<Message> list = new LinkedList<>();
        ListMessagesResponse response = null;
        Logging.error("here");
        do {
            response = request.execute();
            Logging.error("after execute");
            list.addAll(response.getMessages());
            request.setPageToken(response.getNextPageToken());
            Logging.error("messageinfo:"+list.get(0).toString());
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);
        return null;
    }



    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    class FetchEmailTask extends AsyncTask<String, Void, List<String>> {

        /**
         * Background task to call Gmail API.
         *
         * @param queries the gmail api query.
         */
        @Override
        protected List<String> doInBackground(String... queries) {
            Logging.error("trying to get info");
            try {

                return getDataFromApi(queries[0]);

            } catch (Exception e) {
                if (e instanceof UserRecoverableAuthIOException) {
                    Logging.error("success get info2");
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
                    e.printStackTrace();
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


    private void checkGmailApiRequirements() {


        String accountName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(GMAIL_PREF_ACCOUNT_NAME, null);

        if (accountName != null) {
            Logging.error("accountName is : "+accountName);

            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
                    getContext().getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            mCredential.setSelectedAccountName(accountName);


            if (!DeviceUtils.isGooglePlayServicesAvailable(getContext())) {
                DeviceUtils.acquireGooglePlayServices(getContext());
            }
            else{
                mService = new Gmail.Builder(
                        AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), mCredential)
                        .setApplicationName(AppUtils.getApplicationName(getContext()))
                        .build();
                try {
                    userToken = mCredential.getToken();
                    Logging.error("Token is:" + userToken);
                }catch(Exception e){
                    Logging.error("get token failed:"+e.getMessage());
                }

                try {
                    SiftEmail sift = new SiftEmail(accountName,userToken);
//                    sift.main();
                }catch(Exception e){
                    Logging.error("sift failed:"+e.getMessage());
                }
                authorized = true;
            }


        } else {
            GmailAuthorizationActivity.setListener(this);
            getContext().startActivity(new Intent(getContext(), GmailAuthorizationActivity.class));
            Logging.error("select account ends");
        }

    }

}
