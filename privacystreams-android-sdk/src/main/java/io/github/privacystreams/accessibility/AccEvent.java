package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Accessibility event.
 */

public class AccEvent extends Item {
    /**
     * The accessibility event, see Android official document of [AccessibilityEvent](https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent.html) for a list of event types.
     */
    @PSItemField(type = AccessibilityEvent.class)
    public static final String EVENT = "event";

    /**
     * The time in which the event was sent.
     */
    @PSItemField(type = Long.class)
    public static final String EVENT_TIME = "event_time";

    /**
     * The root view of current event, which is an instance of AccessibilityNodeInfo.
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String ROOT_NODE = "root_node";

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
     * The source node of the UI action, only available when using `asUIActions` provider.
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String SOURCE_NODE = "source_node";

    /**
     * The user-typed text. Only available when using `asTextEntries` provider.
     */
    @PSItemField(type = String.class)
    public static final String TEXT = "text";

    AccEvent(AccessibilityEvent accessibilityEvent, AccessibilityNodeInfo rootNode) {
        this.setFieldValue(EVENT, accessibilityEvent);
        this.setFieldValue(EVENT_TIME, accessibilityEvent.getEventTime());
        this.setFieldValue(ROOT_NODE, rootNode);
        this.setFieldValue(EVENT_TYPE, accessibilityEvent.getEventType());
        CharSequence charSequence = accessibilityEvent.getPackageName();
        String packageName = charSequence == null ? "" : charSequence.toString();
        this.setFieldValue(PACKAGE_NAME, packageName);
    }

    /**
     * Provide a live stream of all accessibility items.
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUpdates() {
        return new AllAccEventProvider();
    }

    /**
     * Provide a live stream of window changing accessibility items.
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asWindowChanges() {
        return new WindowChangeEventProvider();
    }

    /**
     * Provide a live stream of UI action events.
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUIActions() {
        return new UIActionProvider();
    }

    /**
     * Provide a live stream of text entry events.
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asTextEntries() {
        return new TextEntryProvider();
    }
}
