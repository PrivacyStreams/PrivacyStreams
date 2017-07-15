package com.github.privacystreams.communication;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    static final String GMAIL_PREF_ACCOUNT_NAME = "gmail accountName";
    static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    private Gmail mService;
    int mMaxResult = Globals.EmailConfig.defaultMaxNumberOfReturnResults;
    long mBegin = 0;
    long mEnd = 0;
    boolean authorized = false;
    long mLastEmailTime = 0;
    public static final String API_ENDPOINT = "https://api.easilydo.com";
    private String API_KEY = "9aed6c22c1623a1bfd6473a4328f85b0";
    private String API_SECRET = "fa53d475c36bc18ea66196afa23cc2f35ea6012c";




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


    private void checkGmailApiRequirements() {


        String accountName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(GMAIL_PREF_ACCOUNT_NAME, null);

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
