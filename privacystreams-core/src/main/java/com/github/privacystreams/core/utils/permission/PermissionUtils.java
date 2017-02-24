package com.github.privacystreams.core.utils.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.utils.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yuanchun on 30/12/2016.
 * Fine-grained permission provided by PrivacyStreams
 */

public class PermissionUtils {
    public static <Tout> void report(Function<Void, Tout> function) {
        Logging.debug("Getting personal data.");
        Logging.debug("Function: " + function.toString());
    }

    /**
     * Check if the permissions are granted in current context
     * @param context the context instance
     * @param requiredPermissions the list of permissions to check
     * @return true if all permissions are granted
     */
    public static boolean checkPermissions(Context context, Set<String> requiredPermissions) {
        boolean permissionsGranted = true;
        for (String p : requiredPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }
        return permissionsGranted;
    }
}
