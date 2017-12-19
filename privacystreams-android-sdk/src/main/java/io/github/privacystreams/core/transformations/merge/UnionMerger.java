package io.github.privacystreams.core.transformations.merge;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.PStreamTransformation;

/**
 * Union the stream with another stream.
 */
class UnionMerger extends StreamMerger {

    private volatile boolean selfFinished = false;
    private volatile boolean anotherFinished = false;

    UnionMerger(PStreamProvider anotherProvider) {
        super(anotherProvider);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.selfFinished = true;
            if (this.selfFinished && this.anotherFinished) {
                this.finish();
            }
            return;
        }
        output(item);
    }

    @Override
    protected void onInputAnother(Item item) {
        if (item.isEndOfStream()) {
            this.anotherFinished = true;
            if (this.selfFinished && this.anotherFinished) {
                this.finish();
            }
            return;
        }
        output(item);
    }
}
