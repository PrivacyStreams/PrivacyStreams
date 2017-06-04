package com.github.privacystreams.email;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;;
import android.text.TextUtils;
import android.util.Log;
import com.github.privacystreams.utils.ConnectionUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;

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

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lenovo on 2017/6/4.
 */

public class GmailActivity extends Activity {
    private static final String TAG = "GmailActivity - ";
    private static int state = 0;
    public static GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS,GmailScopes.GMAIL_READONLY };
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int UPLOAD_INFORMATION = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static GmailResultListener gmailResultListener;

    static void setListener(GmailResultListener gl){
     gmailResultListener = gl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (gmailResultListener!=null) {
            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            getResultsFromApi();

//            if(!gmailResultListener.isEmpty()){
//                Intent intent = new Intent(GmailActivity.this,GmailActivity.class);
//                startActivityForResult(intent,UPLOAD_INFORMATION);
//            }
//            Intent result = new Intent();
//            setResult(UPLOAD_INFORMATION,result);
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_CANCELED, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.e(TAG,"This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
            case UPLOAD_INFORMATION:
                gmailResultListener.onSuccess();
                break;
            case RESULT_CANCELED:
                Log.e(TAG,"cancelled");
                gmailResultListener.onFail();
                break;
        }
        finish();
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! ConnectionUtils.isGooglePlayServicesAvailable(this)) {
            ConnectionUtils.acquireGooglePlayServices(this);
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! ConnectionUtils.isDeviceOnline(this)) {
            Log.e(TAG,"No network connection available.");
        } else {
            Log.e("Permession test","All success");
            new MakeRequestTask(mCredential).execute();
            //gmailResultListener.onSuccess();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    private void chooseAccount() {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
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
        //        private GmailProvider provider;
        MakeRequestTask(GoogleAccountCredential credential) {
//            provider = provider;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            Log.e("Test","Get to make request");
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {

                return getDataFromApi();
            } catch (Exception e) {
                Log.e("Test","In error");
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         * @return List of Strings labels.
         */
//        private List<String> getDataFromApi() throws IOException {
//            String user = "me";
//            List<String> labels = new ArrayList<String>();
//            ListMessagesResponse listResponse = mService.users().messages().list(user).setQ("from:Google").execute();
//            //int i=0;
//            for (Message label : listResponse.getMessages()) {
//                Message m =  mService.users().messages().get(user, label.getId()).execute();
//                String a ="";
//                try{
//                    List<MessagePart> parts  =m.getPayload().getParts();
//                    List<MessagePartHeader> headers = m.getPayload().getHeaders();
//
//                    for(MessagePartHeader header:headers){
//                        String name = header.getName();
//                        if(name.equals("From")||name.equals("from")){
//                           labels.add(header.getValue());
//                            Log.e("Header",header.getValue());
//                            break;
//                        }
//                    }
//                }catch(Exception e){
//                    a+="The following error occurred:\n" +
//                            e.getMessage();
//                }
//
//            }
//            return labels;//extractData(labels);
//        }


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
                    if(total>5){
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
                                //Log.e("Message",mailText);
                                messageList.add(mailText);
                            }
                        }
                    }
                }

            }catch (UserRecoverableAuthIOException e) {
                startActivityForResult(e.getIntent(),REQUEST_AUTHORIZATION);
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            gmailResultListener.setList(messageList);
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
        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
//            PSPermissionActivity.mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            //PSPermissionActivity.mProgress.hide();
            if (output == null || output.size() == 0) {
                Log.e(TAG,"No results returned.");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
                Log.e(TAG, TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            //PSPermissionActivity.mProgress.hide();
            if (mLastError != null) {
                Log.e("Test error ",mLastError.toString());
            } else {
                Log.e("Test error","Request cancelled.");
            }
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
