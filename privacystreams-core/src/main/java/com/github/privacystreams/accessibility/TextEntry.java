package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.Date;

/**
 * @author toby
 * @date 2/28/17
 * @time 10:54 AM
 */
public class TextEntry extends UIAction {
    public static final String CONTENT = "content";

    TextEntry(AccessibilityEvent event, AccessibilityNodeInfo sourceNode, String content, Date timeStamp){
        super(event, sourceNode, timeStamp);
        this.setFieldValue(CONTENT, content);
    }

    public static MultiItemStreamProvider asUpdates() {
        return new TextEntryProvider();
    }
}
