package com.github.privacystreams.communication;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

public class InstantMessage extends Message{
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
                   String packageName,
                   String contact,
                   long timestamp,
                   int[] position,
                   AccessibilityNodeInfo rootNode){
        super(type, content, packageName, contact, timestamp);
        this.setFieldValue(POSITION, position);
        this.setFieldValue(ROOT_NODE, rootNode);
    }

    InstantMessage(String type,
                   String content,
                   String packageName,
                   String contact,
                   long timestamp) {
        super(type, content, packageName, contact, timestamp);
    }

    /**
     * Provide a live stream of new Message items in Instant Messenger (IM) apps, including WhatsApp and Facebook.
     * This provider requires Accessibility service turned on.
     *
     * @return the provider function
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static MStreamProvider asUpdatesInIM(){
        return new IMUIUpdatesProvider();
    }

}
