package io.github.privacystreams.core.actions.callback;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamAction;
import io.github.privacystreams.utils.Assertions;

/**
 * Callback with a field value of an item
 * if the field value is different from the field value of the former item.
 */

class OnFieldChangeCallback<TValue, Void> extends PStreamAction {
    private final String fieldToSelect;
    private final Function<TValue, Void> fieldValueCallback;

    OnFieldChangeCallback(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        this.fieldToSelect = Assertions.notNull("fieldToSelect", fieldToSelect);
        this.fieldValueCallback = Assertions.notNull("fieldValueCallback", fieldValueCallback);
        this.addParameters(fieldToSelect, fieldValueCallback);
    }

    private transient TValue lastFieldValue;
    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        TValue fieldValue = item.getValueByField(this.fieldToSelect);
        if (fieldValue == null) return;
        if (fieldValue.equals(lastFieldValue)) return;
        this.fieldValueCallback.apply(this.getUQI(), fieldValue);
        this.lastFieldValue = fieldValue;
    }

}
