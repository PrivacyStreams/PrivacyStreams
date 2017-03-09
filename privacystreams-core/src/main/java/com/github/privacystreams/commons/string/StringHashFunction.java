package com.github.privacystreams.commons.string;

import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.HashUtils;
import com.github.privacystreams.utils.Logging;

import java.security.NoSuchAlgorithmException;

/**
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
