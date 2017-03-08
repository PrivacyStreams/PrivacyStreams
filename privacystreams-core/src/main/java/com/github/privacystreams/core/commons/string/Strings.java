package com.github.privacystreams.core.commons.string;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.HashUtils;

/**
 * A helper class to access string-related functions
 */

public class Strings {

    /**
     * A function that checks whether a string field in the item contains a certain substring.
     *
     * @param stringField the name of the string field
     * @param searchString the substring to search
     * @return the function.
     */
    public static Function<Item, Boolean> contains(String stringField, String searchString) {
        return new StringContainFunction(stringField, searchString);
    }

    /**
     * A function that gets the MD5-hashed string of a field in an item.
     *
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> md5(String stringField) {
        return new StringHashFunction(stringField, HashUtils.MD5);
    }

    /**
     * A function that gets the SHA1-hashed string of a field in an item.
     *
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> sha1(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA1);
    }

    /**
     * A function that gets the SHA256-hashed string of a field in an item.
     *
     * @param stringField the name of the string field to hash.
     * @return the function.
     */
    public static Function<Item, String> sha256(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA256);
    }

    /**
     * A function that gets the index of a substring in a string field.
     *
     * @param stringField the name of the string field
     * @param searchString the substring to search
     * @return the function
     */
    public static Function<Item, Integer> indexOf(String stringField, String searchString) {
        return new StringIndexOfFunction(stringField, searchString);
    }

    /**
     * A function that gets the length of a string field in an item.
     *
     * @param stringField the name of the string field to calculate length
     * @return the function
     */
    public static Function<Item, Integer> length(String stringField) {
        return new StringLengthFunction(stringField);
    }

    /**
     * A function that replaces a substring to a new string in a string field.
     *
     * @param stringField the name of the string field
     * @param searchString the substring to search
     * @param replaceString the string used to replace the searchString
     * @return the function
     */
    public static Function<Item, String> replace(String stringField, String searchString, String replaceString) {
        return new StringReplaceFunction(stringField, searchString, replaceString);
    }

    /**
     * A function that gets a substring of a string field in an item.
     *
     * @param stringField the name of the string field to calculate length
     * @param start the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @param end the position to end at (exclusive), negative means
     *  count back from the end of the String by this many characters
     * @return the function
     */
    public static Function<Item, String> subString(String stringField, int start, int end) {
        return new SubStringFunction(stringField, start, end);
    }
}
