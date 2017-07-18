package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


class AllAccEventProvider extends AccEventProvider {

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode){
        this.output(new AccEvent(event, rootNode));
    }
}
