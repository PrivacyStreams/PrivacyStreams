package com.github.privacystreams.core.transformations.filter;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.google.gson.annotations.Expose;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/11/2016.
 * Keep the items satisfying a predicate
 */
final class PredicateFilter extends StreamFilter {
    @Expose
    private final Function<Item, Boolean> predicate;

    PredicateFilter(final Function<Item, Boolean> predicate) {
        this.predicate = notNull("predicate", predicate);
        this.addParameters(predicate);
    }

    @Override
    public boolean keep(Item item) {
        return this.predicate.apply(this.getUQI(), item);
    }

}
