package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Logging;

import org.greenrobot.eventbus.Subscribe;

/**
 * Transform a stream to a stream
 */

abstract class StreamTransformation<InStream extends Stream, OutStream extends Stream> extends EventDrivenFunction<InStream, OutStream> {

    protected abstract void onInput(Item item);

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

    @Subscribe
    public final void onEvent(Item item) {
        if (this.isCancelled || item == null) return;
        this.onInput(item);
    }

    @Override
    protected void init() {
        this.input.register(this);
    }

    protected final void finish() {
        this.input.unregister(this);
        this.output(Item.EOS);
    }

    @Override
    protected final void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if (this.input != null)
            this.input.unregister(this);
        if (this.output != null)
            this.output(Item.EOS);
    }
}
