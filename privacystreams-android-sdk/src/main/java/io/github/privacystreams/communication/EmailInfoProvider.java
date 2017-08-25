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

import io.github.privacystreams.communication.emailinfo.Deal;
import io.github.privacystreams.communication.emailinfo.Flight;
import io.github.privacystreams.communication.emailinfo.Order;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.R;
import io.github.privacystreams.utils.Logging;


public class EmailInfoProvider extends PStreamProvider implements EmailAccountNameListener{

    /*for sifts info*/
    private String mApiKey;
    private String mApiSecret;
    private final String mRequestDomain = "https://api.edison.tech";

    /*for signature generation*/
    private Signatory mSignatory;

    /*for addUser*/
    private final String mStatusUserId = "USER_ID";

    /* for listSifts*/
    private final String mStatusListSifts = "LIST_SIFTS";

    /*for connectToken*/
    private final String mStatusConnectToken = "TOKEN";
    private String mConnectToken = null;

    /*for list email*/
    private final String mStatusISConnected = "IS_CONNECTED";

    /*for list sifts*/
    private String mUserName = null;

    private final String mGmailPrefName = "userName";
    private final String mToken = "connectToken";

    private boolean mIsConnected = false;

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private String mDomain = null;
    /*
    @params: api_key: the api_key generated on developer's sift account
    @params: api_secret: the api_secret generated on developer's sift account
     */
    public EmailInfoProvider(String key, String secret, String domain){
        if(key != null) {
            this.mApiKey = key;
            this.mApiSecret = secret;
            mSignatory = new Signatory(mApiKey);
            this.mDomain = domain;
        }
    }

    @Override
    public void onFail() {

    }
    @Override
    public void onSuccess(String name) {
        mUserName = name;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(mGmailPrefName, name);
        editor.apply();
        addUser(name,"en_US");
    }

    @Override
    protected void provide() {
        start();
    }

