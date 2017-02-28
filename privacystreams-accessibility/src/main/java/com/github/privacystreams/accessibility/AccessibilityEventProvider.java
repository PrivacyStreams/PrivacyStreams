package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author toby
 * @date 2/28/17
 * @time 11:23 AM
 */
public class AccessibilityEventProvider extends MultiItemStreamProvider {

    private MyAccessibilityService sharedServiceInstance;
    private int eventTypeMask = 0x0;
    public static final int BASE_ACCESSIBILITY_EVENT_MASK = 0x1, USER_UI_ACTION_ACCESSIBILITY_EVENT_MASK = 0x2, USER_TEXT_ENTRY_ACCESSIBILITY_EVENT_MASK = 0x4;
    private boolean registered = false;
    public AccessibilityEventProvider(int eventTypeMask){
        this.eventTypeMask = eventTypeMask;
        sharedServiceInstance = MyAccessibilityService.getSharedInstance();

    }

    @Override
    protected void provide() {
        if(sharedServiceInstance != null) {
            sharedServiceInstance.registerProvider(this);
            registered = true;
        }
    }

    @Override
    protected void onCancelled(UQI uqi) {
        if(sharedServiceInstance != null) {
            sharedServiceInstance.unregisterProvider(this);
            registered = false;
        }
    }

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){
        if(!registered)
            return;
        if((eventTypeMask & USER_TEXT_ENTRY_ACCESSIBILITY_EVENT_MASK) > 0 && UserTextEntryAccessibilityEventItem.isATextEntryAccessibilityEventType(event)){
            output(new UserTextEntryAccessibilityEventItem(event, rootNode, timeStamp));
        }
        else if((eventTypeMask & USER_UI_ACTION_ACCESSIBILITY_EVENT_MASK) > 0 && UserUIActionAccessibilityEventItem.isAUserUIActionAccessibilityEventType(event)){
            output(new UserUIActionAccessibilityEventItem(event, rootNode, timeStamp));
        }
        else if((eventTypeMask & BASE_ACCESSIBILITY_EVENT_MASK) > 0 && BaseAccessibilityEventItem.isABaseAccessibilityEventType(event)){
            output(new BaseAccessibilityEventItem(event, rootNode, timeStamp));
        }
    }
}
