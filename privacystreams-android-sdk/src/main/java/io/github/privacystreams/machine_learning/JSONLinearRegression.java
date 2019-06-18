package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

class JSONLinearRegression extends JSONMachineLearning{
    private List<String> inputFields;
    private List<Float> weights;
    private List<FieldWeightPair> fieldWeightPairs;

    JSONLinearRegression(){

    }

    JSONLinearRegression(String algorithm, List<FieldWeightPair> fieldWeightPairs){
        super(algorithm);
        this.fieldWeightPairs = fieldWeightPairs;
    }

    JSONLinearRegression(String algorithm, List<String> inputFields, List<Float> weights){
        super(algorithm);
        this.inputFields = inputFields;
        this.weights = weights;
    }

    public List<String> getInputFields(){
        if(inputFields == null){
            inputFields = new ArrayList<>();
            for(FieldWeightPair fwp : fieldWeightPairs){
                inputFields.add(fwp.getField());
            }
        }
        return inputFields;
    }

    public List<Float> getWeights(){
        if(weights == null){
            weights = new ArrayList<>();
            for(FieldWeightPair fwp : fieldWeightPairs){
                weights.add(fwp.getWeight());
            }
        }
        return weights;
    }
}
