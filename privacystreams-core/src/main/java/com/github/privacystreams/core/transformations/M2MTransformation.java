package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.LazyFunction;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class M2MTransformation extends LazyFunction<MultiItemStream, MultiItemStream> {

    protected MultiItemStream initOutput(MultiItemStream input) {
        return new MultiItemStream(input.getStreamProvider().compound(this), this.getUQI());
    }

    protected void onStop(MultiItemStream input, MultiItemStream output) {
        input.close();
        if (!output.isClosed()) output.write(null);
    }
}
