package com.github.privacystreams.core;

import com.github.privacystreams.core.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 19/12/2016.
 * A compound function combines two functions, by taking function1's output as function2's input
 */

final class LazyCompoundFunction<Tin, Ttemp, Tout> extends LazyFunction<Tin, Tout> implements ICompoundFunction<Tin, Tout> {
    private LazyFunction<Tin, ? extends Ttemp> function1;
    private LazyFunction<? super Ttemp, Tout> function2;

    LazyCompoundFunction(LazyFunction<Tin, ? extends Ttemp> function1,
                         LazyFunction<? super Ttemp, Tout> function2) {
        this.function1 = Assertions.notNull("function1", function1);
        this.function2 = Assertions.notNull("function2", function2);;
        this.addParameters(function1, function2);
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
    }

    @Override
    public Function<Tin, ? extends Ttemp> getFunction1() {
        return this.function1;
    }

    @Override
    public Function<? super Ttemp, Tout> getFunction2() {
        return this.function2;
    }

    public String toString() {
        return function1.toString() + " --> " + function2.toString();
    }
}
