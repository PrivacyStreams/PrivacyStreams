package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Provide a live stream of UIAction items.
 */
class UIActionProvider extends AccEventProvider {

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode){
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED
                || eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED
                || eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED
                || eventType == AccessibilityEvent.TYPE_VIEW_SELECTED) {
            AccEvent accEvent = new AccEvent(event, rootNode);
            accEvent.setFieldValue(AccEvent.SOURCE_NODE, event.getSource());
            this.output(accEvent);
        }
    }
}
