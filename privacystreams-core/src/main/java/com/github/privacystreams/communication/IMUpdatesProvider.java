package com.github.privacystreams.communication;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.BaseAccessibilityEvent;
import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.AccessibilityUtils;

import java.util.List;


/**
 * Provide a live stream of messages in IM (instant messaging) apps.
 * Including WhatsApp, Facebook Messenger, etc.
 * The messages are accessed with Android Accessibility APIs.
 */
class IMUpdatesProvider extends MStreamProvider {
    private int totalNumberOfMessages=0;
    private int result=0;
    private String detPackage = "";
    private String detContactName = "";

    public static final String APP_PACKAGE_WHATSAPP = "com.whatsapp";
    public static final String APP_PACKAGE_FACEBOOK_MESSENGER = "com.facebook.orca";


    private void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName) {
        totalNumberOfMessages += 1;
        AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
        String messageContent = nodeInfoList.get(nodeInfoList.size() - 1).getText().toString();
        String messageType = AccessibilityUtils.isIncomingMessage(nodeInfo,packageName) ? Message.TYPE_RECEIVED : Message.TYPE_SENT;
        this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));
    }

    @Override
    protected void provide() {
        getUQI().getData(BaseAccessibilityEvent.asUpdates(), Purpose.LIB_INTERNAL("Event Triggers"))
                .filter(ItemOperators.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{APP_PACKAGE_WHATSAPP, APP_PACKAGE_FACEBOOK_MESSENGER}))
                .filter(Comparators.eq(BaseAccessibilityEvent.EVENT_TYPE,
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED))
                .filter(Comparators.gt(BaseAccessibilityEvent.ITEM_COUNT, 2))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        AccessibilityNodeInfo rootView =
                                input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        if(!packageName.equals(detPackage)){
                            totalNumberOfMessages=0;
                        }

                        detPackage=packageName;
                        String contactName = AccessibilityUtils.getContactNameInChat(rootView,packageName);
                        if(contactName==null) {
                            return;
                        }
                        if(!contactName.equals(detContactName)){
                            totalNumberOfMessages=0;
                        }
                        detContactName=contactName;

                        List<AccessibilityNodeInfo> nodeInfos = AccessibilityUtils.getMessageList(rootView,packageName);

                        int eventItemCount = getEventItemCount(packageName,input);
                        if(nodeInfos==null || nodeInfos.size()==0){
                            return;
                        }

                        if(totalNumberOfMessages==0){
                            initializing(eventItemCount);
                        }
                        else if (eventItemCount - totalNumberOfMessages == 1) {
                            saveNewMessage(nodeInfos, contactName,packageName);

                        }
                    }
                });

    }

    public int getEventItemCount(String pckName,  Item input){
        int temp = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
        if(pckName.equals(APP_PACKAGE_WHATSAPP)){
            result=temp-2;
        }else if(pckName.equals(APP_PACKAGE_FACEBOOK_MESSENGER)){
            result=temp-1;
        }
        return result;
    }

    public boolean initializing(int eventItemCount) {
        try {
            totalNumberOfMessages = eventItemCount;
            return true;        }
        catch (Exception e) {
            return false;
        }
    }
}