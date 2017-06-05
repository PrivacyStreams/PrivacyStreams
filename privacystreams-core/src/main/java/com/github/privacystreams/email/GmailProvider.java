package com.github.privacystreams.email;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.notification.PSNotificationListenerService;
import com.github.privacystreams.utils.ConnectionUtils;
import com.github.privacystreams.utils.PSPermissionActivity;
import com.github.privacystreams.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.gmail.GmailScopes;

import com.google.api.services.gmail.model.*;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

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

    private com.google.api.services.gmail.Gmail mService = null;
    private Exception mLastError = null;
    private String labelId = null;
    private List<Message> newMessageList;
    private final String flightNum ="([^a-z0-9])(([a-z][a-z]|[a-z][0-9]|[0-9][a-z])[a-z]?)([0-9]{3,4}[a-z]?[^a-z0-9])";
    private final String dates ="([0-3]?[0-9])(,)?(\\s|-)?(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(,)?(\\s|-)?((19|20)?[0-9]{2})";
    private final String[] companyEmailList = {"reservations@jetblue.com"};
    private final List<String> flightCompanyEmailList = new ArrayList<String>(Arrays.asList(companyEmailList));
    private final String[] thirdPartyEmailList = {"noreply@vayama.com","info@travelplanner.flyfrontier.com","chenfanglintc@gmail.com"};
    private final List<String> thirdPartyFlightCompanyEmailList = new ArrayList<String>(Arrays.asList(thirdPartyEmailList));

    GmailProvider(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,Manifest.permission.GET_ACCOUNTS,Manifest.permission.ACCESS_NETWORK_STATE);

    }
    @Override
    protected void provide() {
        getGmailInfo();
    }
    private void getGmailInfo(){
        Log.e("Gmail","Starting Looking for gmails");
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        messageList = null;
        GmailActivity.setListener(this);
        Intent intent = new Intent(this.getContext(), GmailActivity.class);
        this.getContext().startActivity(intent);
    }
    @Override
    public void onSuccess() {
        Log.e("Permession test","All success");
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        Log.e("Test","Get to make request");
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Gmail API Android Quickstart")
                .build();
        try {
            getDataFromApi();
        } catch (Exception e) {
            Log.e("Test","In error");
            e.printStackTrace();
        }
        Log.e("TAG","Here 2");
//        this.finish();
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail canceled."));
    }

    private List<String> getDataFromApi(){
        List<String> messageList = new ArrayList<>();
        String query = buildQuery(thirdPartyFlightCompanyEmailList);
        Log.e("Test","Query "+query);
        try{
            String user = "me";
            //               ListMessagesResponse response = mService.users().messages().list(user).setQ("label:^smartlabel_receipt").execute();
//                ListMessagesResponse response = mService.users().messages().list(user).setQ("from:moisheebay@gmail.com").execute();
            ListMessagesResponse response = mService.users().messages().list(user).setQ(query).execute();
//                ListMessagesResponse response = mService.users().messages().list(user).setQ("{from:chenfanglintc@gmail.com}").execute();
            int total =1;
//                findFlightXML("PIT","KJFK","1496008024","3856");
//                findFlightInfo("JBU497");
            if(response.getMessages()==null) Log.e("Test Message","Get Message Null");
            else Log.e("Test Message","Has");
            for(Message item : response.getMessages()){
                if(total>4){
                    break;
                }
                //Log.e("Test item id",item.getId());
                Message message = mService.users().messages().get(user,item.getId()).execute();
                List<MessagePart> messageParts = message.getPayload().getParts();
                if(messageParts==null) Log.e("Test Message","null");
                if (messageParts!=null&&!messageParts.isEmpty()) {
                    byte[] bytes = Base64.decodeBase64(messageParts.get(0).getBody().getData());
                    if(bytes != null){
                        String mailText = new String(bytes);
                        if(!mailText.isEmpty()){
                            total++;
                            //findMatches(mailText);
                            Log.e("Message",mailText);
                            GmailProvider.this.output(new Email(mailText,"Gmail",null));
                            messageList.add(mailText);
                        }
                    }
                }
            }

        }catch (UserRecoverableAuthIOException e) {
            //startActivityForResult(e.getIntent(),REQUEST_AUTHORIZATION);
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return messageList;
    }

    //This method will build up the query string for filtering desperately
    //Input a list of email strings
    // Output the query string use for filtering
    private String buildQuery(List<String> checkList){
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
