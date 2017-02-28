package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;
import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class StreamProvider<OutStream extends Stream> extends EventDrivenFunction<Void, OutStream> {
    protected void init() {
        this.isCancelled = false;
    }

    protected final void output(Item item) {
        this.output.write(item);
    }

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        this.isCancelled = true;
    }

    protected transient volatile boolean isCancelled;

    protected boolean isCancelled() {
        return this.isCancelled;
    }
}
