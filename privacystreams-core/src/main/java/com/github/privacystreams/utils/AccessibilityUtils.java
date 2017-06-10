package com.github.privacystreams.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A list of Accessibility-related utilities.
 */

public class AccessibilityUtils {

    private static String ANDROID_WEBVIEW_CLASSNAME = "android.webkit.WebView";
    public static String ANDROID_VIEW_FIREFOXCLASSNAME="android.widget.TextView";


    // WhatsApp Resource IDs
    private static String WHATSAPP_MESSAGE_TEXT = "message_text";
    private static String WHATSAPP_MESSAGE_CONTACT = "conversation_contact_name";
    private static String WHATSAPP_MESSAGE_ENTRY = "entry";
    private static String WHATSAPP_MAINPAGE_CONTACT_CONTAINER = "contact_row_container";
    private static String WHATSAPP_MAINPAGE_SYMBOL = "fab"; //The green message button on the right corner
    private static String WHATSAPP_MAINPAGE_CONTACT_NAME = "conversations_row_contact_name";
    private static String WHATSAPP_MAINPAGE_MESSAGE_COUNT = "conversations_row_message_count";
    private static String WHATSAPP_UNREAD_SYMBOL = "unread_divider_tv";


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
    private static String FACEBOOK_MESSENGE_MAINPAGE_SYMBOL ="orca_home_fab";
    private static String FACEBOOK_MESSENGE_CHATPAGE_INPUT_BAR ="text_input_bar";


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
     * @return
     */
    public static boolean isIncomingMessage(AccessibilityNodeInfo nodeInfo, Context context) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);

        return rect.left < UIUtils.getScreenHeight(context)
                - rect.right && nodeInfo.getText() != null;
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
     * Get the main page symbol resource id, to check if the user is currently at the main page.
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getMainPageSymbolResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MAINPAGE_SYMBOL);
            case APP_PACKAGE_FACEBOOK_MESSENGER:
                return getFullResID(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER, FACEBOOK_MESSENGE_MAINPAGE_SYMBOL);
        }
        return null;
    }
    /**
     * Get the main page symbol resource id, to check if the page has a unread symbol
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getUnreadResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_UNREAD_SYMBOL);
        }
        return null;
    }
    /**
     * Get the chat page symbol resource id, to check if the page has a text bar
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getInputBarResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                return getFullResID(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER, FACEBOOK_MESSENGE_CHATPAGE_INPUT_BAR);

        }
        return null;
    }
    /**
     * Get the main page container resource id
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getMainPageContainerResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MAINPAGE_CONTACT_CONTAINER);
        }
        return null;
    }

    /**
     * Get the main page contact name resource id
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getMainPageContactNameResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MAINPAGE_CONTACT_NAME);
        }
        return null;
    }

    /**
     * Get the main page contact name resource id
     * @param appName the complete resource id of an app
     * @return the complete url id
     */
    private static String getMainpageMessageCountResourceId(String appName){
        switch (appName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                return getFullResID(AppUtils.APP_PACKAGE_WHATSAPP, WHATSAPP_MAINPAGE_MESSAGE_COUNT);
        }
        return null;
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
            return new ArrayList<>();
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

    public static class SerializedAccessibilityNodeInfo implements Serializable {
        public SerializedAccessibilityNodeInfo(){
            children = new HashSet<>();
        }
        public String className;
        public String boundsInScreen;
        public String boundsInParent;
        public String contentDescription;
        public String text;
        public String viewId;
        public Set<SerializedAccessibilityNodeInfo> children;
    }

    public static SerializedAccessibilityNodeInfo serialize(AccessibilityNodeInfo node){
        SerializedAccessibilityNodeInfo serializedNode = new SerializedAccessibilityNodeInfo();
        Rect boundsInScreen = new Rect(), boundsInParent = new Rect();
        if(node == null){
            return null;
        }
        if(node.getClassName() != null)
            serializedNode.className = node.getClassName().toString();
        node.getBoundsInScreen(boundsInScreen);
        node.getBoundsInParent(boundsInParent);

        serializedNode.boundsInScreen = boundsInScreen.flattenToString();
        serializedNode.boundsInParent = boundsInParent.flattenToString();

        if(node.getContentDescription() != null)
            serializedNode.contentDescription = node.getContentDescription().toString();

        if(node.getText() != null){
            serializedNode.text = node.getText().toString();
        }

        if(node.getViewIdResourceName() != null)
            serializedNode.viewId = node.getViewIdResourceName();

        int childCount = node.getChildCount();
        for(int i = 0; i < childCount; i ++){
            if(node.getChild(i) != null){
                serializedNode.children.add(serialize(node.getChild(i)));
            }
        }

        return serializedNode;
    }
    /**
     * Find out whether you are at the main page of chatting
     * @param root
     * @param appName
     * @return boolean whether you are at the main page of a chatting application or not
     */
    public static boolean getMainPageSymbol(AccessibilityNodeInfo root, String appName){
        try{
            AccessibilityNodeInfo mainPage = root.findAccessibilityNodeInfosByViewId(getMainPageSymbolResourceId(appName)).get(0);
            if(mainPage!=null){
                return true;
            }
        }
        catch (Exception exception){
            return false;
        }
        return false;
    }

    /**
     * Find out whether you have a unread message symbol or not
     * @param root
     * @param appName
     * @return boolean whether you are at the main page of a chatting application or not
     */
    public static boolean getUnreadSymbol(AccessibilityNodeInfo root, String appName){
        try{
            AccessibilityNodeInfo mainPage = root.findAccessibilityNodeInfosByViewId
                    (getUnreadResourceId(appName)).get(0);
            if(mainPage!=null){
                return true;
            }
        }
        catch (Exception exception){
            return false;
        }
        return false;
    }
    /**
     * Find out whether you have a unread message symbol or not
     * @param root
     * @param appName
     * @return boolean whether you are at the main page of a chatting application or not
     */
    public static int getInputBarInputSize(AccessibilityNodeInfo root, String appName){
        try{
            AccessibilityNodeInfo input = root.findAccessibilityNodeInfosByViewId(getInputBarResourceId(appName)).get(0);
            if(input!=null){
                String a = input.getText().toString();

                if(a != null){
                    if(a.equals("Type a message…")) return 0;
                    else if (a.equals("Aa"))return 0;
                    else return a.length();
                }

            }

        }
        catch (Exception exception){
            return 0;
        }
        return 0;
    }
    /**
     * Find out the unread message amount for each of the user
     * @param root
     * @param appName
     * @return A two dimensional array of name and unread message count
     */
    public static Map<String,Integer> getUnreadMessageList(AccessibilityNodeInfo root, String appName){
        try{
            Map<String,Integer> unreadMessageList = new HashMap<>();
            List<AccessibilityNodeInfo> containers = root.findAccessibilityNodeInfosByViewId(
                    getMainPageContainerResourceId(appName));
            for (AccessibilityNodeInfo container : containers){
                String name = String.valueOf(container.findAccessibilityNodeInfosByViewId(
                        getMainPageContactNameResourceId(appName)).get(0).getText());

                List<AccessibilityNodeInfo> a = container.findAccessibilityNodeInfosByViewId(
                        getMainpageMessageCountResourceId(appName));
                int messageCount = 0;
                if(!a.isEmpty()){
                    AccessibilityNodeInfo messageCountNode = container.findAccessibilityNodeInfosByViewId(
                            getMainpageMessageCountResourceId(appName)).get(0);
                    if(messageCountNode!=null) {
                        messageCount = Integer.parseInt(messageCountNode.getText().toString());
                    }
                }
                unreadMessageList.put(name,messageCount);
            }
            if(!unreadMessageList.isEmpty()){
                return unreadMessageList;
            }
        }
        catch (Exception exception){
            return null;
        }
        return null;
    }

}
