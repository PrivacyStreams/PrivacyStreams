package io.github.privacystreams.communication.emailinfo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;


public class EmailInfoProvider extends PStreamProvider implements EmailAccountNameListener {

    private static final String EDISON_API_BASE_URL = "https://api.edison.tech";

    /*Credentials*/
    private String mApiKey;
    private String mApiSecret;
    private String mConnectToken;
    private String mUserName;
    private Signatory mSignatory;

    /*Configs*/
    private String mDomain;

    /*Statuses used to deal with SIFT API*/
    private static final String STATUS_ADD_USER = "add_user";
    private static final String STATUS_LIST_SIFTS = "list_sifts";
    private static final String STATUS_CONNECT_TOKEN = "connect_token";
    private static final String STATUS_CHECK_CONNECTION = "check_connection";


    private static final String GMAIL_PREF_NAME = "USER_NAME";
    private static final String TOKEN = "CONNECT_TOKEN";

    private boolean mIsConnected = false;

    private final ObjectMapper mObjectMapper = new ObjectMapper();


    /**
    @params: api_key: the api_key generated on developer's sift account
    @params: api_secret: the api_secret generated on developer's sift account
     */
    public EmailInfoProvider(String key, String secret, String domain){
        mDomain = domain;
        mApiKey = key;
        mApiSecret = secret;
        mSignatory = new Signatory(mApiSecret);
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.ACCESS_NETWORK_STATE);
    }

    //TODO delete this function when debug ends
    public EmailInfoProvider(String key, String secret, String domain, String userName){
        mDomain = domain;
        mApiKey = key;
        mApiSecret = secret;
        mSignatory = new Signatory(mApiSecret);
        mUserName = userName;
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
    }



    /**
     * Callback when get connect token
     */
    private void onSiftSetupSuccess(){
        listSifts(mUserName,mDomain);
    }

    @Override
    /**
     * Callback when user choose account fail
     */
    public void onFail() {

    }

    /**
     * Callback when email info has been got
     * @param jsonNode Email info got
     */
    public void isSiftAvailable(JsonNode jsonNode){

    }

    @Override
    /**
     * Callback when user choose account succeed
     */
    public void onSuccess(String name) {
        mUserName = name;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(GMAIL_PREF_NAME, name);
        editor.apply();
        addUser(name,"en_US");
    }

    @Override
    protected void provide() {
        setupSiftApi();
    }

    /**
     * Check whether user has confirmed in a given time
     * @param checkingStartedTime When starting to check
     * @return Whether time is out
     */
    private boolean timeIsOut(long checkingStartedTime){
        return System.currentTimeMillis() - checkingStartedTime > Globals.SiftConfig.checkSiftConnectionTimeout;
    }

    /**
     * Setup EmailInfoProvider and start to make http request to sift.
     */
    protected void setupSiftApi() {
        GmailChooseAccountActivity.setListener(this);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(TOKEN, null);
        if(token != null) {
            Logging.debug("already got token");
            onSiftSetupSuccess();
            return;
        }

        if(mUserName == null){
            mUserName = PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getString(GMAIL_PREF_NAME, null);
            if(mUserName == null) {
                Intent chooseAccountIntent = new Intent(getContext(), GmailChooseAccountActivity.class);
                getContext().startActivity(chooseAccountIntent);
            }
        }
        else{
            Logging.debug("already signed in");
            addUser(mUserName,"en_US");
        }

        long checkingStartedTime = System.currentTimeMillis();
        while(!mIsConnected
                && !timeIsOut(checkingStartedTime)){
            try{
                Thread.sleep(Globals.SiftConfig.checkSiftConnectionPollingInterval);
            }
            catch (Exception e){
                Logging.error("Connection Exception");
            }
            finally{
                checkEmailConnection(mUserName);
            }
        }
        if(!mIsConnected){
            Logging.error("Connection Error");
       }

    }

    /**
     * Add a username to developer's sift account.
     * @param userName: A name specified by developer. It can be everything.
     * @param locale: user's locale. e.g: "en_US"
     */

    private void addUser(String userName, String locale){
        Logging.error("addUser starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users";
        String method = "POST";
        params.put("username", userName);
        params.put("locale", locale);
        params = addCommonParams(method, path, params);
        String requestUrl = generateUrl(path, params);
        new WebRequests().execute(requestUrl, method, STATUS_ADD_USER);
    }

    /**
     * Get a connect token of the username
     * @param userName: The username specified by developer
     */

    private void getConnectToken(String userName){
        Logging.error("getConnectToken starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_token";
        String method = "POST";
        params.put("username", userName);
        params = addCommonParams(method,path, params);
        String requestUrl = generateUrl(path, params);
        new WebRequests().execute(requestUrl,method,STATUS_CONNECT_TOKEN);
    }

    /**
    * Let the user confirm to log in.
    * Will pop up the browser to load the url
    * @param userName The username specified by developer
    * @param token The token got from function getConnectToken(String)
    */
    private void connectEmail(String userName, String token){
        Logging.error("connectEmail starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_email";
        params.put("api_key", mApiKey);
        params.put("username", userName);
        params.put("token", token);
        String requestUrl = generateUrl(path,params);
        try {

            Uri uri = Uri.parse(requestUrl);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            getContext().startActivity(browserIntent);
        }catch(Exception e){
            Logging.error("webactivity exception: "+e.getMessage());
        }
    }

    /**
     * List sift information
     * @param username The username specified by developer
     * @param domain Specify what kind of information you want to get
     */
    private void listSifts(String username, String domain){
        Logging.error("list sifts start");
        HashMap<String, Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/sifts";
        String method = "GET";
        params.put("username",username);
        if(domain!=null){
            params.put("domains",domain);
        }
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,STATUS_LIST_SIFTS);
    }

    /**
     * Check whether the user's email is connected.
     * @param userName
     */
    private void checkEmailConnection(String userName){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+userName +"/email_connections";
        String method = "GET";
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl, method, STATUS_CHECK_CONNECTION);
    }

    /**
     * Generate url for http request
     * @param path The endpoint of the request
     * @param params The query strings of the request
     * @return Request Url
     */
    private String generateUrl(String path, HashMap<String,Object> params){
        String url = EDISON_API_BASE_URL + path + "?";
        List<String> keys = new ArrayList<>(params.keySet());
        boolean notFirst = false;
        for(String key : keys){
            if(notFirst){
                url += "&";
            }
            else{
                notFirst = true;
            }
            url += key + "=" + params.get(key);
        }
        return url;
    }

    /**
     * Add mApikey,timestamp and signature to query string
     * @param method The method to open a http connection. e.g "POST"
     * @param path The endpoint of the request
     * @param params The query strings of the point
     * @return Complete query strings
     */

    private HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,Object> params){
        params.put("api_key", mApiKey);
        params.put("timestamp", System.currentTimeMillis() / 1000L);
        params.put("signature", mSignatory.generateSignature(method, path, params));
        return params;
    }

    /**
     * Do http requests in background
     */

    private class WebRequests extends AsyncTask<String,Void,String> {

        private String getResponseText(InputStream in) {
            return new Scanner(in).useDelimiter("\\A").next();
        }

        @Override
        /**
         * Do http requests using given url and method
         */
        protected String doInBackground(String... params) {
            String url = params[0]; //the url to request
            if(url!=null){
                Logging.error("url is:"+url);
            }
            String method = params[1];  // the method for http request
            String status = params[2];    // the called function
            String responseString = "";
            HttpURLConnection urlConnection = null;
            JSONObject responseJson;
            try {
                URL urlToRequest = new URL(url);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod(method);
                int statusCode = urlConnection.getResponseCode();
                if (statusCode==200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = getResponseText(in);
                } else {
                    Logging.error("request error with code:" + statusCode);
                }
            }
            catch (Exception e) {
                Logging.error("url conn error:" + e.getMessage());
            } finally {
                if (urlConnection!=null) {
                    urlConnection.disconnect();
                }
            }
            switch (status) {
                case STATUS_LIST_SIFTS:
                    try {
                        JsonNode root = mObjectMapper.readTree(responseString);
                        JsonNode result = root.get("result");
                        for(JsonNode each: result){
                            Log.e("---------","---------------");
                            JsonNode payload = result.has("@type") ? each : each.get("payload");
                            String type = payload.get("@type").textValue();
                            if(type.startsWith("x-")) {
                                type = type.substring(2);
                                Logging.error("type is:"+type);
                            }
                            else{
                                Logging.error("type is:"+type);
                            }
                            Logging.error("payload:"+payload.toString());

                            isSiftAvailable(payload);
                        }

                    }
                    catch (Exception e) {
                        Logging.error("parse json failed for list sifts");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;

                case STATUS_ADD_USER:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                    } catch (Exception e) {
                        Logging.error("parse json failed for user id");
                        Logging.error("exception is" + e.getMessage());

                    }
                    break;
                case STATUS_CONNECT_TOKEN:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                        mConnectToken = responseJson.getJSONObject("result").get("connect_token").toString();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString(TOKEN, mConnectToken);
                        editor.apply();
                    }
                    catch (Exception e) {
                        Logging.error("parse json failed for connect token");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;

                case STATUS_CHECK_CONNECTION:
                    try {
                        JsonNode root = mObjectMapper.readTree(responseString);
                        Logging.error("json is:" + root);
                        JsonNode temp = root.get("result").get(0);
                        if(temp != null){
                            mIsConnected = true;
                            onSiftSetupSuccess();
                        }
                    }
                    catch (Exception e) {
                        Logging.error("parse json failed for user id");
                        Logging.error("exception is" + e.getMessage());
                    }
                default:

            }
            return status;
        }

        @Override
        /**
         * Give a callback when a http request ends
         */
        protected  void onPostExecute(String lastStatus){
            Logging.error("last step is: "+ lastStatus);
            switch(lastStatus){
                case STATUS_ADD_USER:
                    getConnectToken(mUserName);
                    break;

                case STATUS_CONNECT_TOKEN:
                    connectEmail(mUserName,mConnectToken);
                    break;

                case STATUS_LIST_SIFTS:
                case STATUS_CHECK_CONNECTION:
                    break;
                default:
                    Logging.error("something strange happened");
            }
        }
    }
}