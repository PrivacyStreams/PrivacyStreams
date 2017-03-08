package com.github.privacystreams.commons.item;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that returns true if a field value is in a given collection
 */
final class FieldInCollectionPredicate<TValue> extends ItemFunction<Boolean> {

    private final String field;
    private final List<TValue> collectionToCompare;

    FieldInCollectionPredicate(final String field, final List<TValue> collectionToCompare) {
        this.field = Assertions.notNull("field", field);
        this.collectionToCompare = Assertions.notNull("collectionToCompare", collectionToCompare);
        this.addParameters(field, collectionToCompare);
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        TValue fieldValue = input.getValueByField(this.field);
        return Arrays.asList(this.collectionToCompare).contains(fieldValue);
    }

}
