package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.LazyFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class S2MTransformation extends LazyFunction<SingleItemStream, MultiItemStream> {

    protected MultiItemStream initOutput(SingleItemStream input) {
        return new MultiItemStream(input.getStreamProvider().compound(this), this.getUQI());
    }

    protected void onStop(SingleItemStream input, MultiItemStream output) {
        input.close();
        if (!output.isClosed()) output.write(null);
    }

    @Override
    protected final void applyInBackground(SingleItemStream input, MultiItemStream output) {
        Item item = input.read();
        this.applyInBackground(item, output);
    }

    protected abstract void applyInBackground(Item item, MultiItemStream output);
}
