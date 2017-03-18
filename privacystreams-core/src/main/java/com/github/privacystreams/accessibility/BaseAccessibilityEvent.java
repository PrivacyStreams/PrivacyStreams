package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.Date;

/**
 * Base Accessibility event.
 */
@PSItem
public class BaseAccessibilityEvent extends Item {
    /**
     * The timestamp of when the item is generated.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The type of the event, see Android official document of [AccessibilityEvent](https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent.html) for a list of event types.
     */
    @PSItemField(type = Integer.class)
    public static final String EVENT_TYPE = "event_type";

    /**
     * The package name of the current app (could be null).
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The root view of current event, which is an instance of SerializedAccessibilityNodeInfo
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String ROOT_VIEW = "root_view";

    /**
     * The number of items in current event.
     */
    @PSItemField(type = Integer.class)
    public static final String ITEM_COUNT = "item_count";

    BaseAccessibilityEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfo rootNode, Date timeStamp){
        this.setFieldValue(EVENT_TYPE, accessibilityEvent.getEventType());
        this.setFieldValue(TIMESTAMP, timeStamp);
        this.setFieldValue(PACKAGE_NAME, accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName() : "NULL");
        this.setFieldValue(ROOT_VIEW, SerializedAccessibilityNodeInfo.serialize(rootNode));
        this.setFieldValue(ITEM_COUNT, accessibilityEvent.getItemCount());
    }

    /**
     * Provide a live stream of BaseAccessibilityEvent items.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates() {
        return new BaseAccessibilityEventProvider();
    }
}
