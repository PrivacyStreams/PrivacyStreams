package com.github.privacystreams.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.github.privacystreams.core.utils.Logging;

/**
 * Created by yuanchun on 26/02/2017.
 * Test accessibility service
 */

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Logging.debug("Accessibility event: " + accessibilityEvent);
    }

    @Override
    public void onInterrupt() {

    }
}
