package io.github.privacystreams.document;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;

import java.util.Arrays;

import io.github.privacystreams.utils.AppUtils;
import io.github.privacystreams.utils.DeviceUtils;
import io.github.privacystreams.utils.Logging;

import static io.github.privacystreams.document.BaseGoogleDriveProvider.DRIVE_PREF_ACCOUNT_NAME;
import static io.github.privacystreams.document.BaseGoogleDriveProvider.SCOPES;


/**
 * This is the related activity for google drive providers,
 * used for authorization and permission granting.
 */
public class GoogleDriveAuthorizationActivity extends Activity {
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;

    private static GoogleDriveResultListener googleDriveResultListener;
    private GoogleAccountCredential mCredential;
    private Drive mDrive;

    static void setListener(GoogleDriveResultListener gl) {
        googleDriveResultListener = gl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleDriveResultListener != null) {
            if (!DeviceUtils.isDeviceOnline(this)) {
                Logging.warn("No network connection available.");
            }
            if (!DeviceUtils.isGooglePlayServicesAvailable(this)) {
                DeviceUtils.acquireGooglePlayServices(this);
            }
            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            if (mCredential.getSelectedAccountName() == null) {
                chooseAccount();
            }
            if (getIntent().getAction() != null) {
                if (getIntent().getAction().equalsIgnoreCase("UserRecoverableAuthIOException"))
                    startActivityForResult((Intent) getIntent().
                                    getExtras().get("request_authorization"),
                            REQUEST_AUTHORIZATION);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    googleDriveResultListener.onSuccess(mDrive);
                }
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Logging.error("accountName is: " + accountName);
                    if (accountName != null) {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(this).edit();
                        editor.putString(DRIVE_PREF_ACCOUNT_NAME, accountName);
                        editor.apply();

                        mCredential.setSelectedAccountName(accountName);

                        mDrive = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport()
                                , JacksonFactory.getDefaultInstance(), mCredential)
                                .setApplicationName(AppUtils.getApplicationName(this))
                                .build();
                        googleDriveResultListener.onSuccess(mDrive);
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
                .getString(DRIVE_PREF_ACCOUNT_NAME, null);
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName);
        } else {
            // Start a dialog from which the user can choose an account
            try {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            } catch (ActivityNotFoundException e) {
                Logging.error("no activity found for choosing account");
            }
        }
    }
}
