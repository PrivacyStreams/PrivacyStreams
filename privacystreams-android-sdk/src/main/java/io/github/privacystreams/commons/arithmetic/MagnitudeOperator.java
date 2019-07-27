package io.github.privacystreams.commons.arithmetic;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class MagnitudeOperator extends ItemOperator<Number> {
    String[] numFields;
    MagnitudeOperator(String[] numFields){
        this.numFields = numFields;
    }
    public final Number apply(UQI uqi, Item input) {
        double inside = 0;
        for(String field : numFields){
            Number num = input.getValueByField(field);
            double f = num.doubleValue();
            inside += f * f;
        }
        return Math.sqrt(inside);
    }
}

