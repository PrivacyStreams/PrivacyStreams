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
        Thread providingThread = new Thread() {
            @Override
            public void run() {
                provide();
            }
        };
        providingThread.start();
    }

    protected final void output(Item item) {
        if (this.output == null || this.output.isClosed()) {
            this.cancel(this.getUQI());
        }
        else this.output.write(item, this);
    }

    /**
     * Provide stream data
     * This method will be running in background, and should be stopped when isCancelled turns true.
     */
    protected abstract void provide();

    protected void finish() {
        this.output(Item.EOS);
    }
}
