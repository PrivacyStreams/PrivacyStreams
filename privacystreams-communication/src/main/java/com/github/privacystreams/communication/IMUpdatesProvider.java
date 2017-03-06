package com.github.privacystreams.communication;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.BaseAccessibilityEvent;
import com.github.privacystreams.accessibility.utils.AccessibilityUtils;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.commons.common.ItemCommons;
import com.github.privacystreams.core.commons.comparison.Comparisons;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.List;


/**
 * Created by fanglinchen on 1/31/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class IMUpdatesProvider extends MultiItemStreamProvider {
    private int totalNumberOfMessages=0;
    private int result=0;
    private String detPackage="";

    public static final String APP_PACKAGE_WHATSAPP = "com.whatsapp";
    public static final String APP_PACKAGE_FACEBOOK_MESSENGER = "com.facebook.orca";


    private void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList, String contactName, String packageName) {
        totalNumberOfMessages += 1;
        AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
        String messageContent = nodeInfoList.get(nodeInfoList.size() - 1).getText().toString();
        Message.Type messageType = AccessibilityUtils.isIncomingMessage(nodeInfo,packageName) ? Message.Type.RECEIVED : Message.Type.SENT;
        this.output(new Message(messageType,messageContent,packageName,contactName,System.currentTimeMillis()));

    }
    @Override
    protected void provide() {

        getUQI().getDataItems(BaseAccessibilityEvent.asUpdates(),
                Purpose.internal("Event Triggers"))
                .filter(ItemCommons.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{APP_PACKAGE_WHATSAPP, APP_PACKAGE_FACEBOOK_MESSENGER}))
                .filter(Comparisons.eq(BaseAccessibilityEvent.EVENT_TYPE, AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED))
                .filter(Comparisons.gt(BaseAccessibilityEvent.ITEM_COUNT, 2))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onSuccess(Item input) {

                        AccessibilityNodeInfo rootView =
                                input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        if(!packageName.equals(detPackage)){
                            totalNumberOfMessages=0;
                        }
                        detPackage=packageName;
                        List<AccessibilityNodeInfo> nodeInfos =
                                AccessibilityUtils.getMessageList(rootView,packageName);
                        String contactName = AccessibilityUtils
                                .getContactNameInChat(rootView,packageName);

                        AccessibilityNodeInfo textBox = AccessibilityUtils
                                .getTextBox(rootView, packageName);

                        int eventItemCount = getEventItemCount(packageName,input);
                        if(textBox==null || nodeInfos==null || nodeInfos.size()==0){
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