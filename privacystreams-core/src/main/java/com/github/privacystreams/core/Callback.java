package com.github.privacystreams.core;

import com.github.privacystreams.core.exceptions.PSException;

/**
 * This function is mainly used to handle a result as a callback.
 * Subclasses must implement `onSuccess` method to handle the result.
 * To handle the exceptions, subclasses should implement `onFail` method.
 */

public abstract class Callback<Tin> extends Function<Tin, Void> {
    @Override
    public final Void apply(UQI uqi, Tin input) {
        this.onSuccess(input);
        return null;
    }

    @Override
    public final void onCancelled(UQI uqi) {
        this.onFail(uqi.getException());
    }

    /**
     * This method will be invoked if successfully got the result.
     * @param input the result
     */
    protected abstract void onSuccess(Tin input);

    /**
     * This method will be invoked if failed to get the result.
     * @param exception the exception happened during getting the result.
     */
    protected void onFail(PSException exception) {};
}
