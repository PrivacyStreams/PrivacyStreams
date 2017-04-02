package com.github.privacystreams.core;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * An AsyncFunction is will be applied in background.
 * When applying this function, the output will immediately return,
 * but the content in output will be generated in background.
 *
 * Subclass must implement:
 * `init` method which initializes the output object and returns in current thread.
 * `applyInBackground` method which produces values to output in background.
 */

public abstract class AsyncFunction<Tin, Tout> extends Function<Tin, Tout> {
    private transient UQI uqi;

    protected transient Tin input;
    protected transient Tout output;

    protected AsyncFunction() {
    }

    public final Tout apply(UQI uqi, Tin input) {
        this.uqi = uqi;
        this.input = input;
        this.output = this.init(uqi, input);
        EventBus tempBus = new EventBus();
        tempBus.register(this);
        tempBus.post(new Object());
        return this.output;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(Object obj) {
        this.applyInBackground(this.uqi, this.input);
    }

    protected UQI getUQI() {
        return this.uqi;
    }

    protected abstract Tout init(UQI uqi, Tin input);
    protected abstract void applyInBackground(UQI uqi, Tin input);
}
