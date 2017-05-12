package com.github.privacystreams.commons.string;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.HashUtils;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access string-related functions
 */
@PSOperatorWrapper
public class StringOperators {

    /**
     * Check whether the string value of a field contains a certain substring.
     *
     * @param stringField the name of the string field
     * @param searchString the substring to search
     * @return the function.
     */
    public static Function<Item, Boolean> contains(String stringField, String searchString) {
        return new StringContainFunction(stringField, searchString);
    }

    /**
     * Get the MD5-hashed value of the string value of a field.
     *
     * @param stringField the name of the string field to perform hash.
     * @return the function.
     */
    public static Function<Item, String> md5(String stringField) {
        return new StringHashFunction(stringField, HashUtils.MD5);
    }

    /**
     * Get the SHA1-hashed value of the string value of a field.
     *
     * @param stringField the name of the string field to perform hash.
     * @return the function.
     */
    public static Function<Item, String> sha1(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA1);
    }

    /**
     * Get the SHA256-hashed value of the string value of a field.
     *
     * @param stringField the name of the string field to perform hash.
     * @return the function.
     */
    public static Function<Item, String> sha256(String stringField) {
        return new StringHashFunction(stringField, HashUtils.SHA256);
    }

    /**
     * Get the index of a substring in the string value of a field.
     *
     * @param stringField the name of the string field
     * @param searchString the substring to search
     * @return the function
     */
    public static Function<Item, Integer> indexOf(String stringField, String searchString) {
        return new StringIndexOfFunction(stringField, searchString);
    }

    /**
     * Get the length of the string value of a field.
     *
     * @param stringField the name of the string field to calculate length
     * @return the function
     */
    public static Function<Item, Integer> length(String stringField) {
        return new StringLengthFunction(stringField);
    }

    /**
     * Replace a substring to a new string in the string value of a field.
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
     * Get a substring of the string value of a field.
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

    /**
     * Elide a substring in the string value of a field.
     *
     * @param stringField the name of the string field to calculate length
     * @param start the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @param end the position to end at (exclusive), negative means
     *  count back from the end of the String by this many characters
     * @return the function
     */
    public static Function<Item, String> elide(String stringField, int start, int end) {
        return new StringElideFunction(stringField, start, end);
    }
}
