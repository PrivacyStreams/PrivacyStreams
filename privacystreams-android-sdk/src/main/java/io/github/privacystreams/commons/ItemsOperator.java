package io.github.privacystreams.commons;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;

import java.util.List;

/**
 * A function that takes a list of items as input.
 */

public abstract class ItemsOperator<Tout> extends Function<List<Item>, Tout> {
}
