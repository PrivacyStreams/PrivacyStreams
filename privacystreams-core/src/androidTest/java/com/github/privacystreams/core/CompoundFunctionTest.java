package com.github.privacystreams.core;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yuanchun on 19/02/2017.
 */
public class CompoundFunctionTest {

    @Test
    public void apply() throws Exception {
        Function<Integer, Integer> addOneFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(UQI uqi, Integer input) {
                return input + 1;
            }

            @Override
            protected List<Object> getParameters() {
                return null;
            }
        };

        Function<Integer, Integer> addTwoFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(UQI uqi, Integer input) {
                return input + 2;
            }

            @Override
            protected List<Object> getParameters() {
                return null;
            }
        };

        Function<Integer, Integer> compoundFunction = addOneFunction.compound(addTwoFunction);
        int result = compoundFunction.apply(null, 1);
        assertEquals(4, result);
    }

}