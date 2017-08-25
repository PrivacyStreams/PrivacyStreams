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
import com.google.api.services.gmail.Gmail;

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
import io.github.privacystreams.communication.emailinfo.EmailAccountNameListener;
import io.github.privacystreams.communication.emailinfo.Flight;
import io.github.privacystreams.communication.emailinfo.Order;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.R;
import io.github.privacystreams.utils.Logging;


public class EmailInfoProvider extends PStreamProvider implements EmailAccountNameListener{

    /*for sifts info*/
    private static String api_key;
    private static String api_secret;
    private static final String domain = "https://api.edison.tech";

    /*for signature generation*/
    private static Signatory signatory;

    /*url ro request*/
    private static String requestUrl;

    /*for addUser*/
    private final String USERID = "USERID";
    private static long user_id = 0;

    /* for listSifts*/
    private final String LISTSIFTS = "LISTSIFTS";

    /*for connectToken*/
    private final String TOKEN = "TOKEN";
    private static String connectToken = null;

    /*for list email*/
    private final String ISCONNECTED = "ISCONNECTED";

    /*for list sifts*/
    private static String siftinfo = null;

    protected static String userName = null;

    static final String GMAIL_PREF_ACCOUNT_NAME = "userName";
    static final String CONNECT_TOKEN = "connectToken";

    private boolean isConnected = false;
    /*

     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String domains = null;
    /*
    @params: api_key: the api_key generated on developer's sift account
    @params: api_secret: the api_secret generated on developer's sift account
     */
    public EmailInfoProvider(String key, String secret, String domains){
        if(key != null) {
            this.api_key = key;
            this.api_secret = secret;
            signatory = new Signatory(api_secret);
            this.domains = domains;
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess(String name) {
        userName = name;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(GMAIL_PREF_ACCOUNT_NAME, userName);
        editor.apply();
        addUser(userName,"en_US");
    }

    @Override
    protected void provide() {
        testSelf();

    }

    /* just for test when debug*/
    protected void testSelf(){

        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
        Logging.error("start");
        this.api_key = getContext().getString(R.string.sift_api_key);
        this.api_secret = getContext().getString(R.string.sift_api_secret);
        signatory = new Signatory(api_secret);
        SignInActivity.setListener(this);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(CONNECT_TOKEN, null);
        if(token != null) {
            Logging.error("needn't get token");
            listSifts(userName,null,null,domains);
            return;
        }
        userName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(GMAIL_PREF_ACCOUNT_NAME, null);
        if(userName == null)
            signIn();
        else{
            Logging.error("needn't sign in");
            addUser(userName,"en_US");
        }
        while(!isConnected){
            try{Thread.sleep(500);}catch (Exception e){}
            finally{
                isEmailConnected(userName);}
        }
        listSifts(userName,null,null,domains);
        /*
        userName = "whatever";
        addUser(userName,"en_US");
        */
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
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,USERID);
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
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,TOKEN);
    }

    /*
    Let the user confirm to log in.
    Will call the browser
     @parameters: username: The username specified by developer
     @parameters: token: The token got from function getConnectToken(String)
    */
    private void connectEmail(String username, String token){
        Logging.error("connectEmail starts");
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_email";
        String method = "GET";
        params.put("api_key", api_key);
        params.put("username", username);
        params.put("token", token);
        requestUrl = generateUrl(path,params);
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

    protected String listSifts(String username, Integer offset, Date lastUpdateTime, String domain){
        Logging.error("list sifts start");
        HashMap<String, Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/sifts";
        String method = "GET";
        params.put("username",username);
        if(offset!=null)
            params.put("offset",offset);
        if(lastUpdateTime != null){
            params.put("last_update_time", getEpochSecs(lastUpdateTime));
        }
        if(domain!=null){
            params.put("domains",domain);
        }
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,LISTSIFTS);
        return null;
    }

    protected void isEmailConnected(String username){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username +"/email_connections";
        String method = "GET";
      //  params.put("username", username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,ISCONNECTED);
    }


    private static String generateUrl(String path, HashMap<String,Object> params){
        String base = domain;
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

    private static HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,Object> params){
        params.put("api_key", api_key);
        params.put("timestamp", getCurrentTime());
        params.put("signature", signatory.generateSignature(method, path, params));
        return params;
    }

    private static long getCurrentTime(){
        return System.currentTimeMillis() / 1000L;
    }

    protected static long getEpochSecs(Date date) {
        return date.getTime() / 1000;
    }

    private void signIn(){
        if(userName == null) {
            Intent intent = new Intent(getContext(), SignInActivity.class);
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
            HttpURLConnection urlconnection = null;
            JSONObject responseJson;
            try {
                URL urlToRequest = new URL(url);
                urlconnection = (HttpURLConnection) urlToRequest.openConnection();
                urlconnection.setRequestMethod(method);
                int statusCode = urlconnection.getResponseCode();
                if (statusCode==200) {
                    InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                    responseString = getResponseText(in);
                    Logging.error("response String info is:" + responseString);
                } else {
                    Logging.error("request error with code:" + statusCode);
                }
            } catch (Exception e) {
                Logging.error("url conn error:" + e.getMessage());
            } finally {
                if (urlconnection!=null) {
                    urlconnection.disconnect();
                }
            }
            switch (returnValue) {
                case LISTSIFTS:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                        siftinfo = responseJson.get("result").toString();
                        JsonNode root = objectMapper.readTree(responseString);

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
                                    io.github.privacystreams.communication.emailinfo.Contact contact = (io.github.privacystreams.communication.emailinfo.Contact) objectMapper.treeToValue(payload, Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("contact",contact.getContacts().get(0).getEmail());
                                case "Unknown":
                                    break;
                                case "Order":
                                    Logging.error("cast to order");
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("order",order.getOrderNumber());
                                    break;
                                case "Deal":
                                    Logging.error("cast to deal");
                                    Deal deal = (Deal) objectMapper.treeToValue(payload,
                                            Class.forName("io.github.privacystreams.communication.emailinfo." + type));
                                    Log.e("deal",deal.toString());
                                    break;
                                case "Flight":
                                    Logging.error("cast to flight");
                                    Flight flight = (Flight) objectMapper.treeToValue(payload,
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
                case USERID:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                        user_id = Long.parseLong(responseJson.getJSONObject("result").get("user_id").toString());
                    } catch (Exception e) {
                        Logging.error("parse json failed for user id");
                        Logging.error("exception is" + e.getMessage());

                    }
                    break;
                case TOKEN:
                    try {
                        responseJson = new JSONObject(responseString);
                        Logging.error("json is:" + responseJson);
                        connectToken = responseJson.getJSONObject("result").get("connect_token").toString();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString(CONNECT_TOKEN, connectToken);
                        editor.apply();
                    } catch (Exception e) {
                        Logging.error("parse json failed for connect token");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;
                case ISCONNECTED:
                    try {
                        JsonNode root = objectMapper.readTree(responseString);
                        Logging.error("json is:" + root);
                        JsonNode temp = root.get("result").get(0);
                        if(temp != null){
                            isConnected = true;
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
                case USERID:
                    getConnectToken(userName);
                    break;
                case TOKEN:
                    connectEmail(userName,connectToken);
                    break;
                case LISTSIFTS:
                    break;
                case ISCONNECTED:
                    break;
                default:
                    Logging.error("something strange happened");
            }
        }
    }
}