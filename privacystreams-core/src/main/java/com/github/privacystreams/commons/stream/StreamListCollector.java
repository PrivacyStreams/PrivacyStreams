package com.github.privacystreams.commons.stream;

import com.github.privacystreams.commons.ItemsFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * Collect a stream to a list
 * Each item in the list is a key-value map.
 */

class StreamListCollector extends ItemsFunction<List<Item>> {
    @Override
    public List<Item> apply(UQI uqi, List<Item> items) {
        return items;
    }

}
