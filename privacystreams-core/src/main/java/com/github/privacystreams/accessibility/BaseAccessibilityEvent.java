package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

import java.util.Date;

/**
 * Base Accessibility event.
 */
public class BaseAccessibilityEvent extends Item {

    @ItemField(name="timestamp", type = Long.class, description = "The timestamp of when the item is generated.")
    public static final String TIMESTAMP = "timestamp";

    @ItemField(name="event_type", type = Integer.class, description = "The type of the event, see https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent.html for a list of event types.")
    public static final String EVENT_TYPE = "event_type";

    @ItemField(name="package_name", type = String.class, description = "The package name of the current app (could be null).")
    public static final String PACKAGE_NAME = "package_name";

    @ItemField(name="root_view", type = AccessibilityNodeInfo.class, description = "The root view of current event.")
    public static final String ROOT_VIEW = "root_view";

    @ItemField(name="item_count", type = Integer.class, description = "The number of items in current event.")
    public static final String ITEM_COUNT = "item_count";

    BaseAccessibilityEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfo rootNode, Date timeStamp){
        this.setFieldValue(EVENT_TYPE, accessibilityEvent.getEventType());
        this.setFieldValue(TIMESTAMP, timeStamp);
        this.setFieldValue(PACKAGE_NAME, accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName() : "NULL");
        this.setFieldValue(ROOT_VIEW, rootNode);
        this.setFieldValue(ITEM_COUNT, accessibilityEvent.getItemCount());
    }

    public static MultiItemStreamProvider asUpdates() {
        return new BaseAccessibilityEventProvider();
    }
}
