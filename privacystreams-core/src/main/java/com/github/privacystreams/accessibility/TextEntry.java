package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;
import com.github.privacystreams.utils.annotations.PSItem;

import java.util.Date;

/**
 * A TextEntry represents a piece of text typed by the user.
 */
@PSItem
public class TextEntry extends UIAction {
    @PSItemField(name="content", type = String.class, description = "The user-typed content.")
    public static final String CONTENT = "content";

    TextEntry(AccessibilityEvent event, AccessibilityNodeInfo sourceNode, String content, Date timeStamp){
        super(event, sourceNode, timeStamp);
        this.setFieldValue(CONTENT, content);
    }

    public static MultiItemStreamProvider asUpdates() {
        return new TextEntryProvider();
    }
}
