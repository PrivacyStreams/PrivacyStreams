package com.github.privacystreams.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 19/12/2016.
 * A compound function combines two functions, by taking function1's output as function2's input
 */

final class CompoundFunction<Tin, Ttemp, Tout> extends Function<Tin, Tout> {
    private Function<Tin, ? extends Ttemp> function1;
    private Function<? super Ttemp, Tout> function2;

    CompoundFunction(Function<Tin, ? extends Ttemp> function1, Function<? super Ttemp, Tout> function2) {
        this.function1 = function1;
        this.function2 = function2;
    }

    @Override
    public Tout apply(UQI uqi, Tin input) {
        return function2.apply(uqi, function1.apply(uqi, input));
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
