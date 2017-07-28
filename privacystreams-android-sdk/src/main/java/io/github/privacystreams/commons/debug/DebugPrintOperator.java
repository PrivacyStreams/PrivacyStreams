package io.github.privacystreams.commons.debug;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Logging;

/**
 * Print the item for debugging
 */

final class DebugPrintOperator<Tin> extends Function<Tin, Void> {
    @Override
    public Void apply(UQI uqi, Tin input) {
        String debugMsg;

        if (input instanceof Item) debugMsg = ((Item) input).toDebugString();
        else debugMsg = "" + input;

        Logging.debug(debugMsg);
        return null;
    }

}
