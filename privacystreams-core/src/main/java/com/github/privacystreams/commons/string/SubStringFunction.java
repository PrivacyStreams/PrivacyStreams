package com.github.privacystreams.commons.string;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that calculates the substring of a string field in the item
 */
final class SubStringFunction extends StringProcessor<String> {

    private final int start;
    private final int end;

    SubStringFunction(String stringField, int start, int end) {
        super(stringField);
        this.start = start;
        this.end = end;
        this.addParameters(start, end);
    }

    @Override
    protected String processString(String stringValue) {
        return StringUtils.substring(stringValue, this.start, this.end);
    }
}
