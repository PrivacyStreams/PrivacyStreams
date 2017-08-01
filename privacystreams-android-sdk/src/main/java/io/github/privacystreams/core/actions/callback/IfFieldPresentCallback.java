package io.github.privacystreams.core.actions.callback;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamAction;
import io.github.privacystreams.utils.Assertions;

/**
 * Callback with a field value in a item once the field value is present
 */

class IfFieldPresentCallback<TValue, Void> extends PStreamAction {

    private final String fieldToSelect;
    private final Function<TValue, Void> fieldValueCallback;

    IfFieldPresentCallback(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
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
        if (fieldValue == null) return;
        this.fieldValueCallback.apply(this.getUQI(), fieldValue);
    }

}
