package io.github.privacystreams.core;

import org.greenrobot.eventbus.Subscribe;

import io.github.privacystreams.utils.Logging;

/**
 * Transform a stream to a stream
 */

public abstract class PStreamTransformation extends EventDrivenFunction<PStream, PStream> {

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
        this.output = new PStream(this.getUQI(), input.getStreamProvider().compound(this));
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
