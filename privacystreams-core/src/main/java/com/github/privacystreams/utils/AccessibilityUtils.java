package com.github.privacystreams.utils;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A list of Accessibility-related utilities.
 */

public class AccessibilityUtils {

    private static String ANDROID_WEBVIEW_CLASSNAME = "android.webkit.WebView";
    //public static String ANDROID_VIEW_FIREFOXCLASSNAME="org.mozilla.gecko.GeckoView";
    public static String ANDROID_VIEW_FIREFOXCLASSNAME="android.widget.TextView";


    // WhatsApp Resource IDs
    private static String WHATSAPP_MESSAGE_TEXT = "message_text";
    private static String WHATSAPP_MESSAGE_CONTACT = "conversation_contact_name";
    private static String WHATSAPP_MESSAGE_ENTRY = "entry";

    private static final int WHATSAPP_MESSAGE_LEFT_BOUND_THRESHOLD = 100;
    private static final int FACEBOOK_MESSAGE_LEFT_BOUND_THRESHOLD = 100;

    public static final String APP_PACKAGE_WHATSAPP = "com.whatsapp";
    public static final String APP_PACKAGE_FACEBOOK_MESSENGER = "com.facebook.orca";

    // Browser App URL Resource IDs
    private static String CHROME_URL_BAR = "url_bar";
    private static String FIREFOX_URL_BAR = "url_bar_title";
    private static String OPERA_URL_BAR = "url_field";

    // Facebook Resource IDs
    public static String FACEBOOK_MESSAGE_TEXT = "message_text";
    public static String FACEBOOK_MESSAGE_CONTACT = "thread_title_name";
    public static String FACEBOOK_MESSAGE_ENTRY = "edit_text";

    /**
     * traverse a tree from the root, and return all the notes in the tree
     * @param root the root node
     * @return a list of AccessibilityNodeInfo
     */
    public static List<AccessibilityNodeInfo> preOrderTraverse(AccessibilityNodeInfo root){
        if(root == null)
            return null;
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        list.add(root);
        int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i ++){
            AccessibilityNodeInfo node = root.getChild(i);
            if(node != null)
                list.addAll(preOrderTraverse(node));
        }
        return list;
    }


    /**
     * Get the complete resource id, to retrieve accessibility nodes.
     * @param packageName the package name of app
     * @param id the in-app resource id for a specific widget
     * @return the complete resource id
     */

    public static String getFullResID(String packageName, final String id) {
        return String.format("%s:id/%s", packageName, id);
    }

    /**
     * Get the complete resource id of a contact name in a given chat app.
     * @param appName the package name of the chat app
     * @return the complete resource id of contact name
     */

    private static String getContactNameInChatResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MESSAGE_CONTACT);
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                return getFullResID(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER,FACEBOOK_MESSAGE_CONTACT);
        }
        return null;
    }

    /**
     * Get the complete resource id of a message list in a given chat app.
     * @param appName the package name of the chat app
     * @return the complete resource id of a message list
     */
    private static String getMessageListResourceId(String appName){

        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MESSAGE_TEXT);
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                return getFullResID(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER,FACEBOOK_MESSAGE_TEXT);
        }
        return null;
    }


    /**
     * Get the text box resource id of a given app.
     * @param appName the complete resource id of a message list
     * @return the complete text box resource id
     */

    private static String getTextBoxResourceId(String appName){

        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MESSAGE_ENTRY);
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                return getFullResID(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER,FACEBOOK_MESSAGE_ENTRY);
        }
        return null;
    }

    /**
     * Get the url resource id, to retrieve url related accessibility nodes.
     * @param appName the complete resource id of a message list
     * @return the complete url id
     */
    private static String getUrlResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_CHROME:
                return getFullResID(AppUtils.APP_PACKAGE_CHROME, CHROME_URL_BAR);
            case AppUtils.APP_PACKAGE_FIREFOX:
                return getFullResID(AppUtils.APP_PACKAGE_FIREFOX, FIREFOX_URL_BAR);
            case AppUtils.APP_PACKAGE_OPERA:
                return getFullResID(AppUtils.APP_PACKAGE_OPERA, OPERA_URL_BAR);
        }
        return null;
    }

    /**
     *
     * @param nodeInfo
     * @param packageName
     * @return
     */
    public static boolean isIncomingMessage(AccessibilityNodeInfo nodeInfo, String packageName) {
        int left_bound_threshold =-1;
        switch (packageName){
            case AppUtils.APP_PACKAGE_WHATSAPP:
                left_bound_threshold = WHATSAPP_MESSAGE_LEFT_BOUND_THRESHOLD;
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                left_bound_threshold= FACEBOOK_MESSAGE_LEFT_BOUND_THRESHOLD;
            default:
                break;
        }

        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        if (rect.left < left_bound_threshold && nodeInfo.getText() != null) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param root
     * @param packageName
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo getTextBox(AccessibilityNodeInfo root, String packageName){
        try{
            return root.findAccessibilityNodeInfosByViewId(getTextBoxResourceId(packageName)).get(0);
        }
        catch (Exception exception){
            return null;
        }
    }

    /**
     *
     * @param root is the rootview of a given page.
     * @param packageName denotes the related app for this given page.
     * @return A list of accessibility node infos representing a list of messages in a given chat app
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static List<AccessibilityNodeInfo> getMessageList(AccessibilityNodeInfo root, String packageName){
        if(root!=null)
            return root.findAccessibilityNodeInfosByViewId(getMessageListResourceId(packageName));

        else
            return new ArrayList<AccessibilityNodeInfo>();
    }

    /**
     *
     * @param root is the rootview of a given page.
     * @param packageName denotes the related app for this given page.
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getContactNameInChat(AccessibilityNodeInfo root, String packageName){
        try{
            if(packageName.equals(APP_PACKAGE_WHATSAPP)) {
                return root.findAccessibilityNodeInfosByViewId(getContactNameInChatResourceId(packageName)).get(0).getText().toString();
            }else if(packageName.equals(APP_PACKAGE_FACEBOOK_MESSENGER)){
                return root.findAccessibilityNodeInfosByViewId(getContactNameInChatResourceId(packageName)).get(0).getContentDescription().toString();
            }else{
                return null;
            }
        }
        catch (Exception exception){
            return null;
        }
    }

    /**
     *
     * @param nodeInfoList is the complete list of node infos in a page.
     * @return the first webview node.
     */
    public static String getWebViewTitle(List<AccessibilityNodeInfo> nodeInfoList) {
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            Logging.debug(nodeInfo.getClassName().toString());
            if (nodeInfo.getClassName().equals(ANDROID_WEBVIEW_CLASSNAME)) {
                return nodeInfo.getContentDescription().toString();
            }else if (nodeInfo.getClassName().equals(ANDROID_VIEW_FIREFOXCLASSNAME)) {
                return nodeInfo.getText().toString();
            }
        }
        return null;
    }



    /**
     *
     * @param root
     * @param appName
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getBrowserCurrentUrl(AccessibilityNodeInfo root, String appName){
        try{
            AccessibilityNodeInfo bar = root.findAccessibilityNodeInfosByViewId(getUrlResourceId(appName)).get(0);
            if(bar!=null){
                return bar.getText().toString();
            }
        }
        catch (Exception exception){
            return null;
        }
        return null;
    }
}
