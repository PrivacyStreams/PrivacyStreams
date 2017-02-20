package com.github.privacystreams.core.utilities;

import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/11/2016.
 * A stream collector collects the stream for output
 */

public abstract class ItemsFunction<Tout> extends Function<List<Item>, Tout> {
}
