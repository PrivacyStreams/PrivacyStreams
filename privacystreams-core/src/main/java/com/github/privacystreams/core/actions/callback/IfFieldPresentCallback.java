package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with a field value in a item once the field value is present
 */

class IfFieldPresentCallback<TValue, Void> extends AsyncMultiItemStreamAction<Void> {

    private final String fieldToSelect;
    private final Function<TValue, Void> callback;

    IfFieldPresentCallback(String fieldToSelect, Function<TValue, Void> callback) {
        this.fieldToSelect = fieldToSelect;
        this.callback = callback;
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
            if (fieldValue == null) continue;
            this.callback.apply(this.getUQI(), fieldValue);
            break;
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
