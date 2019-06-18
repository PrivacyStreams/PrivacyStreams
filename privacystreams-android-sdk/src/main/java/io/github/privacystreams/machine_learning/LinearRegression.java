package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class LinearRegression extends MLProcessor<Object>{
    List<Float> weights = new ArrayList<>(0);
    Function<Item, Float> fun;

    LinearRegression(List<String> inputFields, List<Float> weights){
        super(inputFields);
        this.weights = Assertions.notNull("weights", weights);
        this.addParameters(weights);
    }

    LinearRegression(Function<Item, Float> fun) {
        super(new ArrayList<String>(0));
        this.fun = Assertions.notNull("fun", fun);
        this.addParameters(fun);
    }

    private Float inferFun(UQI uqi, Item item) {
        return fun.apply(uqi, item);
    }
    private Float inferWeights(UQI uqi, Item item) {
        Float res = Float.valueOf(0);
        for (int i = 0; i < this.weights.size(); i++) {
            Number v = item.getValueByField(this.inputFields.get(i));
            Float ival = v.floatValue();
            res += ival * this.weights.get(i);
        }
        return res;
    }

    @Override
    protected Float infer(UQI uqi, Item item){
        if(this.weights.size() == 0) {
            return inferFun(uqi, item);
        }
        if(this.weights.size() != this.inputFields.size()) {
            //oops
        }
        return inferWeights(uqi, item);
    }
}
