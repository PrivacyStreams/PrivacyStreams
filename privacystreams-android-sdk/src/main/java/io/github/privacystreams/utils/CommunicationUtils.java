package io.github.privacystreams.utils;

/**
 * Some communication-related utility functions
 */

public class CommunicationUtils {
    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return null;
        return phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
    }
}
