package io.github.privacystreams.communication;

import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.accessibility.AccEvent;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.AccessibilityUtils;
import io.github.privacystreams.utils.AppUtils;

/**
 * Provide a live stream of messages in IM (instant messaging) apps.
 * Including WhatsApp, Facebook Messenger, etc.
 * The messages are accessed with Android Accessibility APIs.
 */
class IMUpdatesProvider extends PStreamProvider {
    private int lastEventItemCountWhatsApp = 0;
    private String detContactName = "";
    private int lastFromIndexWhatsApp = 0;
    private int lastFacebookInputLength = 0;

    private Map<String, Integer> fullUnreadMessageListWhatsApp = new HashMap<>();
    private Map<String, Integer> fullUnreadMessageListFacebook = new HashMap<>();
    private Map<String, ArrayList<String>> dbWhatsApp = new HashMap<>();
    private Map<String, ArrayList<String>> dbFacebook = new HashMap<>();

    private void saveNewMessageScrolling(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName, int eventCount, int theFromIndex) {
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                int fromIndex = theFromIndex - 2;
                if (dbWhatsApp.containsKey(contactName) && fromIndex > 0) {
                    ArrayList<String> dbList = dbWhatsApp.get(contactName);
                    int size = dbList.size();
                    if (size == eventCount) {
                        for (int i = 0; i < nodeInfoList.size(); i++) {
                            if (dbList.get(fromIndex + i) == null) {
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage
                                        (nodeInfo, this.getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                                this.output(new Message(messageType, messageContent,
                                        packageName, contactName, System.currentTimeMillis()));
                                dbList.remove(fromIndex + i);
                                dbList.add(fromIndex + i, messageContent);
                            }
                        }
                    } else if (eventCount > size) {
                        String[] list = new String[eventCount];
                        int count = 0;
                        for (String s : dbList) {
                            list[eventCount - size + count] = s;
                            count++;
                        }
                        for (int i = 0; i < nodeInfoList.size(); i++) {
                            if (list[fromIndex + i] == null) {
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                                this.output(new Message(messageType, messageContent, packageName, contactName, System.currentTimeMillis()));
                                list[fromIndex + i] = messageContent;
                            }
                        }
                        dbList = new ArrayList<>(Arrays.asList(list));
                    }
                    dbWhatsApp.put(contactName, dbList);
                }
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                break;
        }
    }

