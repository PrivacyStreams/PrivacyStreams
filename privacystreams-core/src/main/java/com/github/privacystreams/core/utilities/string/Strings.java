package com.github.privacystreams.core.utilities.string;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.HashUtils;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access string-related functions
 */

public class Strings {
    /**
     * A function that calculate the sentiment score of the string value of a field in an item.
     * The sentiment score is a double from -1.0 to 1.0, and higher sentiment score means more positive.
     * @param stringField the name of the string field to calculate sentiment.
     * @return the function.
     */
    public static Function<Item, Double> sentiment(String stringField) {
        return new StringSentimentCalculator(stringField);
    }

    /**
     * A function that gets the MD5-hashed string of a field in an item.
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> md5(String stringField) {
        return new StringHashFunction(stringField, HashUtils.MD5);
    }

    /**
     * A function that gets the SHA1-hashed string of a field in an item.
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> sha1(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA1);
    }

    /**
     * A function that gets the SHA256-hashed string of a field in an item.
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> sha256(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA256);
    }
}
