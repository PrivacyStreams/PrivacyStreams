package io.github.privacystreams.communication;




import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.easilydo.sift.api.ApiManager;
import com.easilydo.sift.model.*;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.github.privacystreams.utils.Logging;

import java.util.*;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mashape.unirest.http.Unirest.post;

/**
 * Created by xiaobing1117 on 2017/8/14.
 */

public class SiftEmail  {
    private static final String DEVELOPER_API_KEY = "e6b0dc00388a72b4d39043fa447660f2";
    private static final String DEVELOPER_API_SECRET = "8705f9438b3c1c59522afbc430189c57329418a3";
    private static String userToken = "";
    private static String userName = "";
    private static final String baseUrl = "https://api.edison.tech";
    private static String requests = "";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final String givenName = "whatever";
    public SiftEmail(String accountName,String token){
        this.userName = accountName;
        this.userToken = token;
    }

    /** the main function to connect with sift*/
    public void main() throws Exception{

        HashMap<String,Object> param = new HashMap<>();
        String addUser = "/v1/users";
        String getToken = "/v1/connect_token";
        param.put("api_key",DEVELOPER_API_KEY);
        param.put("timestamp",System.currentTimeMillis() / 1000L);
        param.put("username","whatever");
        String requestMethod = "POST";
        String signature = generateSignature(requestMethod,addUser,param);
        param.put("signature",signature);
        try{
            MultipartBody request = Unirest.post(baseUrl+addUser).header("content-type", "x-www-form-urlencoded").queryString(param).field("locale","en_US");
            Logging.error("unirest:"+request.getEntity());
            /*
            if(request.getBody()!=null)
            Logging.error("unirest response1 is"+request.getBody());
            else
            Logging.error("uni req1 return null");
            param.clear();
            param.put("api_key",DEVELOPER_API_KEY);
            param.put("username","whatever");
            param.put("timestamp",System.currentTimeMillis() / 1000L);
            signature = generateSignature(requestMethod,getToken,param);
            param.put("signature",signature);
            request = Unirest.post(baseUrl+getToken).header("content-type", "x-www-form-urlencoded")
                    .queryString(param);
            if(request.getBody()!=null)
            Logging.error("unirest response2 is"+request.getBody().toString());
            else
            Logging.error("unin req 2 return null");
            String conn = "/v1/connect_email";
            param.clear();
            param.put("api_key",DEVELOPER_API_KEY);
            param.put("username","whatever");
            param.put("timestamp",System.currentTimeMillis() / 1000L);
            signature = generateSignature(requestMethod,conn,param);
            param.put("signature",signature);
            request = Unirest.post(baseUrl+conn).header("content-type", "x-www-form-urlencoded")
                    .queryString(param);
            Logging.error("unirest response3 is"+request.getBody());
            */
        }catch (Exception e){
            Logging.error("unirest error:"+e.getMessage());
        }finally {


            RequestWebTask rwt = new RequestWebTask();
            rwt.doInBackground();
        }
    }

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2]     = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars).toLowerCase();
    }

    public static String signData(String data, String key)
            throws java.security.SignatureException
    {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = bytesToHex(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }

        return result;
    }

    public static String generateSignature(String httpMethod, String resource, HashMap<String, Object> params)
    {
        String baseString = "";
        baseString += httpMethod.toUpperCase();
        baseString += "&" + resource;

        ArrayList<String> list = new ArrayList<String>();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }

        Collections.sort(list);
        it = list.iterator();
        while(it.hasNext()) {
            String key = it.next();
            baseString += "&" + key + "=" + params.get(key);
        }

        try {
            String signature = signData(baseString, DEVELOPER_API_SECRET);
            Logging.error("signature is:"+signature);
            return signature;
        } catch (Exception e) {
            Logging.error("generate sig failed:"+e.getMessage());
            return "";
        }
    }

     class RequestWebTask extends AsyncTask<Map<String,Object>,String,JSONArray> {

        private String getResponseText(InputStream in){
            return new Scanner(in).useDelimiter("\\A").next();
        }
        @Override
        //func get JSON Respond
        //Do in Background
        //Get authorization
        //Input URLs you want to get or post in String
        //Output: JSONObject(func Flight_JSON_To_FlightContent in edu.cmu.chimps.messageontap.utility.GenericUtils will help to change that into String)
        protected JSONArray doInBackground(Map<String,Object>... params) {
            /*init*/
            //userName = userName.replace("@","%40");

            HashMap<String,Object> param = new HashMap<>();
            JSONObject jsonObject = null;
            HttpURLConnection urlconnection = null;
            java.net.URL urlToRequest;
            try {
                /*first step: add a user
         
                 */
                Logging.error("start first step");
                String url = baseUrl+"/v1/users?";
                param.put("api_key",DEVELOPER_API_KEY);
                param.put("username",givenName);
                long tm = System.currentTimeMillis() / 1000L;
                param.put("timestamp",tm);
                String requestMethod = "POST";
                String signature = generateSignature(requestMethod,"/v1/users",param);
                url += "api_key="+DEVELOPER_API_KEY
                        +"&username="+givenName
                        +"&timestamp="+tm;
                Logging.error("url is: "+url);
                url += "&signature="+ signature;
                Logging.error("step 1 url:"+url);
                urlToRequest = new URL(url);
                urlconnection = (HttpURLConnection)urlToRequest.openConnection();
                urlconnection.setRequestMethod("POST");
                int statusCode = urlconnection.getResponseCode();
                if(statusCode == 200) {
                    InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                    Logging.error("step 1: add user returns:"+getResponseText(in));
                }else{
                    Logging.error("step 1 failed with code:"+statusCode);
                }

                /*second step: add a email_connection*/
                Logging.error("step 2 start");
                tm = System.currentTimeMillis() / 1000L;
                url = baseUrl + "/v1/users/"+givenName+"/email_connections?"+"api_key="+DEVELOPER_API_KEY
                        +"&timestamp="+tm
                        +"&account_type=google"
                        +"&account="+userName
                        +"&refresh_token="+userToken;
                param.clear();
                param.put("api_key",DEVELOPER_API_KEY);
                param.put("timestamp",tm);
                param.put("account_type","google");
                param.put("account",userName);
                param.put("refresh_token",userToken);
                requestMethod = "POST";
                signature = generateSignature(requestMethod,"/v1/users/"+givenName+"/email_connections",param);
                url += "&signature=" + signature;
                Logging.error("step 2 url:"+url);
                urlToRequest = new URL(url);
                urlconnection = (HttpURLConnection)urlToRequest.openConnection();
                urlconnection.setRequestMethod("POST");
                statusCode = urlconnection.getResponseCode();
                if(statusCode == 200) {
                    InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                    Logging.error("step 2: add email connection returns :"+getResponseText(in));
                }
                else{
                    Logging.error("step 2 failed with code:"+statusCode);
                }
                /*third step: get token*/
                Logging.error("step 3 starts");
                long t = System.currentTimeMillis() / 1000L;
                url = baseUrl + "/v1/connect_token?"+"api_key="+DEVELOPER_API_KEY
                        +"&username="+givenName
                        +"&timestamp="+t;
                param.clear();
                param.put("api_key",DEVELOPER_API_KEY);
                param.put("username",givenName);
                param.put("timestamp",t);
                requestMethod = "POST";
                signature = generateSignature(requestMethod,"/v1/connect_token",param);
                url += "&signature=" + signature;
                urlToRequest = new URL(url);
                urlconnection = (HttpURLConnection)urlToRequest.openConnection();
                urlconnection.setRequestMethod("POST");
                statusCode = urlconnection.getResponseCode();
                if(statusCode == 200) {
                    InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                    jsonObject = new JSONObject(getResponseText(in));
                    String token = jsonObject.getJSONObject("result").toString().split(":")[1].replace("\"","").replace("}","");
                    Logging.error("step 3 get token = " + token);

                    /*fourth step: connect_email*/
                    Logging.error("step 4 starts");
                    long time = System.currentTimeMillis() / 1000L;
                    url = "https://api.edison.tech/v1/connect_email?"
                            +"api_key="+DEVELOPER_API_KEY
                            +"&username="+givenName
                            +"&token="+token
                            +"&redirect_url="+"www.google.com";
                    param.clear();
                    param.put("api_key",DEVELOPER_API_KEY);
                    param.put("username",givenName);
                    param.put("token",token);
                    param.put("redirect_url","www.google.com");
                    //signature = generateSignature("GET","/v1/connect_email",param);
                    //url += "&signature=" + signature;
                    urlToRequest = new URL(url);
                    urlconnection = (HttpURLConnection)urlToRequest.openConnection();
                    urlconnection.setRequestMethod("GET");
                    statusCode = urlconnection.getResponseCode();
                    if(statusCode == 200) {
                        in = new BufferedInputStream(urlconnection.getInputStream());
                        String info = getResponseText(in);
                        Logging.error("Step 4 get connect email returns:"+info);

                        /*last step: list sifts*/
                        Logging.error("last step starts");
                        param.clear();
                        time = System.currentTimeMillis() / 1000L;
                        url = "https://api.edison.tech/v1/users/"+givenName+"/sifts?"
                                +"api_key="+DEVELOPER_API_KEY
                                +"&username="+givenName
                                +"&timestamp="+time;
                        param.clear();
                        param.put("api_key",DEVELOPER_API_KEY);
                        param.put("username",givenName);
                        param.put("timestamp",time);
                        signature = generateSignature("GET","/v1/users/"+givenName+"/sifts",param);
                        url += "&signature=" + signature;
                        urlToRequest = new URL(url);
                        urlconnection = (HttpURLConnection)urlToRequest.openConnection();
                        urlconnection.setRequestMethod("GET");
                        statusCode = urlconnection.getResponseCode();
                        if(statusCode == 200){
                            in = new BufferedInputStream(urlconnection.getInputStream());
                            jsonObject = new JSONObject(getResponseText(in));
                            Logging.error("step 5:list sifts:"+jsonObject);
                        }
                    }
                }
                if(statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                    //return null;
                }
                else if(statusCode != HttpURLConnection.HTTP_OK){
                   // return null;

                }
                else
                {
                    Logging.error("aaa"+urlconnection.getResponseMessage()+"");
                    InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                    jsonObject = new JSONObject(getResponseText(in));
                    return jsonObject.getJSONArray("result");
                }

            } catch (MalformedURLException e) {
                // URL is invalid
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // data retrieval or connection timed out
                e.printStackTrace();
            } catch (IOException e) {
                // could not read response body
                // (could not create input stream)
                e.printStackTrace();
            } catch (JSONException e) {
                // response body is no valid JSON string
                e.printStackTrace();
            } finally {
                if (urlconnection != null) {
                    urlconnection.disconnect();
                }
            }

            return null;

        }


        //Exit
        //Back to main Thread
        //Input JSONObject
        //Series of operation
        protected void onPostExecute(JSONArray jsonArray_private) {
            //jsonArray = jsonArray_private;

        }
    }

}


