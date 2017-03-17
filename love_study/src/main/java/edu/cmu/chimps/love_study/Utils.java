package edu.cmu.chimps.love_study;

import android.app.ActivityManager;
import android.content.Context;

import com.github.privacystreams.accessibility.MyAccessibilityService;

/**
 * Created by fanglinchen on 3/17/17.
 */

public class Utils {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static boolean isAccessibilityEnabled(Context context) {
        return isMyServiceRunning(context, MyAccessibilityService.class);
    }

    public static boolean isTrackingEnabled(Context context){
        return isMyServiceRunning(context, TrackingService.class);
    }

}
