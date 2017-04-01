package com.github.privacystreams.commons.debug;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access debugging functions
 */
@PSOperatorWrapper
public class DebugOperators {
    /**
     * Print the input for debugging.
     *
     * @param <Tin> the input type
     * @return the function
     */
    public static <Tin> Function<Tin, Void> debug() {
        return new DebugPrinter<>();
    }

    /**
     * Log the input and return as original.
     *
     * @param logTag the log tag to use in logging
     * @param <T> the input/output type
     * @return the function
     */
    public static <T> Function<T, T> logAs(String logTag) {
        return new EchoPrinter<>(logTag);
    }
}
