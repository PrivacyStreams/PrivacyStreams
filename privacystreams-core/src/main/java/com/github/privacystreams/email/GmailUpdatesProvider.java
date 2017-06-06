package com.github.privacystreams.email;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.TimeUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the provider which will constantly updates the email information and output it to the uqi
 */
 class GmailUpdatesProvider extends MStreamProvider implements GmailResultListener{
    private final String TAG = "GmailUpdates";
    private com.google.api.services.gmail.Gmail mService;
    private long lastTime = 0;
    private int  maxResult = 2;
    static GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS,GmailScopes.GMAIL_READONLY };
    GmailUpdatesProvider(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,Manifest.permission.GET_ACCOUNTS,Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    protected void provide() {
        getGmailInfo();
    }
    private void getGmailInfo(){
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        GmailUpdatesActivity.setListener(this);
        Intent intent = new Intent(this.getContext(), GmailUpdatesActivity.class);
        this.getContext().startActivity(intent);
    }

    @Override
    public void onSuccess() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Gmail API Android Quickstart")
                .build();

        final Handler handler = new Handler();                          //The timer for the AsyncTask
        Timer timer = new Timer();
        TimerTask doEmailUpdatesTask = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            new MakeRequestTask().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doEmailUpdatesTask,0, Globals.EmailConfig.pullingInterval);
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail Updates canceled."));
    }

    //Build up the query string for filter the emails
    private String buildTimeQuery(long time){
        StringBuilder query = new StringBuilder("");
        query.append(" -category:{social promotions updates forums} ");
        if(time!=0){
            query.append("after:");
            query.append(time);
        }
        return query.toString();
    }

    private List<String> getDataFromApi(){
        List<String> messageList = new ArrayList<>();
        try{
            String  query= buildTimeQuery(lastTime);
            String user = "me";
            ListMessagesResponse response= mService.users().messages().list(user).setQ(query).execute();
            int total =1;
            String diliverTo = "";
            String from = "";
            String subject = "";
            String content = "";
            long timestamp = 0;
            long lastestTimeStamp = 0;
            Log.e("Test","do in back ground");
            if(response.getMessages()!=null){
                for(Message item : response.getMessages()){
                    if(total>maxResult){
                        break;
                    }
                    Message message = mService.users().messages().get("me",item.getId()).setFormat("full").execute();
                    List<MessagePart> messageParts = message.getPayload().getParts();
                    List<MessagePartHeader> headers = message.getPayload().getHeaders();

                    if(!headers.isEmpty()){
                        for(MessagePartHeader header:headers) {
                            String name = header.getName();
                            switch (name){
                                case "From":
                                    from = header.getValue();
                                    break;
                                case "To":
                                    diliverTo = header.getValue();
                                    break;
                                case "Subject":
                                    subject = header.getValue();
                                    break;
                                case "Date":
                                    String date = header.getValue();
                                    date = date.substring(date.indexOf(",")+2,date.length()-5);
                                    String timestampFormat = "dd MMM yyyy HH:mm:ss";
                                    timestamp = TimeUtils.fromFormattedString(timestampFormat,date);
                                    break;
                            }
                        }
                    }
                    if (messageParts!=null&&!messageParts.isEmpty()) {
                        byte[] bytes = Base64.decodeBase64(messageParts.get(0).getBody().getData());
                        if(bytes != null){
                            String mailText = new String(bytes);
                            if(!mailText.isEmpty()){
                                total++;
                                content = mailText;
                                messageList.add(mailText);
                            }
                        }
                    }
                    if(lastestTimeStamp==0) lastestTimeStamp = timestamp;
                    else if(lastestTimeStamp<timestamp) lastestTimeStamp = timestamp;
                    if(content!=null)
                        this.output(new Email(content,"Gmail",from,diliverTo,subject,timestamp));
                }
            }
            if(lastestTimeStamp!=0)
                lastTime =lastestTimeStamp;
        } catch (Exception e){
            e.printStackTrace();
        }
        return messageList;
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
                return getDataFromApi();
        }
        @Override
        protected void onPostExecute(List<String> output) {
            Log.e("Test","end Asytn");
        }
    }
}
