package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;


class BaseAccessibilityEventProvider extends AccessibilityEventProvider {

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        int eventType = event.getEventType();
//        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
//                || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                || eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED
//                || eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED)
        {
            this.output(new BaseAccessibilityEvent(event, rootNode, timeStamp));

        }

    }
}
