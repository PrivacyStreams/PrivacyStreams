package io.github.privacystreams.document;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

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

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.AppUtils;
import io.github.privacystreams.utils.DeviceUtils;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;

/**
 * Base provider for google drive related providers
 */

public class BaseGoogleDriveProvider extends PStreamProvider implements GoogleDriveResultListener {
    static final String DRIVE_PREF_ACCOUNT_NAME = "DRIVE_ACCOUNT_NAME";
    static final String[] SCOPES = {DriveScopes.DRIVE_READONLY};
    private Drive mDrive;
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
                Log.e("f",new DriveDocument(f).toJson().toString());
                this.output(new DriveDocument(f));
            }
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
                    Logging.error("Google Drive is not Available");
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

            if (!DeviceUtils.isGooglePlayServicesAvailable(getContext())) {
                DeviceUtils.acquireGooglePlayServices(getContext());
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

