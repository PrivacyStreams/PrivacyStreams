package com.github.privacystreams.core.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 14/11/2016.
 * From API 23, it is needed to request permissions at runtime.
 */
public class PermissionActivity extends Activity {
    public static final String PERMISSIONS_KEY = "required_permissions";
    public static final int PERMISSION_REQUEST_CODE = 1;

    public static final String CALLBACK_ENABLED_KEY = "callback_enabled";
    private boolean callbackEnabled = false;
    private static Callback<Boolean> onPermissionRequestCallback;

    public static final String REQUEST_CODE = "request_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getSerializableExtra(REQUEST_CODE) != null) {
//            Serializable permissionRequests = getIntent().getSerializableExtra(PERMISSIONS_KEY);
            int requestCode = (int) getIntent().getSerializableExtra(REQUEST_CODE);
            UQI uqi = PermissionUtils.pendingUQIs.get(requestCode);
            Set<String> requestedPermissions = uqi.getQuery().getRequiredPermissions();
//            this.callbackEnabled = getIntent().getBooleanExtra(CALLBACK_ENABLED_KEY, false);
            ActivityCompat.requestPermissions(PermissionActivity.this, requestedPermissions.toArray(new String[requestedPermissions.size()]), requestCode);
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.pendingUQIs.containsKey(requestCode)) {
            UQI uqi = PermissionUtils.pendingUQIs.get(requestCode);
            uqi.evaluate(false);
            PermissionUtils.pendingUQIs.remove(requestCode);
            finish();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static boolean requestPermissions(Context context, String[] requiredPermissions) {
        boolean permissionsGranted = true;
        for (String p : requiredPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }

        // Check the SDK version and whether the permission is already granted or not.
        if (!permissionsGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android version M and above, there is chance to request permission at runtime
                Intent permissionRequest = new Intent(context, PermissionActivity.class);
                permissionRequest.putExtra(PermissionActivity.CALLBACK_ENABLED_KEY, false);
                permissionRequest.putExtra(PermissionActivity.PERMISSIONS_KEY, requiredPermissions);
                permissionRequest.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(permissionRequest);
            }
        }

        return permissionsGranted;
    }

    public static void requestPermissionsAndCallback(Context context, String[] requiredPermissions, Callback<Boolean> callback) {
        boolean permissionsGranted = true;
        for (String p : requiredPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }

        // Check the SDK version and whether the permission is already granted or not.
        if (!permissionsGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android version M and above, there is chance to request permission at runtime
                PermissionActivity.onPermissionRequestCallback = callback;

                Intent permissionRequest = new Intent(context, PermissionActivity.class);
                permissionRequest.putExtra(PermissionActivity.CALLBACK_ENABLED_KEY, true);
                permissionRequest.putExtra(PermissionActivity.PERMISSIONS_KEY, requiredPermissions);
                permissionRequest.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(permissionRequest);
            }
            else {
                callback.apply(null, false);
            }
        }
        else {
            callback.apply(null, true);
        }

    }
}
