package io.github.privacystreams.core.actions.callback;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamAction;
import io.github.privacystreams.utils.Assertions;

/**
 * Callback with the field value for each item in the stream.
 * The callback will be invoked with the field value.
 */

class ForEachFieldCallback<TValue, Void> extends PStreamAction {

    private final String fieldToSelect;
    private final Function<TValue, Void> fieldValueCallback;

    ForEachFieldCallback(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        this.fieldToSelect = Assertions.notNull("fieldToSelect", fieldToSelect);
        this.fieldValueCallback = Assertions.notNull("fieldValueCallback", fieldValueCallback);
        this.addParameters(fieldToSelect, fieldValueCallback);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        TValue fieldValue = item.getValueByField(this.fieldToSelect);
        this.fieldValueCallback.apply(this.getUQI(), fieldValue);
    }

}
