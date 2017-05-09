package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.PermissionUtils;

import java.util.Date;

/**
 * The abstract class of Accessibility-related stream providers.
 */
abstract class AccessibilityEventProvider extends MStreamProvider {

    private boolean registered = false;

    AccessibilityEventProvider() {
        this.addRequiredPermissions(PermissionUtils.USE_ACCESSIBILITY_SERVICE);
    }

    @Override
    protected void provide() {
        PSAccessibilityService.registerProvider(this);
        registered = true;
    }

    @Override
    protected void onCancel(UQI uqi) {
        PSAccessibilityService.unregisterProvider(this);
        registered = false;
    }

    public abstract void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp);
}
