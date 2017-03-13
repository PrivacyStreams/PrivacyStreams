package com.github.privacystreams.core.exceptions;

import java.util.Arrays;

/**
 * An exception which will be thrown if the required permissions are denied.
 */

public class PermissionDeniedException extends PrivacyStreamsException {
    private String[] deniedPermissions;
    public PermissionDeniedException(String[] deniedPermissions) {
        this.deniedPermissions = deniedPermissions;
    }

    public String[] getDeniedPermissions() {
        return deniedPermissions;
    }

    @Override
    public String getMessage() {
        return "Permission denied: " + Arrays.toString(deniedPermissions);
    }
}
