package io.github.privacystreams.core.exceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An exception related to PrivacyStreams.
 */

public class PSException extends Exception {
    private int type = -1;
    private static int TYPE_PERMISSION_DENIED = 1;
    private static int TYPE_INTERRUPTED = 2;
    private static int TYPE_FAILED = 3;

    private String message = "";
    private HashSet<String> deniedPermissions = new HashSet<>();

    private PSException() {}

    public static PSException PERMISSION_DENIED(Set<String> deniedPermissions) {
        PSException e = new PSException();
        e.type = TYPE_PERMISSION_DENIED;
        e.deniedPermissions = new HashSet<>(deniedPermissions);
        return e;
    }

    public static PSException INTERRUPTED(String message) {
        PSException e = new PSException();
        e.type = TYPE_INTERRUPTED;
        e.message = message;
        return e;
    }

    public static PSException FAILED(String message) {
        PSException e = new PSException();
        e.type = TYPE_FAILED;
        e.message = message;
        return e;
    }

    public boolean isPermissionDenied() {
        return this.type == TYPE_PERMISSION_DENIED;
    }

    public boolean isInterrupted() {
        return this.type == TYPE_INTERRUPTED;
    }

    public boolean isFailed() {
        return this.type == TYPE_FAILED;
    }

    public Set<String> getDeniedPermission() {
        return this.deniedPermissions;
    }

    public String toString() {
        return this.getMessage();
    }

    public String getMessage() {
        List<String> errorMsgs = new ArrayList<>();
        if (this.isPermissionDenied()) {
            errorMsgs.add("Permission denied: " + this.deniedPermissions);
        } else if (this.isInterrupted()) {
            errorMsgs.add("Interrupted: " + this.message);
        } else if (this.isFailed()) {
            errorMsgs.add("Failed: " + this.message);
        }
        return "PrivacyStreams exception(s): " + errorMsgs;
    }
}
