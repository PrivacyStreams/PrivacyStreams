package io.github.privacystreams.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * A list of App-related utility functions.
 */

public class AppUtils {
    // Email Apps
    public static final String APP_PACKAGE_GMAIL = "com.google.android.gm";

    // Messaging Apps
    public static final String APP_PACKAGE_WHATSAPP = "com.whatsapp";
    public static final String APP_PACKAGE_WECHAT = "com.tencent.mm";
    public static final String APP_PACKAGE_FACEBOOK_MESSENGER = "com.facebook.orca";

    public static final String APP_PACKAGE_SEARCHBOX = "com.google.android.googlequicksearchbox";

    // Browsing Apps
    public static final String APP_PACKAGE_CHROME = "com.android.chrome";
    public static final String APP_PACKAGE_FIREFOX="org.mozilla.firefox";
    public static final String APP_PACKAGE_OPERA="com.opera.browser";
    /**
     * Get the application name of PrivacyStreams
     *
     * @param context
     * @return the application name.
     */
    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    /**
     * Check if a given package name is an IM app.
     *
     * @param packageName the package name of the app.
     * @return whether the app is instant messaging app or not.
     */
    public static boolean isIMApp(String packageName){
        return packageName != null
                && (packageName.equals(APP_PACKAGE_WHATSAPP)
                || packageName.equals(APP_PACKAGE_WECHAT)
                || packageName.equals(APP_PACKAGE_FACEBOOK_MESSENGER));
    }

    /**
     * Check if a given package name is a browser app.
     *
     * @param packageName the package name of the app.
     * @return whether the app is browser app or not.
     */
    public static boolean isBrowserApp(String packageName){
        return packageName != null
                && (packageName.equals(APP_PACKAGE_CHROME)||packageName.equals(APP_PACKAGE_FIREFOX)
                ||packageName.equals(APP_PACKAGE_OPERA)||packageName.equals(APP_PACKAGE_SEARCHBOX));
    }



}
