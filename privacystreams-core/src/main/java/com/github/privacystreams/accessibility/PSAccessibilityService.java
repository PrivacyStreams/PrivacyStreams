package com.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PrivacyStreams accessibility service
 */
public class PSAccessibilityService extends AccessibilityService {
    private static final String TAG = "PSAccessibilityService";
    public static boolean enabled = false;

    @Override
    protected void onServiceConnected() {
        enabled = true;
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        enabled = false;
        return super.onUnbind(intent);
    }

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
