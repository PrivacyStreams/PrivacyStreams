package com.github.privacystreams.core.exceptions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An exception related to PrivacyStreams.
 */

public class PSException extends Exception {
    private int type = -1;
    public static int TYPE_PERMISSION_DENIED = 1;
    public static int TYPE_INTERRUPTED = 2;

    private String interruptMessage = "";
    private String[] deniedPermissions = new String[]{};

    private PSException() {}

    public static PSException PERMISSION_DENIED(String[] deniedPermissions) {
        PSException e = new PSException();
        e.deniedPermissions = deniedPermissions;
        return e;
    }

    public static PSException INTERRUPTED(String message) {
        PSException e = new PSException();
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
