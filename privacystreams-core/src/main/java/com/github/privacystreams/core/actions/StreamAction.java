package com.github.privacystreams.core.actions;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;
import com.github.privacystreams.core.UQI;

import org.greenrobot.eventbus.Subscribe;

/**
 * Output a stream.
 */

abstract class StreamAction<InStream extends Stream> extends EventDrivenFunction<InStream, Void> {

    protected abstract void onInput(Item item);

    @Subscribe
    public final void onEvent(Item item) {
        if (this.isCancelled || item == null) return;
        this.onInput(item);
    }

    @Override
    protected final void init() {
        this.input.register(this);
    }

    protected final void finish() {
        this.input.unregister(this);
    }

    @Override
    protected final void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if (this.input != null)
            this.input.unregister(this);
    }
}
