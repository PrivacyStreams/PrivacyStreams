package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class SVM extends MLProcessor<Object>{

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
        List<Float> point = new ArrayList<>();
        for(String field : inputFields){
            Number t = item.getValueByField(field);
            point.add(t.floatValue());
        }
        float normal = Float.valueOf(0);
        //Obtains vector from point on plane to point from collected data
        // and performs dot product against normal vector
        for(int i = 0; i < weights.size(); i++){
            float pointOnPlane;
            if(i < weights.size()-1){
                pointOnPlane = 0;
            }
            else{
                pointOnPlane = intercept;
            }
            normal += weights.get(i) * (point.get(i) - pointOnPlane);
        }

        //Returns 1: same direction as normal vector, 0: on the plane, -1: opposite of normal
        if(normal > 0){
            return 1;
        }
        else if(normal == 0){
            return 0;
        }
        else{
            return -1;
        }
    }
}
