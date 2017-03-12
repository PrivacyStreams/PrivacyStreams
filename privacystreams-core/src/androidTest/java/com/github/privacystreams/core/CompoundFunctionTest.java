package com.github.privacystreams.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yuanchun on 19/02/2017.
 * Test cases for CompoundFunction class
 */
public class CompoundFunctionTest {

    @Test
    public void apply() throws Exception {
        Function<Integer, Integer> addOneFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(UQI uqi, Integer input) {
                return input + 1;
            }
        };

        Function<Integer, Integer> addTwoFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(UQI uqi, Integer input) {
                return input + 2;
            }
        };

        Function<Integer, Integer> compoundFunction = addOneFunction.compound(addTwoFunction);
        int result = compoundFunction.apply(null, 1);
        assertEquals(4, result);
    }

}