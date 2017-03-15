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
    private static final String PURPOSE_FEATURE = "Feature";
    private static final String PURPOSE_TEST = "Test";
    private static final String PURPOSE_HEALTH = "Health";
    private static final String PURPOSE_SOCIAL = "Social";
    private static final String PURPOSE_UTILITY = "Utility";
    private static final String PURPOSE_RESEARCH = "Research";

    /**
     * Advertising purpose.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose ADS(String description) {
        return new Purpose(PURPOSE_ADS + ": " + description);
    }

    /**
     * The purpose for app's features.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose FEATURE(String description) {
        return new Purpose(PURPOSE_FEATURE + ": " + description);
    }

    /**
     * Testing purpose.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose TEST(String description) {
        return new Purpose(PURPOSE_TEST + ": " + description);
    }

    /**
     * Utility purpose.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose UTILITY(String description) {
        return new Purpose(PURPOSE_UTILITY + ": " + description);
    }

    /**
     * The purpose for health monitoring.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose HEALTH(String description) {
        return new Purpose(PURPOSE_HEALTH + ": " + description);
    }

    /**
     * The purpose for social interaction (analyzing social relationship, recommending friends, etc.).
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose SOCIAL(String description) {
        return new Purpose(PURPOSE_SOCIAL + ": " + description);
    }

    /**
     * Research purpose.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose RESEARCH(String description) {
        return new Purpose(PURPOSE_RESEARCH + ": " + description);
    }

    // TODO modify this, distinguish INTERNAL purpose and FEATURE purpose
    private static final String PURPOSE_INTERNAL = "Internal";
    public static Purpose INTERNAL(String description) {
        return new Purpose(PURPOSE_INTERNAL + ": " + description);
    }

    // TODO more purposes

    public String toString() {
        return this.purposeString;
    }
}
