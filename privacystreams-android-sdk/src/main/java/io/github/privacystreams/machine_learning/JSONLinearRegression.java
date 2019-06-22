package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONLinearRegression extends JSONMachineLearning{
    private class Model{
        private List<Float> weights;
        private float intercept;
        Model(){

        }
        Model(List<Float> weights, float intercept){
            this.weights = weights;
            this.intercept = intercept;
        }
    }
    private Model model;
    private List<String> inputFields;

    JSONLinearRegression(){

    }

    JSONLinearRegression(String algorithm, List<String> inputFields, Model model){
        super(algorithm);
        this.model = model;
        this.inputFields = inputFields;
    }

    public List<String> getInputFields(){
        return inputFields;
    }

    public Model getModel(){
        return model;
    }

    public List<Float> getWeights(){
        return model.weights;
    }

    public float getIntercept(){
        return model.intercept;
    }
}
