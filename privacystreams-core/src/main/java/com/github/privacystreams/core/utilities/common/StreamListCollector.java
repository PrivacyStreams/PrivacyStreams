package com.github.privacystreams.core.utilities.common;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemsFunction;

import com.github.privacystreams.core.Item;

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

    @Override
    protected List<Object> getParameters() {
        return new ArrayList<>();
    }
}
