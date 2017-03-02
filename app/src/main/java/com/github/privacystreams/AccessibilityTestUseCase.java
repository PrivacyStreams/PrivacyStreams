package com.github.privacystreams;

import android.content.Context;

import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.accessibility.*;

/**
 * @author toby
 * @date 2/28/17
 * @time 3:28 PM
 */
public class AccessibilityTestUseCase {
    private UQI uqi;
    private Context context;

    public AccessibilityTestUseCase(Context context) {
        this.context = context;
        this.uqi = new UQI(context);
    }

    public void startTracking() {
        //AccessibilityEventProvider accessibilityEventProvider = new AccessibilityEventProvider(0x7);
        AccessibilityEventProvider accessibilityEventProvider = new AccessibilityEventProvider(AccessibilityEventProvider.USER_TEXT_ENTRY_ACCESSIBILITY_EVENT_MASK
                | AccessibilityEventProvider.USER_UI_ACTION_ACCESSIBILITY_EVENT_MASK | AccessibilityEventProvider.BASE_ACCESSIBILITY_EVENT_MASK);
        uqi.getDataItems(accessibilityEventProvider, Purpose.feature("test purpose")).print();
    }
}
