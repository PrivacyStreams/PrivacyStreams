package com.github.privacystreams.email;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.github.privacystreams.utils.ConnectionUtils;

;

/**
 * Created by lenovo on 2017/6/4.
 */

public class GmailActivity extends Activity {
    private static final String TAG = "GmailActivity - ";

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
            if (! ConnectionUtils.isGooglePlayServicesAvailable(this)) {
                ConnectionUtils.acquireGooglePlayServices(this);
            }
            if (GmailProvider.mCredential.getSelectedAccountName() == null) {
                chooseAccount();
            }
            if (! ConnectionUtils.isDeviceOnline(this)) {
                Log.e(TAG,"No network connection available.");
            }
            if(ConnectionUtils.isGooglePlayServicesAvailable(this)
                    && GmailProvider.mCredential.getSelectedAccountName() != null
                    && ConnectionUtils.isDeviceOnline(this)){
                gmailResultListener.onSuccess();
            }
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_CANCELED, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//                if (resultCode != RESULT_OK) {
//                    Log.e(TAG,"This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app.");
//                }
//                break;
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
                        GmailProvider.mCredential.setSelectedAccountName(accountName);
                    }
                }
                break;
//            case REQUEST_AUTHORIZATION:
//                if (resultCode == RESULT_OK) {
//                }
//                break;
//            case UPLOAD_INFORMATION:
//                if(resultCode==RESULT_OK)
//                gmailResultListener.onSuccess();
//                break;
//            case RESULT_CANCELED:
//                Log.e(TAG,"cancelled");
//                gmailResultListener.onFail();
//                break;
        }
        finish();
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
            GmailProvider.mCredential.setSelectedAccountName(accountName);
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    GmailProvider.mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
        }
    }
}
