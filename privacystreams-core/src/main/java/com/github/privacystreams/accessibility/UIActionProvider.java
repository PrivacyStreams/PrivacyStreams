package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;

/**
 * Provide a live stream of UIAction items.
 */
class UIActionProvider extends AccessibilityEventProvider {

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED
                || eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED
                || eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED
                || eventType == AccessibilityEvent.TYPE_VIEW_SELECTED){
            this.output(new UIAction(event, rootNode, timeStamp));
        }

    }
}
