package com.github.privacystreams.accessibility;

/**
 * An input event.
 */

class InputEvent {
    public String last;
    public String packageName;
    public int sequence;
    public int sourceHashCode;
    public String text;

    public InputEvent() {
        this.sequence = 0;
    }


}