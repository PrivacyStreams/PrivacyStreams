package com.github.privacystreams.core.actions;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class StreamAction<InStream extends Stream> extends EventDrivenFunction<InStream, Void> {

    protected abstract void onInput(Item item);

    @Subscribe
    protected final void onEvent(Item item) {
        this.onInput(item);
    }

    @Override
    protected final void init() {
        this.input.register(this);
    }

    protected final void finish() {
        this.input.unregister(this);
    }
}
