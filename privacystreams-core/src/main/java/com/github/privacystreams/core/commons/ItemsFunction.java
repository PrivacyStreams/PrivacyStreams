package com.github.privacystreams.core.commons;

import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * A function that takes a list of items as input.
 */

public abstract class ItemsFunction<Tout> extends Function<List<Item>, Tout> {
}
