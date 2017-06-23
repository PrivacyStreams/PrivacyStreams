package com.github.privacystreams.communication;

import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.AccEvent;
import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.notification.Notification;
import com.github.privacystreams.utils.AccessibilityUtils;
import com.github.privacystreams.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Provide a live stream of messages in IM (instant messaging) apps.
 * Including WhatsApp, Facebook Messenger, etc.
 * The messages are accessed with Android Accessibility APIs.
 */
class IMUpdatesProvider extends MStreamProvider {
<<<<<<< HEAD
    private int lastEventItemCountWhatsapp = 0;
    private int result = 0;
=======
    private int lastEventItemCountWhatsapp=0;
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
    private String detContactName = "";
    private int lastFromIndexWhatsapp = 0;
    private int lastFacebookInputLength = 0;

    private Map<String, Integer> fullUnreadMessageListWhatsapp = new HashMap<>();
    private Map<String, Integer> fullUnreadMessageListFacebook = new HashMap<>();
    private Map<String, ArrayList<String>> dbWhatsapp = new HashMap<>();
    private Map<String, ArrayList<String>> dbFacebook = new HashMap<>();

<<<<<<< HEAD
    private void saveNewMessageScrolling(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName, int eventCount, int theFromIndex) {
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                int fromIndex = theFromIndex - 2;
                if (dbWhatsapp.containsKey(contactName) && fromIndex > 0) {
                    ArrayList<String> dbList = dbWhatsapp.get(contactName);
                    int size = dbList.size();
                    if (size == eventCount) {
                        for (int i = 0; i < nodeInfoList.size(); i++) {
                            if (dbList.get(fromIndex + i) == null) {
=======
    private void saveNewMessageScrolling(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName, int eventCount,int theFromIndex){
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                int fromIndex = theFromIndex - 2;
                if(dbWhatsapp.containsKey(contactName) && fromIndex > 0){
                    ArrayList<String> dbList = dbWhatsapp.get(contactName);
                    int size = dbList.size();
                    if (size==eventCount){
                        for (int i = 0; i<nodeInfoList.size();i++) {
                            if (dbList.get(fromIndex+i)==null){
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
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
<<<<<<< HEAD
                        int count = 0;
                        for (String s : dbList) {
                            list[eventCount - size + count] = s;
                            count++;
                        }
                        for (int i = 0; i < nodeInfoList.size(); i++) {
                            if (list[fromIndex + i] == null) {
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage(
                                        nodeInfo, getContext()) ? Message.TYPE_RECEIVED
                                        : Message.TYPE_SENT;
                                this.output(new Message(messageType, messageContent, packageName, contactName, System.currentTimeMillis()));
                                list[fromIndex + i] = messageContent;
=======
                        int count =0;
                        for (String s : dbList){
                            list[eventCount-size+count]=s;
                            count++;
                        }
                        for (int i = 0; i<nodeInfoList.size();i++) {
                            if(list[fromIndex+i]==null){
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo,getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                                this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));
                                list[fromIndex+i] = messageContent;
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
                            }
                        }
                        dbList = new ArrayList<>(Arrays.asList(list));
                    }
                    dbWhatsapp.put(contactName, dbList);
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
                if (!dbWhatsapp.containsKey(contactName)) {
                    dbWhatsapp.put(contactName, new ArrayList<String>());
                }
                list = dbWhatsapp.get(contactName);
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
                    size = fullUnreadMessageListWhatsapp.get(contactName);
                    if (size > nodeInfoList.size())
                        size = nodeInfoList.size();
                    else {
                        do {
                            size++;
                        }
                        while (size < nodeInfoList.size() && list.size() > 0 && !list.get(list.size() - 1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                    fullUnreadMessageListWhatsapp.put(contactName, 0);
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
                dbWhatsapp.put(contactName, list);
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
        getUQI().getData(AccEvent.asWindowChanges(), Purpose.LIB_INTERNAL("IMUpdatesProvider"))
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
<<<<<<< HEAD
                        switch (packageName) {
                            case AppUtils.APP_PACKAGE_WHATSAPP:
                                int eventType = input.getValueByField(BaseAccessibilityEvent.EVENT_TYPE);
                                int item = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
                                if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                                    if (item > 2) {
                                        // Check if it is at the main page
                                        if (AccessibilityUtils.getMainPageSymbol(rootView, packageName)) {
                                            unreadMessageList = AccessibilityUtils.getUnreadMessageList(rootView, packageName);
                                            if (unreadMessageList != null)
                                                fullUnreadMessageListWhatsapp.putAll(unreadMessageList);
                                        } else {
                                            contactName = AccessibilityUtils.getContactNameInChat(rootView, packageName);
                                            if (contactName == null) return;
                                            detContactName = contactName;
                                            nodeInfos = AccessibilityUtils.getMessageList(rootView, packageName);
                                            int eventItemCount = getEventItemCount(packageName, input);
                                            if (!contactName.equals(detContactName)) {
                                                initializing(eventItemCount, packageName);
                                            }
                                            if (nodeInfos == null || nodeInfos.size() == 0) {
                                                return;
                                            }
                                            int index = getFromIndex(packageName, input);
                                            if (AccessibilityUtils.getUnreadSymbol(rootView, packageName)) {
                                                eventItemCount = eventItemCount - 1;
                                            }
                                            if (fullUnreadMessageListWhatsapp.containsKey(contactName) && fullUnreadMessageListWhatsapp.get(contactName) > 0) {
                                                saveNewMessage(nodeInfos, contactName, packageName, true);
                                            } else if ((eventItemCount - lastEventItemCountWhatsapp) != 1
                                                    && lastFromIndexWhatsapp != 0
                                                    && (eventItemCount - lastEventItemCountWhatsapp) != (index - lastFromIndexWhatsapp)) {
                                                if ((lastFromIndexWhatsapp - index) > 1) {
                                                    saveNewMessageScrolling(nodeInfos, contactName, packageName, eventItemCount, index);
=======
                        switch (packageName){
                           case AppUtils.APP_PACKAGE_WHATSAPP:
                               if(eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                                    if(itemCount > 2){
                                        if(AccessibilityUtils.getMainPageSymbol(rootNode,packageName)){     // Check if it is at the main page
                                            unreadMessageList = AccessibilityUtils.getUnreadMessageList(rootNode,packageName);
                                            if(unreadMessageList != null)
                                                fullUnreadMessageListWhatsapp.putAll(unreadMessageList);
                                        }
                                        else{
                                            contactName = AccessibilityUtils.getContactNameInChat(rootNode, packageName);
                                            if (contactName == null) return;
                                            detContactName = contactName;
                                            nodeInfos = AccessibilityUtils.getMessageList(rootNode, packageName);
                                            int eventItemCount = getEventItemCount(packageName, itemCount);
                                            if(!contactName.equals(detContactName)){
                                                initialize(eventItemCount, packageName);;
                                            }
                                            if(nodeInfos == null || nodeInfos.size() == 0){
                                                return;
                                            }
                                            int index = event.getFromIndex();
                                            if(AccessibilityUtils.getUnreadSymbol(rootNode, packageName)){
                                                eventItemCount = eventItemCount - 1;
                                            }
                                            if(fullUnreadMessageListWhatsapp.containsKey(contactName)&&fullUnreadMessageListWhatsapp.get(contactName)>0){
                                                saveNewMessage(nodeInfos, contactName,packageName,true);
                                            }
                                            else if((eventItemCount - lastEventItemCountWhatsapp)!=1
                                                    &&lastFromIndexWhatsapp!=0
                                                    &&(eventItemCount-lastEventItemCountWhatsapp)!=(index-lastFromIndexWhatsapp)){
                                                if((lastFromIndexWhatsapp-index)>1){
                                                    saveNewMessageScrolling(nodeInfos,contactName,packageName,eventItemCount,index);
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
                                                }
                                            } else if (eventItemCount - lastEventItemCountWhatsapp == 1) {
                                                saveNewMessage(nodeInfos, contactName, packageName, false);
                                            }
                                            lastEventItemCountWhatsapp = eventItemCount;
                                            lastFromIndexWhatsapp = index;
                                        }
                                    }
                                }
<<<<<<< HEAD
                                break;
                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                                if (!AccessibilityUtils.getMainPageSymbol(rootView, packageName)) {
                                    contactName = AccessibilityUtils.getContactNameInChat(rootView, packageName);
                                    if (contactName == null) {
                                        return;
                                    }
                                    nodeInfos = AccessibilityUtils.getMessageList(rootView, packageName);
                                    if (nodeInfos == null || nodeInfos.size() == 0) {
                                        return;
                                    }
                                    int inputLength = AccessibilityUtils.getInputBarInputSize(rootView, packageName);
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
=======
                               break;
                           case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                               if(!AccessibilityUtils.getMainPageSymbol(rootNode,packageName)){
                                  contactName = AccessibilityUtils.getContactNameInChat(rootNode, packageName);
                                   if(contactName==null) {
                                       return;
                                   }
                                   nodeInfos = AccessibilityUtils.getMessageList(rootNode, packageName);
                                   if(nodeInfos==null || nodeInfos.size()==0){
                                       return;
                                   }
                                   int inputLength = AccessibilityUtils.getInputBarInputSize(rootNode,packageName);
                                   if(fullUnreadMessageListFacebook.containsKey(contactName)&&
                                           fullUnreadMessageListFacebook.get(contactName)>0){
                                       saveNewMessage(nodeInfos, contactName,packageName,true);
                                   }
                                   else if(lastFacebookInputLength!=-1
                                           &&lastFacebookInputLength>inputLength){
                                       saveNewMessage(nodeInfos, contactName,packageName,false);
                                   }
                                   lastFacebookInputLength = inputLength;
                               }
                               break;
                       }
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f

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
                                    String name = getContactNameFromExtras((Bundle) input.getValueByField(Notification.EXTRA));
                                    if (name != null) contactName = name;
                                }
                                if (fullUnreadMessageListWhatsapp.containsKey(contactName)) {
                                    num = fullUnreadMessageListWhatsapp.get(contactName);
                                    fullUnreadMessageListWhatsapp.put(contactName, num + 1);
                                } else {
                                    fullUnreadMessageListWhatsapp.put(contactName, 1);
                                }
                                break;
                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                                if (fullUnreadMessageListFacebook.containsKey(contactName)) {
                                    num = fullUnreadMessageListFacebook.get(contactName);
                                    fullUnreadMessageListFacebook.put(contactName, num + 1);
                                } else {
                                    fullUnreadMessageListFacebook.put(contactName, 1);
                                }
                        }
                    }

                });
    }

<<<<<<< HEAD
    private int getEventItemCount(String pckName, Item input) {
        int temp = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
        if (pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)) {
            result = temp - 2;
        } else if (pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)) {
            result = temp - 1;
        }
        return result;
    }

    private int getFromIndex(String pckName, Item input) {
        int index = input.getValueByField(BaseAccessibilityEvent.FROM_INDEX);
        if (pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)) {
            result = index;
        } else if (pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)) {
            result = index;
=======
    private int getEventItemCount(String pckName, int eventCount){
        int result = 0;
        if(pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)) {
            result = eventCount-2;
        }
        else if(pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)) {
            result = eventCount-1;
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
        }
        return result;
    }

    private boolean initialize(int eventItemCount, String packageName) {
        try {
            switch (packageName) {
                case AppUtils.APP_PACKAGE_WHATSAPP:
                    lastEventItemCountWhatsapp = eventItemCount;
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

<<<<<<< HEAD
    /**
     * Get contactNameFromExtras for WhatsApp notifications
     * @param extras
     * @return contactName
     */

    private String getContactNameFromExtras(Bundle extras) {
=======
    // Finds the hidden information in whatsapp
    private String dumpExtras(Bundle extras) {
>>>>>>> ae4dc834a6fdc8674d46b45c06d76a4d82ae246f
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
