package com.github.privacystreams.privacystreams_accessibility;

import android.accessibilityservice.AccessibilityService;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.privacystreams_accessibility.utils.AccessibilityUtils;
import com.github.privacystreams.privacystreams_accessibility.utils.AppUtils;

import java.util.List;

import static com.github.privacystreams.privacystreams_accessibility.utils.AppUtils.APP_PACKAGE_WHATSAPP;

/**
 * Created by fanglinchen on 2/1/17.
 */

public abstract class AccessibilityTrackingService extends AccessibilityService {

    protected InputEvent mEvent;

    public static String lastSavedUrl = null;
    public static String lastSavedUrlTitle = null;

    int totalNumberOfMessages = 0;
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("onServiceConnected","onServiceConnected");
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("event",event.toString());
        String pckName = (String) event.getPackageName();
        if(AppUtils.isBrowserApp(pckName)){
            BrowserEventHandling(event,pckName);
        }
        else if(AppUtils.isIMApp(pckName)){
            switch (pckName){
                case APP_PACKAGE_WHATSAPP:
                    WhatsAppEventHandling(event);
                    break;

                default:
                    break;
            }
        }
        else if(pckName.equals(AppUtils.APP_PACKAGE_SEARCHBOX)){
            SearchEventHandler(event);
        }
        else{
            Log.e("pckName",pckName);
            List<AccessibilityNodeInfo> nodeInfos =
                    AccessibilityUtils.preOrderTraverse(this.getRootInActiveWindow());
            for(AccessibilityNodeInfo nodeInfo: nodeInfos){
                if(nodeInfo.getViewIdResourceName()!=null){

                    if(nodeInfo.getViewIdResourceName().contains("EndOfSurvey")){
                        Log.e("nodeInfo",nodeInfo.getViewIdResourceName());

                    }
                }

            }
        }
    }

    protected void SearchEventHandler(AccessibilityEvent event){
        switch (event.getEventType()) {
            case TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE:
                onViewFocused(event);
                break;
            case TransportMediator.FLAG_KEY_MEDIA_PAUSE:
                onViewTextChanged(event);
                break;
            default:
                break;
        }
    }

    protected void BrowserEventHandling(AccessibilityEvent event, String appName){
        if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED){
            String url = AccessibilityUtils.getBrowserCurrentUrl(this.getRootInActiveWindow(),appName);
            String urlTitle = AccessibilityUtils.getWebViewTitle(
                    AccessibilityUtils.preOrderTraverse(this.getRootInActiveWindow()));
            if(url!=null && urlTitle!=null && !url.equals(lastSavedUrl)
                    &&!urlTitle.equals(lastSavedUrlTitle)){
//                EventBus.getDefault().post(new BrowserHistory(urlTitle,
//                        appName,
//                        url,System.currentTimeMillis()));
                Log.e("urltitle",urlTitle+"");

                lastSavedUrl = url;
                lastSavedUrlTitle = urlTitle;
            }

        }
        else{
            SearchEventHandler(event);
        }
    }

    protected void onViewFocused(AccessibilityEvent event) {

        if(mEvent!=null){

            Log.e("text",mEvent.text);
//            EventBus.getDefault().post(new BrowserSearch(mEvent.text,
//                    System.currentTimeMillis()));

        }
        this.mEvent = null;
    }

    protected void onViewTextChanged(AccessibilityEvent event) {
        List<CharSequence> text = event.getText();
        if (text.get(0).length() == 0 || event.isPassword()) {
            this.mEvent = null;

        } else {
            onNewText((CharSequence) text.get(0), event);
        }
    }

    protected void onNewText(CharSequence text, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        int hashCode = -1;
        if (src != null) {
            hashCode = src.hashCode();
            src.recycle();
        }

        if (this.mEvent != null
                && this.mEvent.sourceHashCode == hashCode) {
            this.mEvent.text = text.toString();
        }
        else{
            beginEvent(String.valueOf(event.getPackageName()), hashCode, text);
        }
    }

    private void beginEvent(String packageName, int hashCode, CharSequence text) {

        InputEvent event = new InputEvent();
        event.packageName = packageName;
        event.sourceHashCode = hashCode;
        event.text = text.toString();
        this.mEvent = event;
    }


    public void saveNewMessage(List<AccessibilityNodeInfo> nodeInfoList, String contactName) {
        totalNumberOfMessages += 1;
        AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
        Log.e("message",nodeInfoList.get(nodeInfoList.size() - 1).getText().toString());
//        EventBus.getDefault().post(new Message(AccessibilityUtils.isIncomingMessage(nodeInfo),
//                nodeInfoList.get(nodeInfoList.size() - 1).getText().toString(),
//                APP_PACKAGE_WHATSAPP,
//                contactName,
//                System.currentTimeMillis()
//        ));

    }

    public boolean initializing(int eventItemCount) {
        try {
            totalNumberOfMessages = eventItemCount;
            return true;
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return false;
        }
    }


    protected void WhatsAppEventHandling(AccessibilityEvent event){
        String tmpContactName = AccessibilityUtils.getContactNameInChat(getRootInActiveWindow(),APP_PACKAGE_WHATSAPP);
        AccessibilityNodeInfo textBox = AccessibilityUtils.getTextBox(getRootInActiveWindow(),APP_PACKAGE_WHATSAPP);

        int eventItemCount = event.getItemCount()-2;

        if(textBox==null
                ||tmpContactName==null
                ||eventItemCount < 0)
            return;

        if(event.getEventType()!=AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                &&!(event.getEventType()==AccessibilityEvent.TYPE_VIEW_SCROLLED
        ))
            return;


        List<AccessibilityNodeInfo> tmpNodeInfos =
                AccessibilityUtils.getMessageList(getRootInActiveWindow(),APP_PACKAGE_WHATSAPP);

        //eventItemCount could change and from/to index may change
        if(totalNumberOfMessages==0){
            initializing(eventItemCount);
        }
        else if(eventItemCount-totalNumberOfMessages==1){
            saveNewMessage(tmpNodeInfos,tmpContactName);
        }
    }

    @Override
    public void onInterrupt() {

    }
}