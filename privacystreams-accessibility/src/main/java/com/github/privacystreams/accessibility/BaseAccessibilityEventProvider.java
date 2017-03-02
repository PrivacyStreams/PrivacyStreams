package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.Date;

/**
 * @author toby
 * @date 2/28/17
 * @time 11:23 AM
 */
class BaseAccessibilityEventProvider extends AccessibilityEventProvider {

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED)
            this.output(new BaseAccessibilityEvent(event, rootNode, timeStamp));
    }
}
