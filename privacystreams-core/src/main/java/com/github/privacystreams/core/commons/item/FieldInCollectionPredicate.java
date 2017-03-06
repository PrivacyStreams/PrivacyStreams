package com.github.privacystreams.core.commons.item;

import java.util.Collection;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that returns true if a field value is in a given collection
 */
final class FieldInCollectionPredicate<TValue extends Collection> extends ItemFunction<Boolean> {

    private final String field;
    private final Collection<TValue> collectionToCompare;

    FieldInCollectionPredicate(final String field, final Collection<TValue> collectionToCompare) {
        this.field = Assertions.notNull("field", field);
        this.collectionToCompare = Assertions.notNull("collectionToCompare", collectionToCompare);
        this.addParameters(field, collectionToCompare);
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        TValue fieldValue = input.getValueByField(this.field);
        return this.collectionToCompare.contains(fieldValue);
    }

}
