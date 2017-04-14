package com.github.privacystreams.commons.debug;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Logging;

/**
 * Print the item for debugging
 */

final class DebugPrinter<Tin> extends Function<Tin, Void> {
    @Override
    public Void apply(UQI uqi, Tin input) {
        Logging.debug("" + input);
        return null;
    }

}
