package io.github.privacystreams.communication;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.accessibility.AccEvent;
import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.notification.Notification;
import io.github.privacystreams.utils.AccessibilityUtils;
import io.github.privacystreams.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.PStreamProvider;

/**
 * Provide a live stream of messages in IM (instant messaging) apps.
 * Including WhatsApp, Facebook Messenger, etc.
 * The messages are accessed with Android Accessibility APIs.
 * This provider supports more UI functionalities so that developers
 * can get access to message positions and the rootview of the message window.
 */

public class IMUIUpdatesProvider extends PStreamProvider {
    private int mWhatsAppLastEventItemCount = 0;
    private String mDetContactName = "";
    private int mWhatsAppLastFromIndex = 0;
    private int mFacebookLastInputLength = 0;

    private Map<String, Integer> mWhatsAppFullUnreadMessageList = new HashMap<>();
    private Map<String, Integer> mFacebookFullUnreadMessageList = new HashMap<>();
    private Map<String, ArrayList<String>> mWhatsAppDb = new HashMap<>();
    private Map<String, ArrayList<String>> mFacebookDb = new HashMap<>();

    private void saveNewMessageScrolling(List<AccessibilityNodeInfo> nodeInfoList,
                                         String contactName,
                                         String packageName,
                                         int eventCount,
                                         int theFromIndex,
                                         AccessibilityNodeInfo rootNode) {
        int nodeInfoListSize = nodeInfoList.size();
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                int fromIndex = theFromIndex - 2;
                if (mWhatsAppDb.containsKey(contactName) && fromIndex > 0) {
                    ArrayList<String> dbList = mWhatsAppDb.get(contactName);
                    int dbSize = dbList.size();
                    if (dbSize == eventCount) {
                        for (int i = 0; i < nodeInfoListSize; i++) {
                            if (dbList.get(fromIndex + i) == null) {
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                dbList.remove(fromIndex + i);
                                dbList.add(fromIndex + i, messageContent);
                            }
                        }
                    } else if (eventCount > dbSize) {
                        String[] list = new String[eventCount];
                        int count = 0;
                        for (String s : dbList) {
                            list[eventCount - dbSize + count] = s;
                            count++;
                        }
                        for (int i = 0; i < nodeInfoListSize; i++) {
                            if (list[fromIndex + i] == null) {
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, getContext())
                                        ? InstantMessage.TYPE_RECEIVED : InstantMessage.TYPE_SENT;
                                int messageLogTime = AccessibilityUtils.getLogTimeByTextView(nodeInfo, packageName);

                                Rect rect = new Rect();
                                nodeInfo.getBoundsInScreen(rect);
                                int[] messagePosition = new int[]{rect.left, rect.top, rect.right, rect.bottom};

                                this.output(new InstantMessage(messageType,
                                        messageContent,
                                        messageLogTime,
                                        packageName,
                                        contactName,
                                        System.currentTimeMillis(),
                                        messagePosition,
                                        rootNode
                                ));
                                list[fromIndex + i] = messageContent;
                            }
                        }
                        dbList = new ArrayList<>(Arrays.asList(list));
                    }
                    mWhatsAppDb.put(contactName, dbList);
                }
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                break;
        }
    }

    private void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList,
                                String contactName,
                                String packageName,
                                Boolean contains,
                                AccessibilityNodeInfo rootNode) {
        int messageAmount;
        int nodeInfoListSize = nodeInfoList.size();
        ArrayList<String> individualDb;
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                if (!mWhatsAppDb.containsKey(contactName)) {
                    mWhatsAppDb.put(contactName, new ArrayList<String>());
                }
                individualDb = mWhatsAppDb.get(contactName);
                if (!contains) {
                    messageAmount = 1;
                    if (individualDb.size() > 0
                            && !(individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - 1).getText().toString()))) {
                        do {
                            messageAmount++;
                        }
                        while (messageAmount < nodeInfoListSize && individualDb.size() > 0 && !individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - messageAmount).getText().toString()));
                        messageAmount--;
                    }
                } else {
                    messageAmount = mWhatsAppFullUnreadMessageList.get(contactName);
                    if (messageAmount > nodeInfoListSize)
                        messageAmount = nodeInfoListSize;
                    else {
                        do {
                            messageAmount++;
                        }
                        while (messageAmount < nodeInfoListSize && individualDb.size() > 0 && !individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - messageAmount).getText().toString()));
                        messageAmount--;
                    }
                    mWhatsAppFullUnreadMessageList.put(contactName, 0);
                }
                for (int i = messageAmount; i > 0; i--) {
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoListSize - i);
                    String messageContent = nodeInfoList.get(nodeInfoListSize - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, this.getContext())
                            ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                    int messageLogTime = AccessibilityUtils.getLogTimeByTextView(nodeInfo, packageName);

                    Rect rect = new Rect();
                    nodeInfo.getBoundsInScreen(rect);
                    int[] messagePosition = new int[]{rect.left, rect.top, rect.right, rect.bottom};


                    this.output(new InstantMessage(messageType,
                            messageContent,
                            messageLogTime,
                            packageName,
                            contactName,
                            System.currentTimeMillis(),
                            messagePosition,
                            rootNode
                    ));
                    individualDb.add(messageContent);
                }
                mWhatsAppDb.put(contactName, individualDb);
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                // Get the amount of unread message
                if (!mFacebookDb.containsKey(contactName)) {
                    mFacebookDb.put(contactName, new ArrayList<String>());
                }
                individualDb = mFacebookDb.get(contactName);
                if (!contains) {
                    messageAmount = 1;
                    if (individualDb.size() > 0
                            && !(individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - 1).getText().toString()))) {
                        do {
                            messageAmount++;
                        } while (messageAmount < nodeInfoListSize
                                && individualDb.size() > 0
                                && !individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - messageAmount).getText().toString()));
                        messageAmount--;
                    }
                } else {
                    messageAmount = mFacebookFullUnreadMessageList.get(contactName);
                    if (messageAmount > nodeInfoListSize)
                        messageAmount = nodeInfoListSize;
                    else {
                        do {
                            messageAmount++;
                        } while (messageAmount < nodeInfoListSize
                                && individualDb.size() > 0
                                && !individualDb.get(individualDb.size() - 1).equals(nodeInfoList.get(nodeInfoListSize - messageAmount).getText().toString()));
                        messageAmount--;
                    }
                    mFacebookFullUnreadMessageList.put(contactName, 0);
                }
                for (int i = messageAmount; i > 0; i--) {
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoListSize - i);
                    String messageContent = nodeInfoList.get(nodeInfoListSize - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, this.getContext())
                            ? InstantMessage.TYPE_RECEIVED : InstantMessage.TYPE_SENT;
                    int messageLogTime = AccessibilityUtils.getLogTimeByTextView(nodeInfo, packageName);

                    Rect rect = new Rect();
                    nodeInfo.getBoundsInScreen(rect);
                    int[] messagePosition = new int[]{rect.left, rect.top, rect.right, rect.bottom};

                    this.output(new InstantMessage(messageType,
                            messageContent,
                            messageLogTime,
                            packageName,
                            contactName,
                            System.currentTimeMillis(),
                            messagePosition,
                            rootNode));
                    individualDb.add(messageContent);
                }
                mFacebookDb.put(contactName, individualDb);
                break;
        }
    }

    @Override
    protected void provide() {
        getUQI().getData(AccEvent.asWindowChanges(), Purpose.LIB_INTERNAL("IMUIUpdatesProvider"))
                .filter(ItemOperators.isFieldIn(AccEvent.PACKAGE_NAME,
                        new String[]{
                                AppUtils.APP_PACKAGE_WHATSAPP,
                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        AccessibilityEvent event = input.getValueByField(AccEvent.EVENT);
                        AccessibilityNodeInfo rootNode = input.getValueByField(AccEvent.ROOT_NODE);
                        String packageName = input.getValueByField(AccEvent.PACKAGE_NAME);
                        int itemCount = event.getItemCount();
                        int eventType = event.getEventType();
                        String contactName;
                        Map<String, Integer> unreadMessageList;
                        List<AccessibilityNodeInfo> nodeInfos;
                        switch (packageName) {
                            case AppUtils.APP_PACKAGE_WHATSAPP:
                                if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                                    if (itemCount > 2) {
                                        if (AccessibilityUtils.getMainPageSymbol(rootNode, packageName)) {     // Check if it is at the main page
                                            unreadMessageList = AccessibilityUtils.getUnreadMessageList(rootNode, packageName);
                                            if (unreadMessageList != null)
                                                mWhatsAppFullUnreadMessageList.putAll(unreadMessageList);
                                        } else {
                                            contactName = AccessibilityUtils.getContactNameInChat(rootNode, packageName);
                                            if (contactName == null) return;
                                            mDetContactName = contactName;
                                            nodeInfos = AccessibilityUtils.getMessageList(rootNode, packageName);
                                            int eventItemCount = getEventItemCount(packageName, itemCount);
                                            if (!contactName.equals(mDetContactName)) {
                                                initialize(eventItemCount, packageName);
                                                ;
                                            }
                                            if (nodeInfos == null || nodeInfos.size() == 0) {
                                                return;
                                            }
                                            int index = event.getFromIndex();
                                            if (AccessibilityUtils.getUnreadSymbol(rootNode, packageName)) {
                                                eventItemCount = eventItemCount - 1;
                                            }
                                            if (mWhatsAppFullUnreadMessageList.containsKey(contactName)
                                                    && mWhatsAppFullUnreadMessageList.get(contactName) > 0) {
                                                saveNewMessage(nodeInfos,
                                                        contactName,
                                                        packageName,
                                                        true,
                                                        rootNode);
                                            } else if ((eventItemCount - mWhatsAppLastEventItemCount) != 1
                                                    && mWhatsAppLastFromIndex != 0
                                                    && (eventItemCount - mWhatsAppLastEventItemCount) != (index - mWhatsAppLastFromIndex)) {
                                                if ((mWhatsAppLastFromIndex - index) > 1) {
                                                    saveNewMessageScrolling(nodeInfos,
                                                            contactName,
                                                            packageName,
                                                            eventItemCount,
                                                            index,
                                                            rootNode);
                                                }
                                            } else if (eventItemCount - mWhatsAppLastEventItemCount == 1) {
                                                saveNewMessage(nodeInfos,
                                                        contactName,
                                                        packageName,
                                                        false,
                                                        rootNode);
                                            }
                                            mWhatsAppLastEventItemCount = eventItemCount;
                                            mWhatsAppLastFromIndex = index;
                                        }
                                    }
                                }
                                break;
                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                                if (!AccessibilityUtils.getMainPageSymbol(rootNode, packageName)) {
                                    contactName = AccessibilityUtils.getContactNameInChat(rootNode, packageName);
                                    if (contactName == null) {
                                        return;
                                    }

                                    nodeInfos = AccessibilityUtils.getMessageList(rootNode, packageName);
                                    if (nodeInfos == null || nodeInfos.size() == 0) {
                                        return;
                                    }
                                    int inputLength = AccessibilityUtils.getInputBarInputSize(rootNode, packageName);
                                    if (mFacebookFullUnreadMessageList.containsKey(contactName) &&
                                            mFacebookFullUnreadMessageList.get(contactName) > 0) {
                                        saveNewMessage(nodeInfos,
                                                contactName,
                                                packageName,
                                                true,
                                                rootNode);
                                    } else if (mFacebookLastInputLength != -1
                                            && mFacebookLastInputLength > inputLength) {
                                        saveNewMessage(nodeInfos,
                                                contactName,
                                                packageName,
                                                false,
                                                rootNode);
                                    }
                                    mFacebookLastInputLength = inputLength;
                                }
                                break;
                        }

                    }
                });

        getUQI().getData(Notification.asUpdates(), Purpose.FEATURE("Notification Trigger"))
                .filter(ItemOperators.isFieldIn(Notification.PACKAGE_NAME,
                        new String[]{AppUtils.APP_PACKAGE_WHATSAPP,
                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
                .filter(Comparators.eq(Notification.CATEGORY,
                        "msg"))
                .filter(Comparators.eq(Notification.ACTION, Notification.ACTION_POSTED))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        String contactName = input.getValueByField(Notification.TITLE);
                        String packageName = input.getValueByField(Notification.PACKAGE_NAME);
                        int num;
                        switch (packageName) {
                            case AppUtils.APP_PACKAGE_WHATSAPP:
                                if (contactName.equals("WhatsApp")) {
                                    String name = getWhatsAppContactName((Bundle) input.getValueByField(Notification.EXTRA));
                                    if (name != null) contactName = name;
                                }
                                if (mWhatsAppFullUnreadMessageList.containsKey(contactName)) {
                                    num = mWhatsAppFullUnreadMessageList.get(contactName);
                                    mWhatsAppFullUnreadMessageList.put(contactName, num + 1);
                                } else {
                                    mWhatsAppFullUnreadMessageList.put(contactName, 1);
                                }
                                break;
                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                                if (mFacebookFullUnreadMessageList.containsKey(contactName)) {
                                    num = mFacebookFullUnreadMessageList.get(contactName);
                                    mFacebookFullUnreadMessageList.put(contactName, num + 1);
                                } else {
                                    mFacebookFullUnreadMessageList.put(contactName, 1);
                                }
                        }
                    }

                });
    }

    private int getEventItemCount(String pckName, int eventCount) {
        int result = 0;
        if (pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)) {
            result = eventCount - 2;
        } else if (pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)) {
            result = eventCount - 1;
        }
        return result;
    }

    private boolean initialize(int eventItemCount, String packageName) {
        try {
            switch (packageName) {
                case AppUtils.APP_PACKAGE_WHATSAPP:
                    mWhatsAppLastEventItemCount = eventItemCount;
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Finds the hidden information in whatsApp
    private String getWhatsAppContactName(Bundle extras) {
        String contactName = "";
        if (extras != null) {
            for (String k : extras.keySet()) {
                Object o = extras.get(k);
                if (o instanceof CharSequence[]) {
                    // case for "textLines" and such
                    CharSequence n = "";
                    CharSequence[] data = (CharSequence[]) o;
                    for (CharSequence d : data) {
                        n = d;
                    }
                    contactName = n.toString();
                    int in = contactName.indexOf(": ");
                    contactName = contactName.substring(0, in);
                }
            }
        }
        return contactName;
    }
}
