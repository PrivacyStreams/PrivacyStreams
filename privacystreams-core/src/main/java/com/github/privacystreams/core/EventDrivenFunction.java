package com.github.privacystreams.core;

import android.content.Context;

/**
 * An EventDrivenFunction is driven by events.
 * When applying this function, the output will immediately return,
 * but the content in output will be generated later.
 *
 * Subclass must implement:
 * `init` method which initializes the output object and returns in current thread.
 */

public abstract class EventDrivenFunction<Tin, Tout> extends Function<Tin, Tout> {
    private transient UQI uqi;

    protected transient Tin input;
    protected transient Tout output;

    protected EventDrivenFunction() {
    }

    public final Tout apply(UQI uqi, Tin input) {
        this.uqi = uqi;
        this.input = input;
        this.init();
        return this.output;
    }

    protected UQI getUQI() {
        return this.uqi;
    }
    protected Context getContext() {
        return this.getUQI().getContext();
    }

    protected abstract void init();
}
