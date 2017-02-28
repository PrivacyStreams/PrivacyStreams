package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with a field value in a item once the field value is present
 */

class IfFieldPresentCallback<TValue, Void> extends AsyncMultiItemStreamAction {

    private final String fieldToSelect;
    private final Function<TValue, Void> fieldValueCallback;

    IfFieldPresentCallback(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        this.fieldToSelect = Assertions.notNull("fieldToSelect", fieldToSelect);
        this.fieldValueCallback = Assertions.notNull("fieldValueCallback", fieldValueCallback);
        this.addParameters(fieldToSelect, fieldValueCallback);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) this.finish();
        TValue fieldValue = item.getValueByField(this.fieldToSelect);
        if (fieldValue == null) return;
        this.fieldValueCallback.apply(this.getUQI(), fieldValue);
    }

}
