package io.github.privacystreams.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Provide a live stream of TextEntry items.
 */
class TextEntryProvider extends AccEventProvider {
    private InputEvent mEvent;
    private String textContent;

    private void onViewFocused(AccessibilityEvent event, AccessibilityNodeInfo rootNode) {

        if(mEvent != null && event != null) {
            // Store Text Input.
            AccEvent accEvent = new AccEvent(event, rootNode);
            accEvent.setFieldValue(AccEvent.TEXT, mEvent.text);
            this.output(accEvent);
        }
        this.mEvent = null;
    }

    private void onViewTextChanged(AccessibilityEvent event) {
        List<CharSequence> text = event.getText();
        if (text == null
                || text.size()==0
                || text.get(0).length() == 0
                || event.isPassword()) {
            this.mEvent = null;

        } else {
            onNewText(text.get(0), event);
        }
    }

    private void onNewText(CharSequence text, AccessibilityEvent event) {
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

    public void handleAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootNode){

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                onViewFocused(event, rootNode);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                onViewTextChanged(event);
                break;
            default:
                break;
        }

    }

    private class InputEvent {
        public String last;
        public String packageName;
        public int sequence;
        public int sourceHashCode;
        public String text;
        public InputEvent() {
            this.sequence = 0;
        }
    }
}
