package com.github.privacystreams.core.exceptions;

import java.util.Arrays;

/**
 * Created by yuanchun on 25/02/2017.
 * PrivacyStreamsException happens if the required permissions are denied
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
