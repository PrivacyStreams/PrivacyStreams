package io.github.privacystreams.communication;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Logging;

import com.easilydo.sift.model.Sift;
import com.easilydo.sift.model.Domain;
import com.easilydo.sift.model.gen.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SiftEmail extends PStreamProvider{

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

    /*for list sifts*/
    private static String siftinfo = null;

    private static String userName = null;
    /*

     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /*
    @params: api_key: the api_key generated on developer's sift account
    @params: api_secret: the api_secret generated on developer's sift account
     */
    SiftEmail(String key, String secret){
        if(key != null) {
            this.api_key = key;
            this.api_secret = secret;
            signatory = new Signatory(api_secret);
        }
    }

    @Override
    public void provide() {
        testSelf();

    }

    public static void setUserName(String name){
        Logging.error("now userName is:"+name);
        userName = name;
    }

    /* just for test when debug*/
    private void testSelf(){

        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
        Logging.error("signin");
        signIn();
        while(userName == null);
        Logging.error("start testself");
        this.api_key = "15b6a990b4599c7f6b3deb95cd05307b";
        this.api_secret = "2bc65281868a4a2ce6c83931cd91497f5deabc80";
        signatory = new Signatory(api_secret);
        addUser(userName,"en_US");
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
        Intent intent = new Intent(getContext(),WebActivity.class);
        intent.putExtra("url",requestUrl);
        intent.putExtra("userName",userName);
        try {
            getContext().startActivity(intent);
        }catch(Exception e){
            Logging.error("activity error:"+e.getMessage());
        }
    }


    /*
    List sift information
    @params: username: The username specified by developer
     */
    public String listSifts(String username){
        return listSifts(username,null,null);
    }

    /*
    List sift information
    @params: username: The username specified by developer
    @params: lastUpdateTime: Specify the Date from which sifts information begins
     */

    public String listSifts(String username, Date lastUpdateTime){
        return listSifts(username,null,lastUpdateTime);
    }

    /*
    List sift information
    @params: username: The username specified by developer
    @params: offset: The number of sift information will be ignored from the beginning
     */

    protected String listSifts(String username, int offset){
        return listSifts(username,offset,null);
    }

    /*
    List sift information
    @params: username: The username specified by developer
    @params: offset: The number of sift information will be ignored from the beginning
    @params: lastUpdateTime: Specify the Date from which sifts information begins
     */

    protected String listSifts(String username, Integer offset, Date lastUpdateTime){
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
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,LISTSIFTS);
        return null;
    }


    private String generateUrl(String path, HashMap<String,Object> params){
        String base = new String(domain);
        base += path + "?";
        List<String> keys = new ArrayList<String>(params.keySet());
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
        params.put("api_key", api_key);
        params.put("timestamp", getCurrentTime());
        params.put("signature", signatory.generateSignature(method, path, params));
        return params;
    }

    private long getCurrentTime(){
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
                        Logging.error("root is:"+root);
                        JsonNode result = root.get("result").get(0);
                        Logging.error("result is:" + result);
                        JsonNode payload = result.has("@type") ? result : result.get("payload");
                        Logging.error("payload is"+payload);
                        String type = payload.get("@type").textValue();
                        if(type.startsWith("x-")) {
                            type = type.substring(2);
                            Logging.error("type is:"+type);
                        }
                        else{
                            Logging.error("type is:"+type);
                        }
                        if(type != null){
                            switch(type){
                                case "Unknown":
                                    break;
                                case "Order":
                                    Logging.error("cast to order");
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "Deal":
                                    Logging.error("cast to deal");
                                    Deal deal = (Deal) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "Contact":
                                    Logging.error("cast to Contact");
                                    Contact contact = (Contact) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                default:
                                    Logging.error("unknown type");
                                    /*
                                case "ParcelDelivery":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                case "order":
                                    Order order = (Order) objectMapper.treeToValue(payload, Class.forName("com.easilydo.sift.model.gen." + type));
                                    break;
                                */
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
                    } catch (Exception e) {
                        Logging.error("parse json failed for connect token");
                        Logging.error("exception is" + e.getMessage());
                    }
                    break;
                default:

            }
            return returnValue;
        }

        @Override
        protected  void onPostExecute(String lastProcess){
            Logging.error("last step is: "+ lastProcess);
            switch(lastProcess){
                case USERID:
                    getConnectToken(userName);
                    break;
                case TOKEN:
                    connectEmail(userName,connectToken);
                    break;
                case LISTSIFTS:
                    break;
                default:
                    Logging.error("something strange happened");
            }
        }
    }
}