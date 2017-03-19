package com.github.privacystreams.accessibility;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author toby
 * @date 3/17/17
 * @time 3:17 PM
 */
public class SerializedAccessibilityNodeInfo implements Serializable {

    public SerializedAccessibilityNodeInfo(){
        children = new HashSet<>();
    }

    public String className;
    public String boundsInScreen;
    public String boundsInParent;
    public String contentDescription;
    public String text;
    public String viewId;
    public Set<SerializedAccessibilityNodeInfo> children;


    static SerializedAccessibilityNodeInfo serialize(AccessibilityNodeInfo node){
        SerializedAccessibilityNodeInfo serializedNode = new SerializedAccessibilityNodeInfo();
        Rect boundsInScreen = new Rect(), boundsInParent = new Rect();
        if(node.getClassName() != null)
            serializedNode.className = node.getClassName().toString();
        node.getBoundsInScreen(boundsInScreen);
        node.getBoundsInParent(boundsInParent);

        serializedNode.boundsInScreen = boundsInScreen.flattenToString();
        serializedNode.boundsInParent = boundsInParent.flattenToString();

        if(node.getContentDescription() != null)
            serializedNode.contentDescription = node.getContentDescription().toString();

        if(node.getText() != null){
            serializedNode.text = node.getText().toString();
        }

        if(node.getViewIdResourceName() != null)
            serializedNode.viewId = node.getViewIdResourceName();

        int childCount = node.getChildCount();
        for(int i = 0; i < childCount; i ++){
            if(node.getChild(i) != null){
                serializedNode.children.add(serialize(node.getChild(i)));
            }
        }

        return serializedNode;
    }
}
