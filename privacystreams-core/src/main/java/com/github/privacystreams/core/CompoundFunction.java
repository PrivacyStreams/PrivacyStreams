package com.github.privacystreams.core;

import com.github.privacystreams.utils.Assertions;

/**
 * Compound two functions, by taking function1's output as function2's input.
 * The compounded function has the same input type as function1 and the same output type as function2.
 */

final class CompoundFunction<Tin, Ttemp, Tout> extends Function<Tin, Tout> {
    private Function<Tin, ? extends Ttemp> function1;
    private Function<? super Ttemp, Tout> function2;

    CompoundFunction(Function<Tin, ? extends Ttemp> function1, Function<? super Ttemp, Tout> function2) {
        this.function1 = Assertions.notNull("function1", function1);
        this.function2 = Assertions.notNull("function2", function2);;
        this.addParameters(function1, function2);
    }

    @Override
    public Tout apply(UQI uqi, Tin input) {
        Ttemp temp = function1.apply(uqi, input);
        return function2.apply(uqi, temp);
    }

    public String toString() {
        return function1.toString() + " --> " + function2.toString();
    }

    Function<Tin, ? extends Ttemp> getFunction1() {
        return this.function1;
    }

    Function<? super Ttemp, Tout> getFunction2() {
        return this.function2;
    }
}
