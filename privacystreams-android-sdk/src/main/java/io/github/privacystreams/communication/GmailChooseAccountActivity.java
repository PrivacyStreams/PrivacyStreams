package io.github.privacystreams.communication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;

import java.util.Arrays;

import io.github.privacystreams.utils.DeviceUtils;
import io.github.privacystreams.utils.Logging;



/**
 * Created by xiaobing1117 on 2017/8/24.
 */

public class GmailChooseAccountActivity extends Activity {
    private GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final String GMAIL_PREF_ACCOUNT_NAME = "userName";
    static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    private static EmailAccountNameListener mListener;

    protected static void setListener(EmailAccountNameListener listener){ mListener = listener;}

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (! DeviceUtils.isDeviceOnline(this)) {
            Logging.warn("No network connection available.");
            finish();
        }
        if (! DeviceUtils.isGooglePlayServicesAvailable(this)) {
            DeviceUtils.acquireGooglePlayServices(this);
        }
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        String userName;
        if ((userName = mCredential.getSelectedAccountName()) == null) {
            chooseAccount();
        }else{
            userName = userName.split("@")[0];
            mListener.onSuccess(userName);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_AUTHORIZATION:
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Logging.error("get accountName:"+accountName);

                    String userName = accountName.split("@")[0];
                    mListener.onSuccess(userName);
                }

                break;
        }
        finish();
    }

    private void chooseAccount() {
        String accountName = getPreferences(Context.MODE_PRIVATE)
                .getString(GMAIL_PREF_ACCOUNT_NAME, null);
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName);
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
        }
    }
}
