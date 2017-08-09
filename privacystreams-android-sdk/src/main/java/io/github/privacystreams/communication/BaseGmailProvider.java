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
import io.github.privacystreams.utils.TimeUtils;
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
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Base class for Gmail-related Providers.
 */

abstract class BaseGmailProvider extends PStreamProvider implements GmailResultListener {
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    private Gmail mService;
    int mMaxResult = Integer.MAX_VALUE;
    long mBegin = 0;
    long mEnd = 0;
    boolean authorized = false;
    long mLastEmailTime = 0;

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
                Message message = mService.users().messages().get(user, item.getId()).setFormat("full").execute();
                List<MessagePart> messageParts = message.getPayload().getParts();
                List<MessagePartHeader> headers = message.getPayload().getHeaders();

                if (!headers.isEmpty()) {
                    for (MessagePartHeader header : headers) {
                        String name = header.getName();
                        switch (name) {
                            case "From":
                                from = header.getValue();
                                break;
                            case "To":
                                deliverTo = header.getValue();
                                break;
                            case "Subject":
                                subject = header.getValue();
                                break;
                            case "Date":
                                String date = header.getValue();
                                if(date.contains(","))
                                    date = date.substring(date.indexOf(",") + 2,date.length());;
                                String timestampFormat = "dd MMM yyyy HH:mm:ss Z";
                                timestamp = TimeUtils.fromFormattedString(timestampFormat,date) / 1000;
                                break;
                        }
                    }
                }
                if (messageParts != null && !messageParts.isEmpty()) {
                    byte[] bytes = Base64.decodeBase64(messageParts.get(0).getBody().getData());
                    if (bytes != null) {
                        String mailText = new String(bytes);
                        if (!mailText.isEmpty()) {
                            total++;
                            content = mailText;
                            messageList.add(mailText);
                        }
                    }
                }
                if(mLastEmailTime < timestamp) mLastEmailTime = timestamp;
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
                .getString(PREF_ACCOUNT_NAME, null);

        if (accountName != null) {
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
                authorized = true;
            }


        } else {
            GmailAuthorizationActivity.setListener(this);
            getContext().startActivity(new Intent(getContext(), GmailAuthorizationActivity.class));
        }

    }

}
