package io.github.privacystreams.communication;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Process the text field in an item.
 */
abstract class TopicModelProcessor<Tout> extends ItemOperator<Tout> {

    private final String messageDataField;

    TopicModelProcessor(String messageDataField) {
        this.messageDataField = Assertions.notNull("messageDataField", messageDataField);
        this.addParameters(messageDataField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String message = input.getValueByField(this.messageDataField);
        return this.processCategories(uqi, message);
    }

    protected abstract Tout processCategories(UQI uqi, String message);

}
