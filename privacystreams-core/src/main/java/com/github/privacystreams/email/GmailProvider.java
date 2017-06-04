package com.github.privacystreams.email;

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

import java.util.List;

//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by lenovo on 2017/5/22.
 */

public class GmailProvider extends MStreamProvider implements GmailResultListener{
    private final String TAG = "GMAIL";
    static final int START_STATE = 1;
    private static List<String> messageList;
    GmailProvider(){
        this.addRequiredPermissions(Manifest.permission.INTERNET,Manifest.permission.GET_ACCOUNTS,Manifest.permission.ACCESS_NETWORK_STATE);
    }
    @Override
    protected void provide() {
        getGmailInfo();
    }
    private void getGmailInfo(){
        Log.e("Gmail","Starting Looking for gmails");
        messageList = null;
        GmailActivity.setListener(this);
        Intent intent = new Intent(this.getContext(), GmailActivity.class);
        this.getContext().startActivity(intent);
    }
    @Override
    public void onSuccess() {
        Log.e("TAG","Here");
        if(messageList!=null){
            for(String s : messageList){
                this.output(new Email(s,"Gmail",null));
            }
        }
        this.finish();
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail canceled."));
    }

    @Override
    public void setList(List<String> list) {
        messageList = list;
    }
    @Override
    public boolean isEmpty(){
        if(messageList==null) return false;
        return messageList.isEmpty();
    }

}