    private void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName, Boolean contains) {
        int size;
        ArrayList<String> list;
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                if (!dbWhatsApp.containsKey(contactName)) {
                    dbWhatsApp.put(contactName, new ArrayList<String>());
                }
                list = dbWhatsApp.get(contactName);
                if (!contains) {
                    size = 1;
                    if (list.size() > 0
                            && !(list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - 1).getText().toString()))) {
                        do {
                            size++;
                        }
                        while (size < nodeInfoList.size() && list.size() > 0 && !list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                } else {
                    size = fullUnreadMessageListWhatsApp.get(contactName);
                    if (size > nodeInfoList.size())
                        size = nodeInfoList.size();
                    else {
                        do {
                            size++;
                        }
                        while (size < nodeInfoList.size() && list.size() > 0 && !list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                    fullUnreadMessageListWhatsApp.put(contactName, 0);
                }
                for (int i = size; i > 0; i--) {
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - i);
                    String messageContent = nodeInfoList.get(nodeInfoList.size() - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, this.getContext())
                            ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                    this.output(new Message(messageType, messageContent, packageName, contactName, System.currentTimeMillis()));
                    list.add(messageContent);
                }
                dbWhatsApp.put(contactName, list);
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                // Get the amount of unread message
                if (!dbFacebook.containsKey(contactName)) {
                    dbFacebook.put(contactName, new ArrayList<String>());
                }
                list = dbFacebook.get(contactName);
                if (!contains) {
                    size = 1;
                    if (list.size() > 0 && !(list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - 1).getText().toString()))) {
                        do {
                            size++;
                        }
                        while (size < nodeInfoList.size() && list.size() > 0 && !list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                } else {
                    size = fullUnreadMessageListFacebook.get(contactName);
                    if (size > nodeInfoList.size())
                        size = nodeInfoList.size();
                    else {
                        do {
                            size++;
                        } while (size < nodeInfoList.size() && list.size() > 0
                                && !list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                    fullUnreadMessageListFacebook.put(contactName, 0);
                }
                for (int i = size; i > 0; i--) {
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - i);
                    String messageContent = nodeInfoList.get(nodeInfoList.size() - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo, this.getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                    this.output(new Message(messageType, messageContent, packageName, contactName, System.currentTimeMillis()));
                    list.add(messageContent);
                }
                dbFacebook.put(contactName, list);
                break;
        }
    }

    @Override
    protected void provide() {
        new UQI(getContext()).getData(AccEvent.asWindowChanges(), Purpose.LIB_INTERNAL("IMUpdatesProvider"))
//                .filter(ItemOperators.isFieldIn(AccEvent.PACKAGE_NAME,
//                        new String[]{
//                                AppUtils.APP_PACKAGE_WHATSAPP,
//                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
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
                                                fullUnreadMessageListWhatsApp.putAll(unreadMessageList);
                                        } else {
                                            contactName = AccessibilityUtils.getContactNameInChat(rootNode, packageName);
                                            if (contactName == null) return;
                                            detContactName = contactName;
                                            nodeInfos = AccessibilityUtils.getMessageList(rootNode, packageName);
                                            int eventItemCount = getEventItemCount(packageName, itemCount);
                                            if (!contactName.equals(detContactName)) {
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
                                            if (fullUnreadMessageListWhatsApp.containsKey(contactName) && fullUnreadMessageListWhatsApp.get(contactName) > 0) {
                                                saveNewMessage(nodeInfos, contactName, packageName, true);
                                            } else if ((eventItemCount - lastEventItemCountWhatsApp) != 1
                                                    && lastFromIndexWhatsApp != 0
                                                    && (eventItemCount - lastEventItemCountWhatsApp) != (index - lastFromIndexWhatsApp)) {
                                                if ((lastFromIndexWhatsApp - index) > 1) {
                                                    saveNewMessageScrolling(nodeInfos, contactName, packageName, eventItemCount, index);
                                                }
                                            } else if (eventItemCount - lastEventItemCountWhatsApp == 1) {
                                                saveNewMessage(nodeInfos, contactName, packageName, false);
                                            }
                                            lastEventItemCountWhatsApp = eventItemCount;
                                            lastFromIndexWhatsApp = index;
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
                                    if (fullUnreadMessageListFacebook.containsKey(contactName) &&
                                            fullUnreadMessageListFacebook.get(contactName) > 0) {
                                        saveNewMessage(nodeInfos, contactName, packageName, true);
                                    } else if (lastFacebookInputLength != -1
                                            && lastFacebookInputLength > inputLength) {
                                        saveNewMessage(nodeInfos, contactName, packageName, false);
                                    }
                                    lastFacebookInputLength = inputLength;
                                }
                                break;
                        }

                    }
                });
//
//        getUQI().getData(Notification.asUpdates(),Purpose.FEATURE("Notification Trigger"))
//                .filter(ItemOperators.isFieldIn(Notification.PACKAGE_NAME,
//                        new String[]{AppUtils.APP_PACKAGE_WHATSAPP,
//                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
//                .filter(Comparators.eq(Notification.CATEGORY,
//                        "msg"))
//                .filter(Comparators.eq(Notification.ACTION, Notification.ACTION_POSTED))
//                .forEach(new Callback<Item>() {
//                    @Override
//                    protected void onInput(Item input) {
//                        String contactName = input.getValueByField(Notification.TITLE);
//                        String packageName = input.getValueByField(Notification.PACKAGE_NAME);
//                        int num;
//                        switch (packageName){
//                            case AppUtils.APP_PACKAGE_WHATSAPP:
//                                if(contactName.equals("WhatsApp")){
//                                    String name =dumpExtras((Bundle) input.getValueByField(Notification.EXTRA));
//                                    if(name!=null) contactName = name;
//                                }
//                                if(fullUnreadMessageListWhatsApp.containsKey(contactName)){
//                                    num = fullUnreadMessageListWhatsApp.get(contactName);
//                                    fullUnreadMessageListWhatsApp.put(contactName,num+1);
//                                }
//                                else{
//                                    fullUnreadMessageListWhatsApp.put(contactName,1);
//                                }
//                                break;
//                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
//                                if(fullUnreadMessageListFacebook.containsKey(contactName)){
//                                    num = fullUnreadMessageListFacebook.get(contactName);
//                                    fullUnreadMessageListFacebook.put(contactName,num+1);
//                                }
//                                else{
//                                    fullUnreadMessageListFacebook.put(contactName,1);
//                                }
//                        }
//                    }

//                });
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
                    lastEventItemCountWhatsApp = eventItemCount;
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Finds the hidden information in WhatsApp
    private String dumpExtras(Bundle extras) {
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