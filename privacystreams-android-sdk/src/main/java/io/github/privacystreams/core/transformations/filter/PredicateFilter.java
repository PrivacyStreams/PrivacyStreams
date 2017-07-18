package io.github.privacystreams.core.transformations.filter;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;

import static io.github.privacystreams.utils.Assertions.notNull;

/**
 * Keep the items that satisfy a predicate.
 */
final class PredicateFilter extends StreamFilter {
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
