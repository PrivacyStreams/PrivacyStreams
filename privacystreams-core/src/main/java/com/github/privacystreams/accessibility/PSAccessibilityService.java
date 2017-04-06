package com.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PrivacyStreams accessibility service
 */
public class PSAccessibilityService extends AccessibilityService {

    private static Set<AccessibilityEventProvider> accessibilityEventProviders = new HashSet<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent == null) return;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        for(AccessibilityEventProvider provider : accessibilityEventProviders){
            provider.handleAccessibilityEvent(accessibilityEvent, rootNode, new Date());
        }
    }

    @Override
    public void onInterrupt() {
    }

    static void registerProvider(AccessibilityEventProvider provider){
        if (provider != null)
            accessibilityEventProviders.add(provider);
    }

    static void unregisterProvider(AccessibilityEventProvider provider){
        if (provider != null && accessibilityEventProviders.contains(provider))
            accessibilityEventProviders.remove(provider);
    }
}
