package io.github.privacystreams.communication;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.R;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;


public class EmailInfoProvider extends PStreamProvider implements EmailAccountNameListener{

    private static final String EDISON_API_BASE_URL = "https://api.edison.tech";
    private final String GMAIL_PREF_NAME = "userName";
    private final String TOKEN = "connectToken";

    /*Credentials*/
    private String mApiKey;
    private String mApiSecret;
    private String mConnectToken;
    private String mUserName;
    private Signatory mSignatory;

    /*Configs*/
    private String mDomain;

    /*Statuses used to deal with SIFT API*/
    private final String STATUS_ADD_USER = "add_user";
    private final String STATUS_LIST_SIFTS = "list_sifts";
    private final String STATUS_CONNECT_TOKEN = "connect_token";
    private final String STATUS_CHECK_CONNECTION = "check_connection";


    private boolean mIsConnected = false;

    private final ObjectMapper mObjectMapper = new ObjectMapper();


    /**
    @params: api_key: the api_key generated on developer's sift account
    @params: api_secret: the api_secret generated on developer's sift account
     */
    public EmailInfoProvider(String key, String secret, String domain){
            mDomain = domain;
            if(key!= null && key.isEmpty()) {
                mApiKey = key;
                mApiSecret = secret;

                this.addRequiredPermissions(Manifest.permission.INTERNET,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.ACCESS_NETWORK_STATE);
            }

    }

    private void onSiftSetupSuccess(){

        listSifts(mUserName,null,null,mDomain);
    }

    @Override
    public void onFail() {

    }

    public void isSiftAvailable(JsonNode jsonNode){

    }

    @Override
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


    private boolean timeIsOut(long checkingStartedTime){
        return System.currentTimeMillis() - checkingStartedTime > Globals.SiftConfig.checkSiftConnectionTimeout;
    }

    /* just for test when debug*/
    //TODO Change this function when debug ends

    protected void setupSiftApi(){
        /*
        GmailChooseAccountActivity.setListener(this);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(TOKEN, null);
        if(token != null) {
            Logging.debug("already got token");
            onSiftSetupSuccess();
            return;
        }


        mUserName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(GMAIL_PREF_NAME, null);
        if(mUserName == null)
            chooseAccount();
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
        */
        mApiKey = getContext().getString(R.string.sift_api_key);
        mApiSecret = getContext().getString(R.string.sift_api_secret);
        mSignatory = new Signatory(mApiSecret);
        mUserName = "whatever";
      //  addUser(mUserName,"en_US");

        while(!mIsConnected) {
            try {
                Thread.sleep(Globals.SiftConfig.checkSiftConnectionPollingInterval);
            } catch (Exception e) {
                Logging.error("Connection Exception");
            } finally {
                checkEmailConnection(mUserName);
            }
        }
    }

    /**
    Add a username to developer's sift account.
    @params: username: A name specified by developer. It can be everything.
    @params: locale: user's locale. e.g: "en_US"
     */

    private void addUser(String username, String locale){
        Logging.error("addUser starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users";
        String method = "POST";
        params.put("username", username);
        params.put("locale", locale);
        params = addCommonParams(method, path, params);
        String requestUrl = generateUrl(path, params);
        new WebRequests().execute(requestUrl, method, STATUS_ADD_USER);
    }

    /**
    Get a connect token of the username
    @params: username: The username specified by developer
     */

    private void getConnectToken(String username){
        Logging.error("getConnectToken starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_token";
        String method = "POST";
        params.put("username", username);
        params = addCommonParams(method,path, params);
        String requestUrl = generateUrl(path, params);
        new WebRequests().execute(requestUrl,method,STATUS_CONNECT_TOKEN);
    }

    /*
    Let the user confirm to log in.
    Will pop up the browser to load the url
     @parameters: username: The username specified by developer
     @parameters: token: The token got from function getConnectToken(String)
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
     * @param offset The number of sift information will be ignored from the beginning
     * @param lastUpdateTime Specify the Date from which sifts information begins
     * @param domain Specify what kind of information you want to get
     */
    private void listSifts(String username, Integer offset, Date lastUpdateTime, String domain){
        Logging.error("list sifts start");
        HashMap<String, Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/sifts";
        String method = "GET";
        params.put("username",username);
        if(offset!=null)
            params.put("offset",offset);
        if(lastUpdateTime != null){
            params.put("last_update_time", lastUpdateTime.getTime() / 1000);
        }
        if(domain!=null){
            params.put("domains",domain);
        }
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,STATUS_LIST_SIFTS);
    }

    /**
     * Check whether the user's email is connected.
     * @param username
     */
    private void checkEmailConnection(String username){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username +"/email_connections";
        String method = "GET";
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl, method, STATUS_CHECK_CONNECTION);
    }


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

    private HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,Object> params){
        params.put("api_key", mApiKey);
        params.put("timestamp", System.currentTimeMillis() / 1000L);
        params.put("signature", mSignatory.generateSignature(method, path, params));
        return params;
    }

    private class WebRequests extends AsyncTask<String,Void,String> {

        private String getResponseText(InputStream in) {
            return new Scanner(in).useDelimiter("\\A").next();
        }

        @Override
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