package com.github.privacystreams.commons;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

import java.util.List;

/**
 * A function that takes a list of items as input.
 */

public abstract class ItemsFunction<Tout> extends Function<List<Item>, Tout> {
}
