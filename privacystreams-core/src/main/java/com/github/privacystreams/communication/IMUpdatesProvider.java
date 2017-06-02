package com.github.privacystreams.communication;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.BaseAccessibilityEvent;
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
    private int lastEventItemCountWhatsapp=0;
    private int result=0;
    private String detContactName = "";
    private int lastFromIndexWhatsapp = 0;
    private int lastFacebookInputLength = 0;

    private Map<String,Integer> fullUnreadMessageListWhatsapp = new HashMap<>();
    private Map<String,Integer> fullUnreadMessageListFacebook = new HashMap<>();
    private Map<String,ArrayList<String>> dbWhatsapp = new  HashMap<>();
    private Map<String,ArrayList<String>> dbFacebook = new  HashMap<>();



    private void saveNewMessageScrolling(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName,int eventCount,int theFromIndex){
        switch (packageName) {
            case AppUtils.APP_PACKAGE_WHATSAPP:
                int fromIndex = theFromIndex-2;
                if(dbWhatsapp.containsKey(contactName)&&fromIndex>0){
                    ArrayList<String> dbList = dbWhatsapp.get(contactName);
                    Log.e("Test","DBlist size"+dbList.size());
                    int size = dbList.size();
                    if(size==eventCount){
                        for(int i = 0; i<nodeInfoList.size();i++) {
                            if(dbList.get(fromIndex+i)==null){
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage
                                        (nodeInfo,this.getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                                this.output(new Message(messageType,messageContent,
                                        packageName,contactName,System.currentTimeMillis()));
                                dbList.remove(fromIndex+i);
                                dbList.add(fromIndex+i,messageContent);
                            }
                        }
                    }
                    else if(eventCount>size){
                        String[] list = new String[eventCount];
                        int count =0;
                        for(String s : dbList){
                            list[eventCount-size+count]=s;
                            count++;
                        }
                        for(int i = 0; i<nodeInfoList.size();i++) {
                            if(list[fromIndex+i]==null){
                                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                                String messageContent = nodeInfoList.get(i).getText().toString();
                                String messageType = AccessibilityUtils.isIncomingMessage(
                                        nodeInfo,getContext()) ? Message.TYPE_RECEIVED
                                        : Message.TYPE_SENT;
                                this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));
                                list[fromIndex+i] = messageContent;
                            }
                        }
                        dbList= new ArrayList<>(Arrays.asList(list));
                    }
                    dbWhatsapp.put(contactName,dbList);
                }
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                break;
        }
    }
    private void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName ,Boolean contains) {
        int size;
        ArrayList<String> list;
        switch (packageName){
            case AppUtils.APP_PACKAGE_WHATSAPP:
                if(!dbWhatsapp.containsKey(contactName)){
                    dbWhatsapp.put(contactName,new ArrayList<String>());
                }
                 list = dbWhatsapp.get(contactName);
                if(!contains){
                    size =1;
                    if(list.size()>0
                            &&!(list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() -1).getText().toString()))){
                        do{
                            size++;
                        }while(size<nodeInfoList.size()&&list.size()>0&&!list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                }
                else
                {
                    size = fullUnreadMessageListWhatsapp.get(contactName);
                    if(size>nodeInfoList.size())
                        size = nodeInfoList.size();
                    else{
                        do{
                            size++;
                        }while(size<nodeInfoList.size()&&list.size()>0&&!list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                    fullUnreadMessageListWhatsapp.put(contactName,0);
                }
                for(int i = size;i>0;i--){
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - i);
                    String messageContent = nodeInfoList.get(nodeInfoList.size() - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo,this.getContext())
                            ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                    this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));
                    list.add(messageContent);
                }
                dbWhatsapp.put(contactName,list);
                break;
            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                // Get the amount of unread message
                if(!dbFacebook.containsKey(contactName)){
                    dbFacebook.put(contactName,new ArrayList<String>());
                }
                list = dbFacebook.get(contactName);
                if(!contains){
                    size =1;
                    if(list.size()>0&&!(list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() -1).getText().toString()))){
                        do{
                            size++;
                        }while(size<nodeInfoList.size()&&list.size()>0&&!list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                }
                else
                {
                    size = fullUnreadMessageListFacebook.get(contactName);
                    if(size>nodeInfoList.size())
                        size = nodeInfoList.size();
                    else{
                        do{
                            size++;
                        }while(size<nodeInfoList.size()&&list.size()>0
                                &&!list.get(list.size()-1).equals(nodeInfoList.get(nodeInfoList.size() - size).getText().toString()));
                        size--;
                    }
                    fullUnreadMessageListFacebook.put(contactName,0);
                }
                for(int i = size;i>0;i--){
                    // Put certain amount of message into the database
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - i);
                    String messageContent = nodeInfoList.get(nodeInfoList.size() - i).getText().toString();
                    String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo,this.getContext()) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
                    this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));
                    list.add(messageContent);
                }
                dbFacebook.put(contactName,list);
                break;
            }
    }
    @Override
    protected void provide() {
        getUQI().getData(BaseAccessibilityEvent.asUpdates(), Purpose.LIB_INTERNAL("Event Triggers"))
                .filter(ItemOperators.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{AppUtils.APP_PACKAGE_WHATSAPP,
                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        AccessibilityNodeInfo rootView =
                                input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        String contactName;
                        Map<String,Integer> unreadMessageList;
                        List<AccessibilityNodeInfo> nodeInfos;
                        switch (packageName){
                           case AppUtils.APP_PACKAGE_WHATSAPP:
                               int eventType = input.getValueByField(BaseAccessibilityEvent.EVENT_TYPE);
                               int item = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
                               if(eventType==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                                    if(item > 2){
                                        if(AccessibilityUtils.getMainPageSymbol(rootView,packageName)){     // Check if it is at the main page
                                            unreadMessageList= AccessibilityUtils.getUnreadMessageList(rootView,packageName);
                                            if(unreadMessageList!=null)
                                                fullUnreadMessageListWhatsapp.putAll(unreadMessageList);
                                        }
                                        else{
                                            contactName= AccessibilityUtils.getContactNameInChat(rootView,packageName);
                                            if(contactName==null) return;
                                            detContactName=contactName;
                                            nodeInfos = AccessibilityUtils.getMessageList(rootView,packageName);
                                            int eventItemCount = getEventItemCount(packageName,input);
                                            if(!contactName.equals(detContactName)){
                                                initializing(eventItemCount,packageName);;
                                            }
                                            if(nodeInfos==null || nodeInfos.size()==0){
                                                return;
                                            }
                                            int index = getFromIndex(packageName,input);
                                            if(AccessibilityUtils.getUnreadSymbol(rootView,packageName)){
                                                eventItemCount = eventItemCount-1;
                                            }
                                            if(fullUnreadMessageListWhatsapp.containsKey(contactName)&&fullUnreadMessageListWhatsapp.get(contactName)>0){
                                                saveNewMessage(nodeInfos, contactName,packageName,true);
                                            }
                                            else if((eventItemCount-lastEventItemCountWhatsapp)!=1
                                                    &&lastFromIndexWhatsapp!=0
                                                    &&(eventItemCount-lastEventItemCountWhatsapp)!=(index-lastFromIndexWhatsapp)){
                                                if((lastFromIndexWhatsapp-index)>1){
                                                    saveNewMessageScrolling(nodeInfos,contactName,packageName,eventItemCount,index);
                                                }
                                            }
                                            else if (eventItemCount - lastEventItemCountWhatsapp == 1) {
                                                saveNewMessage(nodeInfos, contactName,packageName,false);
                                            }
                                            lastEventItemCountWhatsapp = eventItemCount;
                                            lastFromIndexWhatsapp = index;
                                        }
                                    }
                                }
                               break;
                           case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                               if(!AccessibilityUtils.getMainPageSymbol(rootView,packageName)){
                                  contactName = AccessibilityUtils.getContactNameInChat(rootView,packageName);
                                   if(contactName==null) {
                                       return;
                                   }
                                   nodeInfos = AccessibilityUtils.getMessageList(rootView,packageName);
                                   if(nodeInfos==null || nodeInfos.size()==0){
                                       return;
                                   }
                                   int inputLength = AccessibilityUtils.getInputBarInputSize(rootView,packageName);
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

                    }
                });

        getUQI().getData(Notification.asUpdates(),Purpose.FEATURE("Notification Trigger"))
                                .filter(ItemOperators.isFieldIn(Notification.PACKAGE_NAME,
                                        new String[]{AppUtils.APP_PACKAGE_WHATSAPP,
                                                AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER}))
                                .filter(Comparators.eq(Notification.CATEGORY,
                                        "msg"))
                                .filter(Comparators.eq(Notification.ACTION, Notification.ACTION_POSTED))
                                .forEach(new Callback<Item>() {
                                    @Override
                                    protected void onInput(Item input) {
                                        String conName = input.getValueByField(Notification.TITLE);
                                        String packageName = input.getValueByField(Notification.PACKAGE_NAME);
                                        int num;
                                        switch (packageName){
                                            case AppUtils.APP_PACKAGE_WHATSAPP:
                                                if(fullUnreadMessageListWhatsapp.containsKey(conName)){
                                                    num = fullUnreadMessageListWhatsapp.get(conName);
                                                    fullUnreadMessageListWhatsapp.put(conName,num+1);
                                                }
                                                else{
                                                    fullUnreadMessageListWhatsapp.put(conName,1);
                                                }
                                                break;
                                            case AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER:
                                                if(fullUnreadMessageListFacebook.containsKey(conName)){
                                                    num = fullUnreadMessageListFacebook.get(conName);
                                                    fullUnreadMessageListFacebook.put(conName,num+1);
                                                }
                                                else{
                                                    fullUnreadMessageListFacebook.put(conName,1);
                                                }
                                        }
                                    }

                                });
    }

    private int getEventItemCount(String pckName, Item input){
        int temp = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
        if(pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)){
            result=temp-2;
        }else if(pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)){
            result=temp-1;
        }
        return result;
    }

    private int getFromIndex(String pckName, Item input){
        int index = input.getValueByField(BaseAccessibilityEvent.FROM_INDEX);
        if(pckName.equals(AppUtils.APP_PACKAGE_WHATSAPP)){
            result=index;
        }else if(pckName.equals(AppUtils.APP_PACKAGE_FACEBOOK_MESSENGER)){
            result=index;
        }
        return result;
    }

    private boolean initializing(int eventItemCount, String packageName) {
        try {
            switch (packageName){
                case AppUtils.APP_PACKAGE_WHATSAPP:
                    lastEventItemCountWhatsapp = eventItemCount;
                    break;
            }
            return true;        }
        catch (Exception e) {
            return false;
        }
    }
}
