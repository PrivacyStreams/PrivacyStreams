package com.github.privacystreams.core.transformations.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/11/2016.
 * Keep the items satisfying a predicate
 */
final class PredicateFilter extends StreamFilter {
    private final Function<Item, Boolean> predicate;

    PredicateFilter(final Function<Item, Boolean> predicate) {
        this.predicate = notNull("predicate", predicate);
    }

    @Override
    public boolean keep(Item item) {
        return this.predicate.apply(this.getUQI(), item);
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(predicate.toString());
        return parameters;
    }

}
