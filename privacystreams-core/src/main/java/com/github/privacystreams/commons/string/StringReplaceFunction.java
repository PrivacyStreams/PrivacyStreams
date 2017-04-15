package com.github.privacystreams.commons.string;

import com.github.privacystreams.utils.Assertions;

/**
 * Replace a substring with a new string in the string specified by a field.
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
        if (stringValue == null) return null;
        return stringValue.replace(this.searchString, this.replaceString);
    }
}
