package com.github.privacystreams.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 19/12/2016.
 * A compound function combines two functions, by taking function1's output as function2's input
 */

final class LazyCompoundFunction<Tin, Ttemp, Tout> extends LazyFunction<Tin, Tout> {
    private LazyFunction<Tin, ? extends Ttemp> function1;
    private LazyFunction<? super Ttemp, Tout> function2;

    LazyCompoundFunction(LazyFunction<Tin, ? extends Ttemp> function1,
                         LazyFunction<? super Ttemp, Tout> function2) {
        this.function1 = function1;
        this.function2 = function2;
    }

    @Override
    public Tout apply(UQI uqi, Tin input) {
        return this.function2.apply(uqi, this.function1.apply(uqi, input));
    }

    @Override
    protected Tout initOutput(Tin input) {
        return this.function2.initOutput(this.function1.initOutput(input));
    }

    public void evaluate() {
        this.function1.evaluate();
        this.function2.evaluate();
    }

    @Override
    protected void applyInBackground(Tin input, Tout output) {
        // Should not reach here.
        return;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(function1);
        parameters.add(function2);
        return parameters;
    }

    public String toString() {
        return function1.toString() + " --> " + function2.toString();
    }
}
