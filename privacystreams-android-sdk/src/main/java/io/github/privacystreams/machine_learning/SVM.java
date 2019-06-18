package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class SVM extends MLProcessor<Object>{

    List<Double> normalVector;
    List<Double> pointOnPlane;

    SVM(List<String> inputFields, List<Double> normalVector, List<Double> pointOnPlane){
        super(inputFields);
        this.normalVector = normalVector;
        this.addParameters(normalVector);
        this.pointOnPlane = pointOnPlane;
        this.addParameters(pointOnPlane);
    }

    protected Integer infer(UQI uqi, Item item){
        List<Double> point = new ArrayList<>();
        for(String field : inputFields){
            point.add(item.getAsDouble(field));
        }
        Double normal = 0.0;
        //Obtains vector from point on plane to point from collected data
        // and performs dot product against normal vector
        for(int i = 0; i < normalVector.size(); i++){
            normal += normalVector.get(i) * (point.get(i) - pointOnPlane.get(i));
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
