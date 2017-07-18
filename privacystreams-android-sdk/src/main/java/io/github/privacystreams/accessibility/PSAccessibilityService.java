package io.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

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

    private static Set<AccEventProvider> accEventProviders = new HashSet<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent == null) return;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        for(AccEventProvider provider : accEventProviders){
            provider.handleAccessibilityEvent(accessibilityEvent, rootNode);
        }
    }

    @Override
    public void onInterrupt() {
    }

    static void registerProvider(AccEventProvider provider){
        if (provider != null)
            accEventProviders.add(provider);
    }

    static void unregisterProvider(AccEventProvider provider){
        if (provider != null && accEventProviders.contains(provider))
            accEventProviders.remove(provider);
    }
}
