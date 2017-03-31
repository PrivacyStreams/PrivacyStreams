package com.github.privacystreams.utils;

/**
 * A list of App-related utility functions.
 */

public class AppUtils {
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
     *
     * @param packageName
     * @return whether the app is instant messaging app or not.
     */
    public static boolean isIMApp(String packageName){
        return packageName != null
                && (packageName.equals(APP_PACKAGE_WHATSAPP)
                || packageName.equals(APP_PACKAGE_WECHAT)
                || packageName.equals(APP_PACKAGE_FACEBOOK_MESSENGER));
    }

    /**
     *
     * @param packageName
     * @return whether the app is browser app or not.
     */
    public static boolean isBrowserApp(String packageName){
        return packageName != null
                && (packageName.equals(APP_PACKAGE_CHROME)||packageName.equals(APP_PACKAGE_FIREFOX)
                ||packageName.equals(APP_PACKAGE_OPERA)||packageName.equals(APP_PACKAGE_SEARCHBOX));
    }



}