    /* just for test when debug*/
    //TODO Change this function when debug ends
    protected void start(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
        Logging.error("start");
        this.mApiKey = getContext().getString(R.string.sift_api_key);
        this.mApiSecret = getContext().getString(R.string.sift_api_secret);
        mSignatory = new Signatory(mApiSecret);
        GmailChooseAccountActivity.setListener(this);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(mToken, null);
        if(token != null) {
            Logging.error("needn't get token");
            listSifts(mUserName,null,null,mDomain);
            return;
        }
        mUserName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(mGmailPrefName, null);
        if(mUserName == null)
            chooseAccount();
        else{
            Logging.error("needn't sign in");
            addUser(mUserName,"en_US");
        }
        while(!mIsConnected){
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }finally{
                isEmailConnected(mUserName);
            }
        }
        listSifts(mUserName,null,null,mDomain);

    }

    /*
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
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,mStatusUserId);
    }

    /*
    Get a connect token of the username
    @params: username: The username specified by developer
     */

    private void getConnectToken(String username){
        Logging.error("getConnectToken starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_token";
        String method = "POST";
        params.put("username", username);
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,mStatusConnectToken);
    }

    /*
    Let the user confirm to log in.
    Will call the browser
     @parameters: username: The username specified by developer
     @parameters: token: The token got from function getConnectToken(String)
    */
    private void connectEmail(String userName, String token){
        Logging.error("connectEmail starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_email";
        String method = "GET";
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

    /*
    List sift information
    @params: username: The username specified by developer
    @params: offset: The number of sift information will be ignored from the beginning
    @params: lastUpdateTime: Specify the Date from which sifts information begins
    @params: domain: Specify what kind of information you want to get
     */

    private String listSifts(String username, Integer offset, Date lastUpdateTime, String domain){
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
        new WebRequests().execute(requestUrl,method,mStatusListSifts);
        return null;
    }


    private void isEmailConnected(String username){
        Logging.error("checkisEmailConneected");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username +"/email_connections";
        String method = "GET";
      //  params.put("username", username);
        params = addCommonParams(method,path,params);
        String requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,mStatusISConnected);
    }


    private String generateUrl(String path, HashMap<String,Object> params){
        String base = mRequestDomain;
        base += path + "?";
        List<String> keys = new ArrayList<>(params.keySet());
        boolean notFirst = false;
        for(String key : keys){
            if(notFirst){
                base += "&";
            }
            else{
                notFirst = true;
            }
            base += key + "=" + params.get(key);
        }
        return base;
    }

    private HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,Object> params){
        params.put("api_key", mApiKey);
        params.put("timestamp", System.currentTimeMillis() / 1000L);
        params.put("signature", mSignatory.generateSignature(method, path, params));
        return params;
    }



    private void chooseAccount(){
        if(mUserName == null) {
            Intent intent = new Intent(getContext(), GmailChooseAccountActivity.class);
            getContext().startActivity(intent);
        }
    }

    class WebRequests extends AsyncTask<String,Void,String> {

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
            String returnValue = params[2];    // the called function
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
                    Logging.error("response String info is:" + responseString);
                } else {
                    Logging.error("request error with code:" + statusCode);
                }
            } catch (Exception e) {
                Logging.error("url conn error:" + e.getMessage());
            } finally {
                if (urlConnection!=null) {
                    urlConnection.disconnect();
                }
            }
            switch (returnValue) {
                case mStatusListSifts:
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
                            switch(type){
                                case "Contact":
                                    io.github.privacystreams.communication.emailinfo.Contact contact = (io.github.privacystreams.communication.emailinfo.Contact) mObjectMapper.treeToValue(payload, Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("contact",contact.getContacts().get(0).getEmail());
                                case "Unknown":
                                    break;
                                case "Order":
                                    Logging.error("cast to order");
                                    Order order = (Order) mObjectMapper.treeToValue(payload, Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("order",order.getOrderNumber());
                                    break;
                                case "Deal":
                                    Logging.error("cast to deal");
                                    Deal deal = (Deal) mObjectMapper.treeToValue(payload,
                                            Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("deal",deal.toString());
                                    break;
                                case "Flight":
                                    Logging.error("cast to flight");
                                    Flight flight = (Flight) mObjectMapper.treeToValue(payload,
                                            Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("flight",flight.toString());
                                    break;
                                default:
                                    Logging.error("unknown type");

                            }
                        }

                    } catch (Exception e) {
                        Logging.error("parse json failed for list sifts");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;
                case mStatusUserId:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                    } catch (Exception e) {
                        Logging.error("parse json failed for user id");
                        Logging.error("exception is" + e.getMessage());

                    }
                    break;
                case mStatusConnectToken:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                        mConnectToken = responseJson.getJSONObject("result").get("connect_token").toString();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString(mToken, mConnectToken);
                        editor.apply();
                    } catch (Exception e) {
                        Logging.error("parse json failed for connect token");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;
                case mStatusISConnected:
                    try {
                        JsonNode root = mObjectMapper.readTree(responseString);
                        Logging.error("json is:" + root);
                        JsonNode temp = root.get("result").get(0);
                        if(temp != null){
                            mIsConnected = true;
                        }
                    } catch (Exception e) {
                        Logging.error("parse json failed for user id");
                        Logging.error("exception is" + e.getMessage());
                    }
                default:

            }
            return returnValue;
        }

        @Override
        protected  void onPostExecute(String lastStatus){
            Logging.error("last step is: "+ lastStatus);
            switch(lastStatus){
                case mStatusUserId:
                    getConnectToken(mUserName);
                    break;
                case mStatusConnectToken:
                    connectEmail(mUserName,mConnectToken);
                    break;
                case mStatusListSifts:
                    break;
                case mStatusISConnected:
                    break;
                default:
                    Logging.error("something strange happened");
            }
        }
    }
}