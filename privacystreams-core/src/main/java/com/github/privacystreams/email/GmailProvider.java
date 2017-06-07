package com.github.privacystreams.email;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by lenovo on 2017/5/22.
 */

public class GmailProvider extends MStreamProvider implements GmailResultListener{
    private final String TAG = "GMAIL";
    private static List<String> messageList;
    public static GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS,GmailScopes.GMAIL_READONLY };
    private String timestampFormat = "dd MMM yyyy HH:mm:ss";
    private String stringFormat = "yyyy/MM/dd HH:mm:ss";
    private com.google.api.services.gmail.Gmail mService = null;
    private String labelId = null;
    private List<Message> newMessageList;
    private List<String> headerList;
    private final String flightNum ="([^a-z0-9])(([a-z][a-z]|[a-z][0-9]|[0-9][a-z])[a-z]?)([0-9]{3,4}[a-z]?[^a-z0-9])";
    private final String dates ="([0-3]?[0-9])(,)?(\\s|-)?(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(,)?(\\s|-)?((19|20)?[0-9]{2})";
    private final String[] companyEmailList = {"reservations@jetblue.com"};
    private final List<String> flightCompanyEmailList = new ArrayList<String>(Arrays.asList(companyEmailList));
    private final String[] thirdPartyEmailList = {"noreply@vayama.com","info@travelplanner.flyfrontier.com","chenfanglintc@gmail.com"};
    private final List<String> thirdPartyFlightCompanyEmailList = new ArrayList<String>(Arrays.asList(thirdPartyEmailList));
    private Long lastTime = null;
    private final int  maxResult = 4;

    GmailProvider(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,Manifest.permission.GET_ACCOUNTS,Manifest.permission.ACCESS_NETWORK_STATE);

    }
    @Override
    protected void provide() {
        //Log.e(TAG,"You get here");
        getGmailInfo();
    }
    private void getGmailInfo(){
        Log.e("Gmail","Starting Looking for gmails");
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        messageList = null;
        headerList = new ArrayList<String>();
        GmailActivity.setListener(this);
        Intent intent = new Intent(this.getContext(), GmailActivity.class);
        this.getContext().startActivity(intent);
    }
    @Override
    public void onSuccess() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        Log.e("Test","Get to make request");
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Gmail API Android Quickstart")
                .build();
        new MakeRequestTask().execute();
        Log.e("TAG","Here 2");
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail canceled."));
    }

    private List<String> getDataFromApi(ListMessagesResponse response){
        List<String> messageList = new ArrayList<>();
        //String query = buildEmailListQuery(thirdPartyFlightCompanyEmailList);

        try{
            int total =1;
//                findFlightXML("PIT","KJFK","1496008024","3856");
//                findFlightInfo("JBU497");
            String diliverTo = "";
            String from = "";
            String subject = "";
            String content = "";
            Long timestamp = null;
            Long lastestTimeStamp = null;
            if(response.getMessages()!=null){
                for(Message item : response.getMessages()){
                    if(total>maxResult){
                        break;
                    }
                    //Log.e("Test item id",item.getId());
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
                                //findMatches(mailText);
                                content = mailText;
                                messageList.add(mailText);
                            }
                        }
                    }
                    if(lastestTimeStamp==null) lastestTimeStamp = timestamp;
                    else if(lastestTimeStamp<timestamp) lastestTimeStamp = timestamp;

                    if(content!=null) this.output(new Email(content,"Gmail",from,diliverTo,subject,timestamp));
                }
            }
            if(lastestTimeStamp!=null)
                lastTime =lastestTimeStamp;
        } catch (Exception e){
            e.printStackTrace();
        }
        return messageList;
    }

    //This method will build up the query string for filtering desperately
    //Input a list of email strings
    // Output the query string use for filtering
    private String buildEmailListQuery(List<String> checkList){
        StringBuilder query = new StringBuilder("");
        query.append("{");
        for(String email:checkList){
            query.append("from:");
            query.append(email);
            query.append(" ");
        }
        query.append("}");
        return query.toString();
    }
    private String buildTimeQuery(Long time){
        StringBuilder query = new StringBuilder("");
        query.append(" -category:{social promotions updates forums} ");
        if(time!=null){
            query.append("after:");
            query.append(time);
        }
        return query.toString();
    }

    private List<String> findMatches(String emailText){
        List<String> fnList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        final Pattern patternFN = Pattern.compile(flightNum, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        final Matcher matcherFN = patternFN.matcher(emailText);
        final Pattern patternDates = Pattern.compile(dates, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        final Matcher matcherDates = patternDates.matcher(emailText);
        while (matcherFN.find()) {
            String FN =  matcherFN.group();
            Log.e("Test","Full match: " +FN.substring(1,FN.length()-1));
            fnList.add(FN);
        }
        while (matcherDates.find()) {
            Log.e("Test","Full match: " + matcherDates.group(0));
        }

        return fnList;
    }

    //Input: method(POST, GET), inputName(sections like flight number, Date, airport), inputValue(values of inputName)
    //Output: String that is used by findFlightXML
    private String buildURL(String method,String[] inputName,String[] inputValue){
        final String URL = "http://flightxml.flightaware.com/json/FlightXML2/";

        StringBuilder url =new StringBuilder(URL);
        url.append(method);
        if(inputName!=null){
            url.append("?");
            for(int i = 0; i<inputName.length;i++){
                url.append(inputName[i]);
                url.append("=");
                url.append(inputValue[i]);
                if(i<inputName.length-1)
                    url.append("&");
            }
        }
        Log.d("Flight", "URL used: " + url.toString());
        return url.toString();
    }

    private void findFlightInfo(String flightNum){
        String method = "FlightInfoEx";
        String[] inName = {"ident","howMany","offset"};
        String[] inValue = {flightNum, "5", "0"};
        String url = buildURL(method,inName,inValue);
        Log.e("Url",url);
        new RequestWebTask().execute(url);
    }

    private void findFlightXML(String origin,String destination,String date,String flight){
        long sDate = Long.parseLong(date);
        long eDate = sDate+100000;
        String sDateS = Long.toString(sDate);
        String eDateS = Long.toString(eDate);
        String method = "AirlineFlightSchedules";
        String[] inName = { "startDate", "endDate", "origin", "destination","flightno", "howMany", "offset" };
        String[] inValue = { sDateS, eDateS, origin, destination, flight, "30","0"};
        String url = buildURL(method,inName,inValue);
        new RequestWebTask().execute(url);
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
            Log.e("Test","do in back ground");
            String query = buildTimeQuery(lastTime);
            try {
                String user = "me";
                ListMessagesResponse response = mService.users().messages().list(user).setQ(query).execute();
                return getDataFromApi(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<String> output) {
            Log.e("Test","end Asytn");
            GmailProvider.this.finish();
        }

    }
    //******************************** Inner Class ********************************///
    public static class RequestWebTask extends  AsyncTask<String,String,JSONObject>{
        public static final String TAG = "WebService";
        public static final int CONNECTION_TIMEOUT = 30000;
        public static final int DATA_READ_TIMEOUT = 30000;
        public static final String username = "fanglin";
        public static final String password = "209d3487c396edf7fb4d17c9a922c70a19e73056";
        //Function to get response Text
        // Output;String
        private String getResponseText(InputStream in){
            return new Scanner(in).useDelimiter("\\A").next();
        }
        //Function to get JSON Respond
        //Do in background
        //Input: Url
        //Output JSONObject
        @Override
        protected JSONObject doInBackground(String... url) {
            JSONObject jsonObject = null;
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(username,password.toCharArray());
                }
            });

            HttpURLConnection urlConnection = null;
            try {
                URL urlToRequest = new URL(url[0]);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATA_READ_TIMEOUT);
                urlConnection.setRequestMethod("POST");

                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.d(TAG, "HTTP Error Code: " + statusCode);
                    return null;
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "HTTP Error Code: " + statusCode);
                    return null;
                } else {
                    Log.e("Message", urlConnection.getResponseMessage() + "");
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    Log.e("Messages",getResponseText(in));
                    jsonObject = new JSONObject(getResponseText(in));
                    return jsonObject;
                }
            } catch (JSONException | IOException e) {
                //URL is invalid
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        // Exit
        //Input JSONObject
        //Series of operation

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.i(TAG, "JsonObject: " + jsonObject);
        }
    }

}