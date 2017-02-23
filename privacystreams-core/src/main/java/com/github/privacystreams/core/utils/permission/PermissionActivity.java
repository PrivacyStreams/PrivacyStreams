package com.github.privacystreams.core.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.github.privacystreams.core.Callback;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getSerializableExtra(PERMISSIONS_KEY) != null) {
            Serializable permissionRequests = getIntent().getSerializableExtra(PERMISSIONS_KEY);
            List<String> requestedPermissions = Arrays.asList((String[]) permissionRequests);
            this.callbackEnabled = getIntent().getBooleanExtra(CALLBACK_ENABLED_KEY, false);
            ActivityCompat.requestPermissions(PermissionActivity.this, requestedPermissions.toArray(new String[requestedPermissions.size()]), PERMISSION_REQUEST_CODE);
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            int notGranted = 0;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    notGranted++;
                }
            }
            if (notGranted > 0) {
                if (this.callbackEnabled) {
                    PermissionActivity.onPermissionRequestCallback.apply(null, false);
                }
                Intent result = new Intent();
                setResult(Activity.RESULT_CANCELED, result);
                finish();
            } else {
                if (this.callbackEnabled) {
                    PermissionActivity.onPermissionRequestCallback.apply(null, true);
                }
                Intent result = new Intent();
                setResult(Activity.RESULT_OK, result);
                finish();
            }

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
