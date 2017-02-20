package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.AsyncFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class M2STransformation extends AsyncFunction<MultiItemStream, SingleItemStream> {

    protected SingleItemStream initOutput(MultiItemStream input) {
        return new SingleItemStream(input.getStreamProvider().compound(this), this.getUQI());
    }

    protected void onStop(MultiItemStream input, SingleItemStream output) {
        input.close();
        if (!output.isClosed()) output.write(null);
    }

    @Override
    protected final void applyInBackground(MultiItemStream input, SingleItemStream output) {
        Item newItem = this.applyInBackground(input);
        output.write(newItem);
    }

    protected abstract Item applyInBackground(MultiItemStream stream);
}
