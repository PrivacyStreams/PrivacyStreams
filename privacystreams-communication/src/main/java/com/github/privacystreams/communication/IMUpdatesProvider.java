package com.github.privacystreams.communication;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.BaseAccessibilityEvent;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.commons.comparison.Comparisons;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;


import java.util.List;



/**
 * Created by fanglinchen on 1/31/17.
 */

public class IMUpdatesProvider extends MultiItemStreamProvider {
    MultiItemStream mStream;
    private int totalNumberOfMessages=0;
    private static final String OPERATOR = "$message_updates";

    public static final String APP_PACKAGE_WHATSAPP = "com.whatsapp";


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
                Purpose.feature("Event Triggers"))
                .filter(Comparisons.eq(BaseAccessibilityEvent.PACKAGE_NAME, APP_PACKAGE_WHATSAPP))
                .filter(Comparisons.eq(BaseAccessibilityEvent.EVENT_TYPE, AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED))
                .filter(Comparisons.gt(BaseAccessibilityEvent.ITEM_COUNT, 2))
                .forEach(new Callback<Item>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    protected void onSuccess(Item input) {
                        AccessibilityNodeInfo rootView =
                                input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        List<AccessibilityNodeInfo> nodeInfos =
                                input.getValueByField(BaseAccessibilityEvent.UI_NODE_LIST);
                        String contactName = AccessibilityUtils
                                .getContactNameInChat(rootView,APP_PACKAGE_WHATSAPP);

                        AccessibilityNodeInfo textBox = AccessibilityUtils
                                .getTextBox(rootView, APP_PACKAGE_WHATSAPP);
                        int eventItemCount = input.getValueByField(BaseAccessibilityEvent.ITEM_COUNT);
                        eventItemCount-=2;
                        if(textBox==null){
                            return;
                        }
                        if(totalNumberOfMessages==0){
                            initializing(eventItemCount);
                        }
                        else if (eventItemCount - totalNumberOfMessages == 1) {
                            saveNewMessage(nodeInfos, contactName,APP_PACKAGE_WHATSAPP);
                        }
//                        List<AccessibilityNodeInfo> nodeInfos =
//                                input.getValueByField(BaseAccessibilityEvent.UI_NODE_LIST);
//                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
//                        String url = AccessibilityUtils
//                                .getBrowserCurrentUrl(rootView,packageName);
////                         String urlTitle = AccessibilityUtils
////                                .getWebViewTitle(nodeInfos);
                    }
                });

    }
    public boolean initializing(int eventItemCount) {
        try {
            totalNumberOfMessages = eventItemCount;
            return true;        }
        catch (Exception e) {
            Log.e("Exception", e.toString());
            return false;
        }
    }
}
