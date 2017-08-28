package io.github.privacystreams.communication.message;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class InstantMessage extends Message {
    /**
     * The current position of this message
     * represented by a long[] with left, top, right, bottom
     */
    @PSItemField(type = List.class)
    public static final String POSITION = "position";

    /**
     * The root view of the page where this message comes from.
     */
    @PSItemField(type = AccessibilityNodeInfo.class)
    public static final String ROOT_NODE = "rootNode";

    public InstantMessage(String type,
                          String content,
                          int logTime,
                          String packageName,
                          String contact,
                          long timestamp,
                          int[] position,
                          AccessibilityNodeInfo rootNode) {
        super(type, content, logTime, packageName, contact, timestamp);
        this.setFieldValue(POSITION, position);
        this.setFieldValue(ROOT_NODE, rootNode);
    }

    InstantMessage(String type,
                   String content,
                   int logTime,
                   String packageName,
                   String contact,
                   long timestamp) {
        super(type, content, logTime, packageName, contact, timestamp);
    }

    /**
     * Provide a live stream of new Message items in Instant Messenger (IM) apps, including WhatsApp and Facebook.
     * This provider requires Accessibility service turned on.
     *
     * @return the provider function
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static PStreamProvider asUpdatesInIM() {
        return new IMUIUpdatesProvider();
    }

}
