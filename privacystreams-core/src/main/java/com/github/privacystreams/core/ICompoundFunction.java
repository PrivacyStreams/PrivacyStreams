package com.github.privacystreams.core;

import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 19/12/2016.
 * A compound function combines two functions, by taking function1's output as function2's input
 */

interface ICompoundFunction<Tin, Tout> {
    Function<Tin, ?> getFunction1();
    Function<?, Tout> getFunction2();
}
