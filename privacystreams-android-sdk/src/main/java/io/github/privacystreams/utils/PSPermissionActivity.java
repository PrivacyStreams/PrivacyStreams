package io.github.privacystreams.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Pair;
import android.widget.Toast;

import io.github.privacystreams.accessibility.PSAccessibilityService;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.R;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.notification.PSNotificationListenerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Integer> requestList;
    private int requestListId;
    private Handler requestHandler;

    private static final int REQUEST_ACCESSIBILITY = 1;
    private static final int REQUEST_NOTIFICATION = 2;
    private static final int REQUEST_OVERLAY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int labelId = this.getApplicationInfo().labelRes;
        appName = labelId == 0 ? this.getPackageName() : this.getString(labelId);
        requestedPermissions = new HashSet<>();
        requestCode = -1;
        requestList = new ArrayList<>();
        requestListId = 0;
        requestHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int requestCode = msg.what;
                Pair<UQI, Function<Void, Void>> uqiQuery = PermissionUtils.pendingUQIQueries.get(requestCode);
                if(uqiQuery != null) {
                    PSPermissionActivity.this.requestCode = requestCode;
                    PSPermissionActivity.this.requestedPermissions = new HashSet<>(uqiQuery.second.getRequiredPermissions());
                    PSPermissionActivity.this.requestPermissions();
                }
            }
        };

        Intent intent = getIntent();
        this.onNewIntent(intent);

        permRequestTask.start();
    }

    Thread permRequestTask = new Thread() {
        @Override
        public void run() {
            while (requestList.size() > requestListId) {
                int curRequestCode = requestList.get(requestListId);
                if (curRequestCode == requestCode) {
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                requestCode = curRequestCode;
                Message msg = requestHandler.obtainMessage(curRequestCode);
                msg.sendToTarget();
            }
            Logging.debug("All permission requests processed.");
            PSPermissionActivity.this.finish();
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null && intent.getSerializableExtra(REQUEST_CODE) != null) {
            int newRequestCode = (int) intent.getSerializableExtra(REQUEST_CODE);
            requestList.add(newRequestCode);
            Logging.debug("New permission request: " + newRequestCode);
        }
        else {
            // Shouldn't be here
            Logging.warn("PSPermissionActivity started without an intent.");
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult()" + requestCode);
        if (requestCode == REQUEST_ACCESSIBILITY
                || requestCode == REQUEST_NOTIFICATION
                || requestCode == REQUEST_OVERLAY) {
            // If it is Accessibility request, Notification request, or Overlay request, continue requesting.
            this.requestPermissions();
        }
    }

    private void requestPermissions() {
        if (requestedPermissions.contains(PermissionUtils.USE_ACCESSIBILITY_SERVICE)) {
            requestedPermissions.remove(PermissionUtils.USE_ACCESSIBILITY_SERVICE);
            if (!PSAccessibilityService.enabled) {
                boolean accessibilityEnabled = this.getResources().getBoolean(R.bool.ps_accessibility_enabled);
                if (!accessibilityEnabled) {
                    Logging.warn("Cannot request accessibility service permission. " +
                            "You need to set ps_accessibility_enabled to true in res/values/bools.xml");
                } else {
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
        }

        if (requestedPermissions.contains(PermissionUtils.USE_NOTIFICATION_SERVICE)) {
            requestedPermissions.remove(PermissionUtils.USE_NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (!PSNotificationListenerService.enabled) {
                    boolean notificationEnabled = this.getResources().getBoolean(R.bool.ps_notification_enabled);
                    if (!notificationEnabled) {
                        Logging.warn("Cannot request notification listener permission. " +
                                "You need to set \"ps_notification_enabled\" to true in res/values/bools.xml");
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        try {
                            Intent settingsIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                            this.startActivityForResult(settingsIntent, REQUEST_NOTIFICATION);
                            Toast.makeText(this, "Please turn on notification service for " + appName, Toast.LENGTH_LONG).show();
                            return;
                        } catch (ActivityNotFoundException ignored) {
                        }
                    }
                }
            }
        }

        if (requestedPermissions.contains(PermissionUtils.OVERLAY)) {
            requestedPermissions.remove(PermissionUtils.OVERLAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    boolean overlayEnabled = this.getResources().getBoolean(R.bool.ps_overlay_enabled);
                    if (!overlayEnabled) {
                        Logging.warn("Cannot request overlay permission. " +
                                "You need to set \"ps_overlay_enabled\" to true in res/values/bools.xml");
                    } else {
                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            this.startActivityForResult(intent, REQUEST_OVERLAY);
                            Toast.makeText(this, "Please turn on overlay permission for " + appName, Toast.LENGTH_LONG).show();
                            return;
                        } catch (ActivityNotFoundException ignored) {
                        }
                    }
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
        this.requestListId++;
        Logging.debug("Processed permission request: " + requestCode);
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
