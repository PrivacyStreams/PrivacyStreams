package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.LazyFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class S2STransformation extends LazyFunction<SingleItemStream, SingleItemStream> {

    protected SingleItemStream initOutput(SingleItemStream input) {
        return new SingleItemStream(input.getStreamProvider().compound(this), this.getUQI());
    }

    protected void onStop(SingleItemStream input, SingleItemStream output) {
        input.close();
        if (!output.isClosed()) output.write(null);
    }

    @Override
    protected final void applyInBackground(SingleItemStream input, SingleItemStream output) {
        Item oldItem = input.read();
        Item newItem = this.applyInBackground(oldItem);
        output.write(newItem);
    }

    protected abstract Item applyInBackground(Item input);
}
