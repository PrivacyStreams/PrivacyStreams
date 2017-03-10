package com.github.privacystreams.core;

import android.content.Context;

/**
 * Created by yuanchun on 01/12/2016.
 * An LazyFunction applies in background.
 *
 * Subclass must implement:
 * init method which initializes the output object and returns in UI thread.
 * applyInBackground method which produce values to output in background.
 *
 * Subclass may implement:
 * 1. onStart method which is invoked when the function starts evaluating;
 * 2. onStop method which is invoked when the function stops evaluating,
 *    onStop is invoked before onFinish/onCancel,
 *    onStop should be mainly used for recycling objects.
 * 3. onFinish method which is invoked when the function is finished
 * 4. onCancel method which is invoked when the function is cancelled
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
