package com.github.privacystreams.core.exceptions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An exception related to PrivacyStreams.
 */

public class PrivacyStreamsException extends Exception {
    private int type = -1;
    public static int TYPE_PERMISSION_DENIED = 1;
    public static int TYPE_INTERRUPTED = 2;

    private String interruptMessage = "";
    private String[] deniedPermissions = new String[]{};

    private PrivacyStreamsException() {}

    public static PrivacyStreamsException PERMISSION_DENIED(String[] deniedPermissions) {
        PrivacyStreamsException e = new PrivacyStreamsException();
        e.deniedPermissions = deniedPermissions;
        return e;
    }

    public static PrivacyStreamsException INTERRUPTED(String message) {
        PrivacyStreamsException e = new PrivacyStreamsException();
        e.interruptMessage = message;
        return e;
    }

    public boolean isPermissionDenied() {
        return this.type == TYPE_PERMISSION_DENIED;
    }

    public boolean isInterrupted() {
        return this.type == TYPE_INTERRUPTED;
    }

    public String toString() {
        return this.getMessage();
    }

    public String getMessage() {
        List<String> errorMsgs = new ArrayList<>();
        if (this.isPermissionDenied()) {
            errorMsgs.add("Permission denied: " + Arrays.asList(this.deniedPermissions));
        }
        if (this.isInterrupted()) {
            errorMsgs.add("Interrupted: " + this.interruptMessage);
        }
        return StringUtils.join(errorMsgs, "\n");
    }
}
