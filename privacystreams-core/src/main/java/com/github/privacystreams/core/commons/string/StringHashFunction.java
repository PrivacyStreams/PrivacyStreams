package com.github.privacystreams.core.commons.string;

import java.security.NoSuchAlgorithmException;

import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.HashUtils;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that hash a given string and return the hashed string
 */
final class StringHashFunction extends StringProcessor<String> {

    private final String hashAlgorithm;

    StringHashFunction(String stringField, String hashAlgorithm) {
        super(stringField);
        this.hashAlgorithm = Assertions.notNull("hashAlgorithm", hashAlgorithm);
        this.addParameters(hashAlgorithm);

    }

    @Override
    protected String processString(String stringValue) {
        try {
            return HashUtils.hash(stringValue, hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            Logging.warn("Hash function failed. Algorithm " + hashAlgorithm);
            e.printStackTrace();
            return null;
        }
    }
}
