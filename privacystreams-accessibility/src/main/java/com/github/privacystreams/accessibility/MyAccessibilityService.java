package com.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuanchun on 26/02/2017.
 * Test accessibility service
 */

public class MyAccessibilityService extends AccessibilityService {

    public Set<AccessibilityEventProvider> accessibilityEventProviders = new HashSet<>();


    private static MyAccessibilityService sharedServiceInstance;

    @Override
    protected void onServiceConnected() {
        sharedServiceInstance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //Logging.debug("Accessibility event: " + accessibilityEvent);
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        for(AccessibilityEventProvider provider : accessibilityEventProviders){
            provider.handleAccessibilityEvent(accessibilityEvent, rootNode, new Date());
        }
    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public boolean onUnbind(Intent intent) {
        sharedServiceInstance = null;
        return super.onUnbind(intent);
    }

    public static MyAccessibilityService getSharedInstance() {
        return sharedServiceInstance;
    }

    /**
     *
     * @param provider
     */
    public void registerProvider(AccessibilityEventProvider provider){
        accessibilityEventProviders.add(provider);
    }

    public void unregisterProvider(AccessibilityEventProvider provider){
        accessibilityEventProviders.remove(provider);
    }
}
