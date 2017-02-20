package com.github.privacystreams.core.utilities;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 13/12/2016.
 * A function that takes a Item as the input and an arbitrary Type as the output type
 */

public abstract class ItemFunction<Tout> extends Function<Item, Tout> {
}
