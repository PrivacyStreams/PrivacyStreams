package com.github.privacystreams.document;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.AppUtils;
import com.github.privacystreams.utils.ConnectionUtils;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.Logging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Base provider for google drive related providers
 */

public class BaseGoogleDriveProvider extends MStreamProvider implements GoogleDriveResultListener {
    static final String DRIVE_PREF_ACCOUNT_NAME = "drive mAccountName";
    static final String[] SCOPES = {DriveScopes.DRIVE_READONLY};
    public Drive mDrive;
    int mMaxResult = Globals.DriveConfig.defaultMaxNumberOfReturnDrives;
    long mBegin = 0;
    long mEnd = System.currentTimeMillis();
    int mResultNum = 0;
    boolean authorized = false;

    BaseGoogleDriveProvider() {
        this.addRequiredPermissions(Manifest.permission.INTERNET,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    protected void provide() {
        checkDriveApiRequirements();
    }

    /**
     * When the app just got the authorization and permission from the activity,
     * it goes to this callback.
     */
    @Override
    public void onSuccess(Drive drive) {
        mDrive = drive;
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Google Drive canceled."));
    }

    /**
     * get data from google drive api
     * returning documents are selected in the range of the input beginning time and ending time
     * time here refers to modified time
     *
     * @return output selected drive documents
     * @throws IOException caused by query
     */
    private java.util.List<String> getDataFromApi() throws IOException {
        DateTime mBeginTime = new DateTime(mBegin);
        DateTime mEndTime = new DateTime(mEnd);
        FileList fileList = mDrive.files().list()
                .setQ("modifiedTime>'" + mBeginTime + "' and modifiedTime<'" + mEndTime + "'")
                .setPageSize(mResultNum)
                .setFields("nextPageToken, " +
                        "files(id, name, createdTime, modifiedTime, size, description)")
                .execute();
        List<File> files = fileList.getFiles();
        if (files != null) {
            for (File f :
                    files) {
                Log.e("privacystream", new DriveDocument(f).toString());
                this.output(new DriveDocument(f));
            }
        } else {
            Log.e("privacystream", "Files selected does not exist");
        }
        mBegin = 0;
        mEnd = 0;
        return null;
    }

    /**
     * An asynchronous task that handles the google drive API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    class FetchDriveTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            try {
                if (mDrive != null) {
                    getDataFromApi();
                } else {
                    Logging.error("mDrive is null");
                }
            } catch (IOException e) {
                if (e instanceof UserRecoverableAuthIOException) {
                    Intent authorizationIntent = new Intent(getContext(),
                            GoogleDriveAuthorizationActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    authorizationIntent.setAction("UserRecoverableAuthIOException");
                    authorizationIntent.putExtra("request_authorization",
                            ((UserRecoverableAuthIOException) e).getIntent());

                    getContext().startActivity(authorizationIntent);
                } else {
                    Logging.error("The following error occurred:\n"
                            + e.getMessage());
                }
                return null;
            }
            return null;
        }
    }


    private void checkDriveApiRequirements() {
        String accountName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(DRIVE_PREF_ACCOUNT_NAME, null);

        if (accountName != null) {
            GoogleDriveAuthorizationActivity.setListener(this);
            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
                    getContext().getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
            mCredential.setSelectedAccountName(accountName);

            if (!ConnectionUtils.isGooglePlayServicesAvailable(getContext())) {
                ConnectionUtils.acquireGooglePlayServices(getContext());
            } else {
                mDrive = new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        JacksonFactory.getDefaultInstance(), mCredential)
                        .setApplicationName(AppUtils.getApplicationName(getContext()))
                        .build();
                authorized = true;
            }
        } else {
            GoogleDriveAuthorizationActivity.setListener(this);
            getContext()
                    .startActivity(new Intent(getContext(), GoogleDriveAuthorizationActivity.class));
        }
    }
}


