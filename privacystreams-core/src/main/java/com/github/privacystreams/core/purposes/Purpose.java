package com.github.privacystreams.core.purposes;

/**
 * The purpose ontology of personal data use.
 */

public class Purpose {
    private String purposeString;
    private Purpose(String purposeString) {
        this.purposeString = purposeString;
    }

    private static final String PURPOSE_ADS = "Advertisement";
    private static final String PURPOSE_ANALYTICS = "Analytics";
    private static final String PURPOSE_HEALTH = "Health";
    private static final String PURPOSE_SOCIAL = "Social";
    private static final String PURPOSE_UTILITY = "Utility";
    private static final String PURPOSE_RESEARCH = "Research";
    private static final String PURPOSE_GAME = "Game";
    private static final String PURPOSE_FEATURE = "Feature";

    private static final String PURPOSE_LIB_INTERNAL = "LibInternal";
    private static final String PURPOSE_TEST = "Test";

    /**
     * Advertising purpose.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose ADS(String description) {
        return new Purpose(PURPOSE_ADS + ": " + description);
    }

    /**
     * The purpose for analytics.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose ANALYTICS(String description) {
        return new Purpose(PURPOSE_ANALYTICS + ": " + description);
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

    /**
     * The purpose for game.
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose GAME(String description) {
        return new Purpose(PURPOSE_GAME + ": " + description);
    }

    /**
     * Testing purpose.
     * App should not use this purpose in released app.
     *
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose TEST(String description) {
        return new Purpose(PURPOSE_TEST + ": " + description);
    }

    /**
     * For internal library use.
     * App should not use this purpose anyway.
     *
     * @param description a short description of the purpose.
     * @return the Purpose instance
     */
    public static Purpose LIB_INTERNAL(String description) {
        return new Purpose(PURPOSE_LIB_INTERNAL + ": " + description);
    }

    public String toString() {
        return this.purposeString;
    }
}
