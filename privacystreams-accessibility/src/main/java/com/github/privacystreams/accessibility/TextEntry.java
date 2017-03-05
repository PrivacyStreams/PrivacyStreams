package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.Date;

/**
 * @author toby
 * @date 2/28/17
 * @time 10:54 AM
 */
public class TextEntry extends UIAction {
    public static final String BEFORE_TEXT = "before_text";
    public static final String AFTER_TEXT = "after_text";

    TextEntry(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        super(event, rootNode, timeStamp);
        this.setFieldValue(BEFORE_TEXT, event.getBeforeText() != null ? event.getBeforeText() : "NULL");
        this.setFieldValue(AFTER_TEXT, event.getSource() != null ? event.getSource().getText() : "NULL");
    }

    //TODO: FOR TESTING PURPOSE ONLY
//    @Override
//    public String toString(){
//        String eventType = "";
//        int eventTypeInt = getValueByField(EVENT_TYPE);
//        switch (eventTypeInt){
//            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
//                eventType = "TYPE_VIEW_TEXT_CHANGED";
//                break;
//        }
//
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
//        String timeStamp = format.format(getValueByField(TIMESTAMP));
//
//        String packageName = getValueByField(PACKAGE_NAME).toString();
//
//        String listSize = String.valueOf(((List<AccessibilityNodeInfo>)getValueByField(UI_NODE_LIST)).size());
//
//        return timeStamp + " " + eventType + " " + packageName + " " + "NODE_COUNT: " + listSize + " BEFORE_TEXT:\"" + getValueByField(BEFORE_TEXT) + "\"  AFTER_TEXT:\"" + getValueByField(AFTER_TEXT) + "\" {" + getValueByField(SOURCE_NODE) + "}";
//
//    }

    public static MultiItemStreamProvider asUpdates() {
        return new TextEntryProvider();
    }
}
