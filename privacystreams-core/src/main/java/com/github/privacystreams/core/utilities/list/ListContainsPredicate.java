package com.github.privacystreams.core.utilities.list;

import java.util.List;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that makes list-related comparisons on field values
 */
final class ListContainsPredicate extends ListProcessor<Boolean> {

    private final Object value;

    ListContainsPredicate(final String listField, final Object value) {
        super(listField);
        this.value = value;
    }

    @Override
    protected Boolean processList(List<Object> list) {
        return list.contains(this.value);
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = super.getParameters();
        parameters.add(value);
        return parameters;
    }
}
