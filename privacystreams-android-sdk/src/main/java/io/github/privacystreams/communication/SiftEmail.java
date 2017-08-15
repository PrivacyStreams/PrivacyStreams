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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    //private HttpResponse httpResponse;
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
   // private static final String username = "dummy_username";
    private static final String resource = "/v1/connect_token";
    private static final String url = "https://api.edison.tech" + resource;
    private ApiManager apiManager;
    public SiftEmail(String accountName,String token){
        this.userName = accountName;
        this.userToken = token;
        //requests = baseUrl + "/v1/connect_email?api_key=<"+DEVELOPER_API_KEY+">&username=<"+userName+">&token="+userToken+">";
        Logging.error("init");
        apiManager = new ApiManager(DEVELOPER_API_KEY,DEVELOPER_API_SECRET);
        Logging.error("apiManager init success");
    }

    /** the main function to connect with sift*/
    public void main() throws Exception{
        String name = "whatever";
       // String request1 = baseUrl + "/v1/user";//add a user
        apiManager.addUser(name,"en_US");
        Logging.error("addUser success");

        long userId = apiManager.addGmailConnection(name,userName,userToken);
        Logging.error("userId is:"+userId);
        Logging.error("connections:"+apiManager.listConnections(name));
        List list = apiManager.listSifts(name);
        for(Object obj : list){
            Logging.error("sift result:" + obj.toString());
        }
        /*
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("api_key", DEVELOPER_API_KEY);
        params.put("timestamp", System.currentTimeMillis() / 1000L);
        params.put("username", name);
        Logging.error("start to generate sig");
        String signature = generateSignature("POST", resource, params);
        Logging.error("signature is:"+signature);
        params.put("signature", signature);
        */
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
            return signature;
        } catch (Exception e) {
            Logging.error("generate sig failed:"+e.getMessage());
            return "";
        }
    }



}


