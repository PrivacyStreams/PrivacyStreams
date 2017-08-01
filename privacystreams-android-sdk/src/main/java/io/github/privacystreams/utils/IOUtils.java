package io.github.privacystreams.utils;

import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.audio.AudioData;
import io.github.privacystreams.image.ImageData;
import io.github.privacystreams.location.LatLon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of io-related utility functions.
 */

public class IOUtils {

    public static Object serialize(Object object) {
        if (object == null) return null;

        if (object instanceof Number) {
            return object;
        }
        else if (object instanceof String) {
            return object;
        }
        else if (object instanceof CharSequence) {
            return object.toString();
        }
        else if (object instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object obj : (List) object) {
                result.add(serialize(obj));
            }
            return result;
        }
        else if (object instanceof Map) {
            Map<Object, Object> result = new HashMap<>();
            for (Object key : ((Map) object).keySet()) {
                Object value = ((Map) object).get(key);
                result.put(serialize(key), serialize(value));
            }
            return result;
        }
        else if (object instanceof LatLon) {
            Map<String, Double> result = new HashMap<>();
            result.put("lat", ((LatLon) object).getLatitude());
            result.put("lon", ((LatLon) object).getLongitude());
            return result;
        }
        else if (object instanceof AccessibilityEvent) {
            Map<String, Object> result = new HashMap<>();
            AccessibilityEvent event = (AccessibilityEvent) object;
            result.put("event_type", AccessibilityEvent.eventTypeToString(event.getEventType()));
            result.put("event_time", event.getEventTime());
            result.put("package_name", serialize(event.getPackageName()));
            result.put("action", event.getAction());
            result.put("class_name", serialize(event.getClassName()));
            result.put("text", serialize(event.getText()));
            result.put("content_description", serialize(event.getContentDescription()));
            result.put("item_count", event.getItemCount());
            result.put("current_item_index", event.getCurrentItemIndex());
            result.put("enabled", event.isEnabled());
            result.put("is_password", event.isPassword());
            result.put("checked", event.isChecked());
            result.put("full_screen", event.isFullScreen());
            result.put("scrollable", event.isScrollable());
            result.put("before_text", serialize(event.getBeforeText()));
            result.put("from_index", event.getFromIndex());
            result.put("to_index", event.getToIndex());
            result.put("scroll_x", event.getScrollX());
            result.put("scroll_y", event.getScrollY());
            result.put("max_scroll_x", event.getMaxScrollX());
            result.put("max_scroll_y", event.getMaxScrollY());
            result.put("removed_count", event.getRemovedCount());
            try {
                AccessibilityNodeInfo sourceNode = event.getSource();
                result.put("source_node", serialize(sourceNode));
            }
            catch (IllegalStateException ignored) {}
            return result;
        }
        else if (object instanceof AccessibilityNodeInfo) {
            Map<String, Object> result = new HashMap<>();
            AccessibilityNodeInfo nodeInfo = (AccessibilityNodeInfo) object;
            result.put("package", serialize(nodeInfo.getPackageName()));
            result.put("text", serialize(nodeInfo.getText()));
            result.put("class", serialize(nodeInfo.getClassName()));
            result.put("child_count", nodeInfo.getChildCount());
            result.put("content_description", serialize(nodeInfo.getContentDescription()));
            result.put("enabled", nodeInfo.isEnabled());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                result.put("resource_id", nodeInfo.getViewIdResourceName());
                result.put("editable", nodeInfo.isEditable());
            }
            result.put("scrollable", nodeInfo.isScrollable());
            result.put("is_password", nodeInfo.isPassword());

            result.put("checkable", nodeInfo.isCheckable());
            result.put("checked", nodeInfo.isChecked());

            result.put("clickable", nodeInfo.isClickable());

            result.put("focusable", nodeInfo.isFocusable());
            result.put("focused", nodeInfo.isFocused());
            result.put("visible", nodeInfo.isVisibleToUser());
            result.put("long_clickable", nodeInfo.isLongClickable());
            result.put("selected", nodeInfo.isSelected());

            Rect boundsInScreen = new Rect();
            nodeInfo.getBoundsInScreen(boundsInScreen);
            result.put("bounds", serialize(boundsInScreen));
//            Rect boundsInParent = new Rect();
//            nodeInfo.getBoundsInParent(boundsInParent);
//            result.put("bounds_in_parent", serialize(boundsInParent));

            List<Object> childNodes = new ArrayList<>();
            for(int i = 0; i < nodeInfo.getChildCount(); i ++){
                AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
                if(childNode != null){
                    childNodes.add(serialize(childNode));
                }
            }
            result.put("children", childNodes);

            return result;
        }
        else if (object instanceof Rect) {
            Rect rect = (Rect) object;
            return new int[]{rect.left, rect.top, rect.right, rect.bottom};
        }
        else if (object instanceof ImageData) {
            // pass
        }
        else if (object instanceof AudioData) {
            // pass
        }
        return object.toString();
    }
}
