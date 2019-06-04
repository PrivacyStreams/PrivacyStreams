package io.github.privacystreams.machine_learning;

import java.util.List;
import java.util.Vector;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class LinearRegression extends MLProcessor<Double>{
    List<Double> weights = new Vector<>(0);
    Function<Item, Double> fun;

    LinearRegression(List<String> inputFields, List<Double> weights){
        super(inputFields);
        this.weights = Assertions.notNull("weights", weights);
        this.addParameters(weights);
    }

    LinearRegression(Function<Item, Double> fun) {
        super(new Vector<String>(0));
        this.fun = Assertions.notNull("fun", fun);
        this.addParameters(fun);
    }

    private Double inferFun(UQI uqi, Item item) {
        return fun.apply(uqi, item);
    }
    private Double inferWeights(UQI uqi, Item item) {
        Double res = 0.0;
        for (int i = 0; i < this.weights.size(); i++) {
            Double ival = (Double) item.getValueByField(this.inputFields.get(i));
            res += ival * this.weights.get(i);
        }
        return res;
    }

    @Override
    protected Double infer(UQI uqi, Item item){
        if(this.weights.size() == 0) {
            return inferFun(uqi, item);
        }
        if(this.weights.size() != this.inputFields.size()) {
            //oops
        }
        return inferWeights(uqi, item);
    }
}
