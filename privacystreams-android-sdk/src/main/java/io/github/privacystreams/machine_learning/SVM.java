package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class SVM extends MLProcessor<Number>{

    List<Float> weights;
    float intercept;

    SVM(List<String> inputFields, List<Float> weights, float intercept){
        super(inputFields);
        this.weights = weights;
        this.addParameters(weights);
        this.intercept = intercept;
        this.addParameters(intercept);
    }

    protected Integer infer(UQI uqi, Item item){
        //Decision Algorithm (formula: w^T x + b;
        // formula > 0 : 1
        // formula = 0 : 0
        // formula < 0 : -1)
        float formula = Float.valueOf(0);
        //Obtains vector from point on plane to point from collected data
        // and performs dot product against normal vector
        for(int i = 0; i < weights.size(); i++){
            formula += weights.get(i) * ((Number) item.getValueByField(inputFields.get(i))).floatValue();
        }
        formula += intercept;

        //Returns 1: same direction as normal vector, 0: on the plane, -1: opposite of normal
        if(formula > 0){
            return 1;
        }
        else if(formula == 0){
            return 0;
        }
        else{
            return -1;
        }
    }
}
