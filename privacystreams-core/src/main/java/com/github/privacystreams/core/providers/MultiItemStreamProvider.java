package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.LazyFunction;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 29/11/2016.
 * Create a new stream of data
 */

public abstract class MultiItemStreamProvider extends LazyFunction<Void, MultiItemStream> {

    protected final MultiItemStream initOutput(Void input) {
        return new MultiItemStream(this, this.getUQI());
    }

    @Override
    protected final void applyInBackground(Void input, MultiItemStream output) {
        this.provide(output);
    }

    protected abstract void provide(MultiItemStream output);
}
