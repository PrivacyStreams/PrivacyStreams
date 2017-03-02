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
abstract class AccessibilityEventProvider extends MultiItemStreamProvider {

    private boolean registered = false;

    @Override
    protected void provide() {
        MyAccessibilityService sharedServiceInstance = MyAccessibilityService.getSharedInstance();
        if(sharedServiceInstance != null) {
            sharedServiceInstance.registerProvider(this);
            registered = true;
        }
    }

    @Override
    protected void onCancelled(UQI uqi) {
        MyAccessibilityService sharedServiceInstance = MyAccessibilityService.getSharedInstance();
        if(sharedServiceInstance != null) {
            sharedServiceInstance.unregisterProvider(this);
            registered = false;
        }
    }

    public abstract void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp);
}
