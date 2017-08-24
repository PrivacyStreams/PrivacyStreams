package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Deprecated.
 * Moved to AccEvent.asUIActions.
 * A UI action, such as a view is clicked, selected, etc.
 */
class UIAction extends BaseAccessibilityEvent {

    /**
     * The source node of the UI action, which is an instance of [AccessibilityNodeInfo](https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo.html).
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String SOURCE_NODE = "source_node";

    public UIAction(AccessibilityEvent event, AccessibilityNodeInfo rootNode) {
        super(event, rootNode);
        this.setFieldValue(SOURCE_NODE, event.getSource());
    }

    /**
     * Provide a live stream of UIAction items.
     * A UIAction item will be generated once there is a UI action happened.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUpdates() {
        return new UIActionProvider();
    }

}
