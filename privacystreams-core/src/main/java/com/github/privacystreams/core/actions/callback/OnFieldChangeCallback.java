package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with a field value of an item
 * if the field value is different from the field value of the former item.
 */

class OnFieldChangeCallback<TValue, Void> extends AsyncMultiItemStreamAction<Void> {
    private final String fieldToSelect;
    private final Function<TValue, Void> callback;

    OnFieldChangeCallback(String fieldToSelect, Function<TValue, Void> callback) {
        this.fieldToSelect = fieldToSelect;
        this.callback = callback;
    }

    @Override
    protected Void initOutput(MultiItemStream input) {
        return null;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, Void output) {
        TValue lastFieldValue = null;
        while (!this.isCancelled()) {
            Item item = input.read();
            if (item == null) break;
            TValue fieldValue = item.getValueByField(this.fieldToSelect);
            if (!fieldValue.equals(lastFieldValue))
                this.callback.apply(this.getUQI(), fieldValue);
            lastFieldValue = fieldValue;
        }
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(fieldToSelect);
        parameters.add(callback);
        return parameters;
    }
}
