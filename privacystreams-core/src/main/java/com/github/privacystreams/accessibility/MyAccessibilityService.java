package com.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yuanchun on 26/02/2017.
 * Test accessibility service
 */
public class MyAccessibilityService extends AccessibilityService {

    private static Set<AccessibilityEventProvider> accessibilityEventProviders = new HashSet<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        for(AccessibilityEventProvider provider : accessibilityEventProviders){
            provider.handleAccessibilityEvent(accessibilityEvent, rootNode, new Date());
        }
    }

    @Override
    public void onInterrupt() {
    }

    static void registerProvider(AccessibilityEventProvider provider){
        accessibilityEventProviders.add(provider);
    }

    static void unregisterProvider(AccessibilityEventProvider provider){
        accessibilityEventProviders.remove(provider);
    }
}
