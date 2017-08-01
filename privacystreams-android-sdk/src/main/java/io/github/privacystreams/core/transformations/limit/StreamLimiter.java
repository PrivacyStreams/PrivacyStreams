package io.github.privacystreams.core.transformations.limit;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamTransformation;

/**
 * Limit the length of the stream.
 */
abstract class StreamLimiter extends PStreamTransformation {

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        if (this.keep(item)) this.output(item);
        else this.finish();
    }

    protected abstract boolean keep(Item item);
}
