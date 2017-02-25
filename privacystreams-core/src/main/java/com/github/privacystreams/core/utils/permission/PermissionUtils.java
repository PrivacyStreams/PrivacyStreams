package com.github.privacystreams.core.utils.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utils.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuanchun on 30/12/2016.
 * Fine-grained permission provided by PrivacyStreams
 */

public class PermissionUtils {
    /**
     * Check if the permissions are granted in current context
     * @param context the context instance
     * @param requiredPermissions the list of permissions to check
     * @return true if all permissions are granted
     */
    public static boolean checkPermissions(Context context, Set<String> requiredPermissions) {
        if (requiredPermissions == null) return true;
        boolean permissionsGranted = true;
        for (String p : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }
        return permissionsGranted;
    }

    public static Map<Integer, UQI> pendingUQIs = new HashMap<>();
    public static void requestPermissionAndEvaluate(UQI uqi) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android version M and above, there is chance to request permissions at runtime
            int requestCode = uqi.hashCode();
            pendingUQIs.put(requestCode, uqi);
            Intent permissionRequest = new Intent(uqi.getContext(), PermissionActivity.class);
            permissionRequest.putExtra(PermissionActivity.REQUEST_CODE, requestCode);
            permissionRequest.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uqi.getContext().startActivity(permissionRequest);
        }
        else {
            // For Android M-, we cannot request permissions at runtime
            uqi.evaluate(false);
        }
    }
}
