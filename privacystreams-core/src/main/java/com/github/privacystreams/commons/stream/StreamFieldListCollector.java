package com.github.privacystreams.commons.stream;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemsFunction;
import com.github.privacystreams.core.Item;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/12/2016.
 * Select a field of each item in a stream and output to a list
 * Each item in the list is the field value in type TValue.
 */

class StreamFieldListCollector<TValue> extends ItemsFunction<List<TValue>> {
    private final String fieldToSelect;

    StreamFieldListCollector(String fieldToSelect) {
        this.fieldToSelect = notNull("fieldToSelect", fieldToSelect);
        this.addParameters(fieldToSelect);
    }

    @Override
    public List<TValue> apply(UQI uqi, List<Item> items) {
        List<TValue> result = new ArrayList<>();
        for (Item item : items) {
            TValue fieldValue = item.getValueByField(this.fieldToSelect);
            result.add(fieldValue);
        }
        return result;
    }

}
