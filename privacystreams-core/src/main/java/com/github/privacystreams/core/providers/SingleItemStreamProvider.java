package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 29/11/2016.
 * Create a new stream of data
 */

public abstract class SingleItemStreamProvider extends LazyFunction<Void, SingleItemStream> {

    protected final SingleItemStream initOutput(Void input) {
        return new SingleItemStream(this, this.getUQI());
    }

    protected void onStop(Void input, SingleItemStream output) {
        if (!output.isClosed()) output.write(null);
    }

    @Override
    protected final void applyInBackground(Void input, SingleItemStream output) {
        this.provide(output);
    }

    protected abstract void provide(SingleItemStream output);
}
