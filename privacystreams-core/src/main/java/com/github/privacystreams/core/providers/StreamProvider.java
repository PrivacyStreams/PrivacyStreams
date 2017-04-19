package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;
import com.github.privacystreams.utils.Logging;

/**
 * A StreamProvider is a function that produces a stream.
 */
abstract class StreamProvider<OutStream extends Stream> extends EventDrivenFunction<Void, OutStream> {
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
        if (this.output == null) {
            Logging.warn(this.getClass().getSimpleName() + " is outputting to an empty stream.");
            return;
        }
        if (this.output.isClosed()) {
            if (!this.isCancelled) this.cancel(this.getUQI());
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
