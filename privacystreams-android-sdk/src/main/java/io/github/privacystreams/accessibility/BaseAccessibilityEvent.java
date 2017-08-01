package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Deprecated.
 * Moved to AccEvent.asWindowChanges.
 * Base Accessibility event.
 */
class BaseAccessibilityEvent extends Item {
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
     * The root view of current event, which is an instance of AccessibilityNodeInfo.
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String ROOT_NODE = "root_node";

    BaseAccessibilityEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfo rootNode) {
        this.setFieldValue(EVENT_TYPE, accessibilityEvent.getEventType());
        this.setFieldValue(PACKAGE_NAME, accessibilityEvent.getPackageName());
        this.setFieldValue(ROOT_NODE, rootNode);
    }

    /**
     * Provide a live stream of BaseAccessibilityEvent items.
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUpdates() {
        return new WindowChangeEventProvider();
    }
}
