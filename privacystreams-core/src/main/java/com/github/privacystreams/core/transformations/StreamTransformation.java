package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;
import com.github.privacystreams.core.UQI;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class StreamTransformation<InStream extends Stream, OutStream extends Stream> extends EventDrivenFunction<InStream, OutStream> {

    protected abstract void onInput(Item item);

    protected final void output(Item item) {
        if (this.output == null || this.output.isClosed()) {
            if (!this.isCancelled) this.cancel(this.getUQI());
        }
        else this.output.write(item, this);
    }

    @Subscribe
    protected final void onEvent(Item item) {
        if (this.isCancelled) return;
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
    protected final void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        if (this.input != null)
            this.input.unregister(this);
        this.output(Item.EOS);
    }
}
