package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;
import com.github.privacystreams.utils.annotations.PSItem;

import java.util.Date;

/**
 * User input text.
 */
@PSItem
public class TextEntry extends UIAction {
    /**
     * The user-typed text content.
     */
    @PSItemField(type = String.class)
    public static final String CONTENT = "content";

    TextEntry(AccessibilityEvent event, AccessibilityNodeInfo sourceNode, String content, Date timeStamp){
        super(event, sourceNode, timeStamp);
        this.setFieldValue(CONTENT, content);
    }

    /**
     * Provide a live stream of TextEntry items.
     * @return the provider function.
     */
    public static Function<Void, MultiItemStream> asUpdates() {
        return new TextEntryProvider();
    }
}
