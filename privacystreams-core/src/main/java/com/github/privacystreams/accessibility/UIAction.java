package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;
import com.github.privacystreams.utils.annotations.PSItem;

import java.util.Date;

/**
 * A UIAction item represents a UI action.
 */
@PSItem
public class UIAction extends BaseAccessibilityEvent {

    /**
     * The source node of current accessibility event.
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String SOURCE_NODE = "source_node";

    public UIAction(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        super(event, rootNode, timeStamp);
        this.setFieldValue(SOURCE_NODE, event.getSource());
    }

    //TODO: FOR TESTING PURPOSE ONLY
//    @Override
//    public String toString(){
//        String eventType = "";
//        int eventTypeInt = getValueByField(EVENT_TYPE);
//        switch (eventTypeInt){
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                eventType = "TYPE_VIEW_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
//                eventType = "TYPE_VIEW_LONG_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
//                eventType = "TYPE_VIEW_FOCUSED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_SELECTED:
//                eventType = "TYPE_VIEW_SELECTED";
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
//        AccessibilityNodeInfo sourceNode = getValueByField(SOURCE_NODE);
//
//        return timeStamp + " " + eventType + " " + packageName + " " + "NODE_COUNT: " + listSize + " {" + sourceNode + "}";
//
//    }

    public static Function<Void, MultiItemStream> asUpdates() {
        return new UIActionProvider();
    }

}
