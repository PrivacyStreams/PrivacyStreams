package io.github.privacystreams.core;

import android.util.Log;

import io.github.privacystreams.utils.Logging;


/**
 * A PStreamProvider is a function that produces a stream.
 */
public abstract class PStreamProvider extends EventDrivenFunction<Void, PStream> {
    protected void init() {

        this.output = new PStream(this.getUQI(), this);
        this.isCancelled = false;
        Thread providingThread = new Thread() {
            @Override
            public void run() {
                Log.e("StreamProvider", "calling provide");
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
