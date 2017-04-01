package com.github.privacystreams.core.items;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;

/**
 * An empty item.
 */
@PSItem
public class EmptyItem extends Item {

    /**
     * Provide a live stream of EmptyItems. The interval between each two items is a given value.
     *
     * @return the provider function
     */
    public static MStreamProvider asUpdates(long interval) {
        return new EmptyItemUpdatesProvider(interval);
    }
}
