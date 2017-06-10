package com.github.privacystreams.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;
import android.widget.Toast;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.R;
import com.github.privacystreams.core.UQI;
import java.util.HashSet;
import java.util.Set;

/**
 * This Activity is to request permissions from users.
 * From API 23, it is needed to request permissions at runtime.
 */
public class PSPermissionActivity extends Activity {


    private static final String TAG = "PSPermissionActivity";
    public static final String REQUEST_CODE = "request_code";
    private String appName;
    private Set<String> requestedPermissions;
    private int requestCode;

    private static final int REQUEST_ACCESSIBILITY = 1;
    private static final int REQUEST_NOTIFICATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getSerializableExtra(REQUEST_CODE) != null) {
            requestCode = (int) getIntent().getSerializableExtra(REQUEST_CODE);
            Pair<UQI, Function<Void, Void>> uqiQuery = PermissionUtils.pendingUQIQueries.get(requestCode);
            requestedPermissions = new HashSet<>(uqiQuery.second.getRequiredPermissions());
            int labelId = this.getApplicationInfo().labelRes;
            appName = labelId == 0 ? this.getPackageName() : this.getString(labelId);
            this.requestPermissions();
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult()" + requestCode);
        if (requestCode == REQUEST_ACCESSIBILITY || requestCode == REQUEST_NOTIFICATION) {
            // If it is Accessibility request or Notification request, continue requesting.
            this.requestPermissions();
        }
    }

    private void requestPermissions() {
        if (requestedPermissions.contains(PermissionUtils.USE_ACCESSIBILITY_SERVICE)) {
            requestedPermissions.remove(PermissionUtils.USE_ACCESSIBILITY_SERVICE);
            boolean accessibilityEnabled = this.getResources().getBoolean(R.bool.accessibility_enabled);
            if (!accessibilityEnabled) {
                Logging.warn("Cannot request accessibility service permission. " +
                        "You need to set accessibility_enabled to true in res/values/bools.xml");
            }
            else {
                // request to turn on accessibility service
                try {
                    Intent settingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    this.startActivityForResult(settingsIntent, REQUEST_ACCESSIBILITY);
                    Toast.makeText(this, "Please turn on accessibility service for " + appName, Toast.LENGTH_LONG).show();
                    return;
                } catch (ActivityNotFoundException ignored) {
                }
            }
        }
        if (requestedPermissions.contains(PermissionUtils.USE_NOTIFICATION_SERVICE)) {
            requestedPermissions.remove(PermissionUtils.USE_NOTIFICATION_SERVICE);
            boolean notificationEnabled = this.getResources().getBoolean(R.bool.notification_enabled);
            if (!notificationEnabled) {
                Logging.warn("Cannot request notification listener permission. " +
                        "You need to set \"notification_enabled\" to true in res/values/bools.xml");
            }
            else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                try {
                    Intent settingsIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    this.startActivityForResult(settingsIntent, REQUEST_NOTIFICATION);
                    Toast.makeText(this, "Please turn on notification service for " + appName, Toast.LENGTH_LONG).show();
                    return;
                } catch (ActivityNotFoundException ignored) {
                }
            }
        }
        if (!requestedPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(PSPermissionActivity.this,
                    requestedPermissions.toArray(new String[requestedPermissions.size()]), requestCode);
        }
        else {
            this.retry();
        }
    }

    private void retry() {
        Pair<UQI, Function<Void, Void>> uqiQuery = PermissionUtils.pendingUQIQueries.get(requestCode);
        uqiQuery.first.evaluate(uqiQuery.second, false);
        PermissionUtils.pendingUQIQueries.remove(requestCode);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.pendingUQIQueries.containsKey(requestCode)) {
            this.retry();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
