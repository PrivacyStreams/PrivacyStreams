package com.github.privacystreams.core;

/**
 * Created by yuanchun on 14/12/2016.
 * A Function convert a input in type Tin to a output in type Tout
 */

public abstract class Callback<Tin> extends Function<Tin, Void> {
    @Override
    public final Void apply(UQI uqi, Tin input) {
        this.onSuccess(input);
        return null;
    }

    public abstract void onSuccess(Tin input);
}
