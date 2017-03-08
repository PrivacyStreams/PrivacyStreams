package com.github.privacystreams.core.commons.string;

import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.utils.HashUtils;
import com.github.privacystreams.core.utils.Logging;

import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that calculates the length of a string field in the item
 */
final class StringLengthFunction extends StringProcessor<Integer> {

    StringLengthFunction(String stringField) {
        super(stringField);
    }

    @Override
    protected Integer processString(String stringValue) {
        return StringUtils.length(stringValue);
    }
}
