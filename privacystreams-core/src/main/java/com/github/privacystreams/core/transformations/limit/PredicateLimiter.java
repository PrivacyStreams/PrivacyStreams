package com.github.privacystreams.core.transformations.limit;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/11/2016.
 * limit the items with a predicate, stop the stream once the predicate fails
 */
final class PredicateLimiter extends StreamLimiter {
    private final Function<Item, Boolean> predicate;

    PredicateLimiter(final Function<Item, Boolean> predicate) {
        this.predicate = notNull("predicate", predicate);
    }

    @Override
    protected boolean keep(Item item) {
        return this.predicate.apply(this.getUQI(), item);
    }

    private static final String OPERATOR = "$limit_with_predicate";

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(predicate.toString());
        return parameters;
    }
}
