package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Deprecated.
 * Moved to AccEvent.asTextEntries.
 * A user text input action.
 */
class TextEntry extends UIAction {
    /**
     * The user-typed text content.
     */
    @PSItemField(type = String.class)
    public static final String CONTENT = "content";

    TextEntry(AccessibilityEvent event, AccessibilityNodeInfo sourceNode, String content){
        super(event, sourceNode);
        this.setFieldValue(CONTENT, content);
    }

    /**
     * Provide a live stream of TextEntry items.
     * The provider will generate a TextEntry item once the user type some text.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUpdates() {
        return new TextEntryProvider();
    }
}
