package com.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;
import java.util.List;

/**
 * Provide a live stream of TextEntry items.
 */
class TextEntryProvider extends AccessibilityEventProvider {
    protected InputEvent mEvent;

    protected void onViewFocused(AccessibilityEvent event) {

        if(mEvent != null && event != null){
            //Store Text Input.
            this.output(new TextEntry(event, event.getSource(), mEvent.text, new Date(System.currentTimeMillis())));
        }
        this.mEvent = null;

    }

    protected void onViewTextChanged(AccessibilityEvent event) {
        List<CharSequence> text = event.getText();
        if (text == null
                || text.size()==0
                || text.get(0).length() == 0
                || event.isPassword()) {
            this.mEvent = null;

        } else {
            onNewText((CharSequence) text.get(0), event);
        }
    }

    protected void onNewText(CharSequence text, AccessibilityEvent event) {
        AccessibilityNodeInfo src = event.getSource();
        int hashCode = -1;
        if (src != null) {
            hashCode = src.hashCode();
            src.recycle();
        }

        if (this.mEvent != null
                && this.mEvent.sourceHashCode == hashCode) {
            this.mEvent.text = text.toString();
        }
        else{
            beginEvent(String.valueOf(event.getPackageName()), hashCode, text);
        }
    }

    private void beginEvent(String packageName, int hashCode, CharSequence text) {

        InputEvent event = new InputEvent();
        event.packageName = packageName;
        event.sourceHashCode = hashCode;
        event.text = text.toString();
        this.mEvent = event;
    }

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode, Date timeStamp){

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                onViewFocused(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                onViewTextChanged(event);
                break;
            default:
                break;
        }

    }
}
