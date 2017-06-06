package com.github.privacystreams.email;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;;
import android.util.Log;
import com.github.privacystreams.utils.ConnectionUtils;

/*
 * This is the Activity for the GmailUpdatesProvider which would mainly be in charge of account choose UI action
 */

public class GmailUpdatesActivity extends Activity {
    private static final String TAG = "GmailUpdatesActivity ";
    static final int REQUEST_ACCOUNT_PICKER = 1000;
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
            if (GmailUpdatesProvider.mCredential.getSelectedAccountName() == null) {
            chooseAccount();
            }
            if (! ConnectionUtils.isDeviceOnline(this)) {
                Log.e(TAG,"No network connection available.");
            }
            if(ConnectionUtils.isGooglePlayServicesAvailable(this)&&GmailUpdatesProvider.mCredential.getSelectedAccountName() != null&&ConnectionUtils.isDeviceOnline(this)){
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
                        GmailUpdatesProvider.mCredential.setSelectedAccountName(accountName);
                    }
                }
                break;
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
            GmailUpdatesProvider.mCredential.setSelectedAccountName(accountName);
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    GmailUpdatesProvider.mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
        }
    }
}
