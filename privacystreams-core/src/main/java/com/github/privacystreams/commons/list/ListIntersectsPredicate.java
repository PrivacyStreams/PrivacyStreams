package com.github.privacystreams.commons.list;

import com.github.privacystreams.core.utils.Assertions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that makes list-related comparisons on field values
 */
final class ListIntersectsPredicate extends ListProcessor<Boolean> {

    private final Object[] listToCompare;

    ListIntersectsPredicate(final String listField, final Object[] listToCompare) {
        super(listField);
        this.listToCompare = Assertions.notNull("listToCompare", listToCompare);
        this.addParameters(listToCompare);
    }

    @Override
    protected Boolean processList(List<Object> list) {
        List<Object> objectList = Arrays.asList(this.listToCompare);
        for (Object element : list) {
            if (objectList.contains(element))
                return true;
        }
        return false;
    }

}
