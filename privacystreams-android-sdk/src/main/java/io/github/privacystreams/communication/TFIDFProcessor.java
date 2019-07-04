package io.github.privacystreams.communication;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Process the text field in an item.
 */
abstract class TFIDFProcessor<Tout> extends ItemOperator<Tout> {

    private final String messageDataField;
    //private final String term;

    TFIDFProcessor(String messageDataField) {
        this.messageDataField = Assertions.notNull("messageDataField", messageDataField);
        this.addParameters(messageDataField);

        //this.term = Assertions.notNull("term", term);
        //this.addParameters(term);

    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String message = input.getValueByField(this.messageDataField);

        //String term = input.getValueByField(this.term);

        return this.getTFIDFScore(uqi, message);
    }

    protected abstract Tout getTFIDFScore(UQI uqi, String message);

}

