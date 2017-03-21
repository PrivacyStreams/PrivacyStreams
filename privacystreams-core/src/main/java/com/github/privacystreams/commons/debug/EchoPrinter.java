package com.github.privacystreams.commons.debug;

import android.util.Log;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Logging;

/**
 * Print the item for debugging
 */

final class EchoPrinter<T> extends Function<T, T> {

    private final String logTag;

    EchoPrinter(String logTag) {
        this.logTag = Assertions.notNull("logTag", logTag);
    }

    @Override
    public T apply(UQI uqi, T input) {
        Log.d(this.logTag, input.toString());
        return input;
    }

}
