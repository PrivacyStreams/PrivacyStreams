package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with the field value for each item in the stream.
 * The callback will be invoked with the field value.
 */

class ForEachFieldCallback<TValue, Void> extends AsyncMultiItemStreamAction<Void> {

    private final String fieldToSelect;
    private final Function<TValue, Void> fieldValueCallback;

    ForEachFieldCallback(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        this.fieldToSelect = Assertions.notNull("fieldToSelect", fieldToSelect);
        this.fieldValueCallback = Assertions.notNull("fieldValueCallback", fieldValueCallback);
        this.addParameters(fieldToSelect, fieldValueCallback);
    }

    @Override
    protected Void initOutput(MultiItemStream input) {
        return null;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, Void output) {
        while (!this.isCancelled()) {
            Item item = input.read();
            if (item == null) break;
            TValue fieldValue = item.getValueByField(this.fieldToSelect);
            this.fieldValueCallback.apply(this.getUQI(), fieldValue);
        }
    }

}
