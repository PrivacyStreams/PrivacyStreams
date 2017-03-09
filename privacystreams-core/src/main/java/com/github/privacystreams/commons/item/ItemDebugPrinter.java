package com.github.privacystreams.commons.item;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.utils.Logging;

/**
 * Print the item for debugging
 */

final class ItemDebugPrinter extends ItemFunction<Void> {
    @Override
    public Void apply(UQI uqi, Item input) {
        Logging.debug(input.toString());
        return null;
    }

}
