package com.github.privacystreams.commons.string;

import com.github.privacystreams.core.utils.Assertions;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that replace a substring in a string field with a new string
 */
final class StringReplaceFunction extends StringProcessor<String> {

    private final String searchString;
    private final String replaceString;

    StringReplaceFunction(String stringField, String searchString, String replaceString) {
        super(stringField);
        this.searchString = Assertions.notNull("searchString", searchString);
        this.replaceString = Assertions.notNull("replaceString", replaceString);
        this.addParameters(searchString, replaceString);
    }

    @Override
    protected String processString(String stringValue) {
        return StringUtils.replace(stringValue, this.searchString, this.replaceString);
    }
}
