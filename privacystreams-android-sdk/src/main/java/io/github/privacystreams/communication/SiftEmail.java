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

    SiftEmail(String key, String secret){
        if(key != null) {
            this.api_key = key;
            this.api_secret = secret;
            signatory = new Signatory(api_secret);
        }
    }

    @Override
    public void provide(){
        testSelf();
    }

    /* just for test when debug*/
    private void testSelf(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);

        Logging.error("start testself");
        this.api_key = "feb6159c2f04b44cf03b6d58fe10177c";
        this.api_secret = "82aea7a170e5eb9648f77ead8c28f81a85f84dc3";
        signatory = new Signatory(api_secret);
        addUser("whatever","en_US");
        while(user_id == 0);
        Logging.error("addUser returns:"+user_id);

        getConnectToken("whatever");
        while(connectToken == null);
        Logging.error("getConnectToken returns: "+connectToken);

        Logging.error("start to connect Email");
        connectEmail("whatever",connectToken,null);

    }

    public long addUser(String username, String locale){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users";
        String method = "POST";
        params.put("username", username);
        params.put("locale", locale);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,USERID);
        return user_id;
    }

    public void deleteUser(String username){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username;
        String method = "DELETE";
        params.put("username", username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);

    }

    public long addGmailConnection(String username, String account, String refreshToken){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/email_connections";
        String method = "POST";
        params.put("username", username);
        params.put("account_type","google");
        params.put("account",account);
        params.put("refreshToken",refreshToken);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        return 1;
    }

    public void deleteEmailConnection(String username, long id){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/email_connections/"+id;
        String method = "DELETE";
        params.put("username", username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
    }

    public void listConnections(String username){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/email_connections";
        String method = "GET";
        params.put("username", username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
    }

    public String getConnectToken(String username){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_token";
        String method = "POST";
        params.put("username", username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,TOKEN);
        return connectToken;
    }

    public void connectEmail(String username, String token, String redirectUrl){
        HashMap<String,Object> params = new HashMap<>();
        String path = "/v1/connect_email";
        String method = "GET";
        params.put("api_key", api_key);
        params.put("username", username);
        params.put("token", token);
        if(redirectUrl!=null){
            params.put("redirect_url", redirectUrl);
        }
        requestUrl = generateUrl(path,params);
        Intent intent = new Intent(getContext(),WebActivity.class);
        intent.putExtra("url",requestUrl);
        try {
            getContext().startActivity(intent);
        }catch(Exception e){
            Logging.error("activity error:"+e.getMessage());
        }
    }

    public void listSifts(String username){
        HashMap<String, Object> params = new HashMap<>();
        String path = "/v1/users/"+username+"/sifts";
        String method = "GET";
        params.put("username",username);
        params = addCommonParams(method,path,params);
        requestUrl = generateUrl(path,params);
        new WebRequests().execute(requestUrl,method,LISTSIFTS);
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

    class WebRequests extends AsyncTask<String,Void,Void> {
        private String getResponseText(InputStream in) {
            return new Scanner(in).useDelimiter("\\A").next();
        }

        @Override
        protected Void doInBackground(String... params) {
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
            return null;
        }
    }
}