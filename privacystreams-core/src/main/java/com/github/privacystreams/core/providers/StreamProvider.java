package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class StreamProvider<OutStream extends Stream> extends EventDrivenFunction<Void, OutStream> {
    protected void init() {};
    protected final void output(Item item) {
        this.output.write(item);
    }
}
