package com.github.privacystreams.core.purposes;

/**
 * Created by yuanchun on 21/12/2016.
 * The purpose of personal data use.
 */

public class Purpose {
    private String purposeString;
    private Purpose(String purposeString) {
        this.purposeString = purposeString;
    }

    private static final String PURPOSE_ADS = "Advertisement";
    public static Purpose ads(String description) {
        return new Purpose(PURPOSE_ADS + ": " + description);
    }

    private static final String PURPOSE_FEATURE = "Feature";
    public static Purpose feature(String description) {
        return new Purpose(PURPOSE_FEATURE + ": " + description);
    }

    private static final String PURPOSE_INTERNAL = "Internal";
    public static Purpose internal(String description) {
        return new Purpose(PURPOSE_INTERNAL + ": " + description);
    }

    private static final String PURPOSE_TEST = "Test";
    public static Purpose test(String description) {
        return new Purpose(PURPOSE_TEST + ": " + description);
    }

    // TODO more purposes

    public String toString() {
        return this.purposeString;
    }
}
