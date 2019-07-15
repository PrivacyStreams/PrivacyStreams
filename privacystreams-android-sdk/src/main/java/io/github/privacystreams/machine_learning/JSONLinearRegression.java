package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONLinearRegression extends JSONMachineLearning{
    public class Model{
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

    JSONLinearRegression(List<String> inputFields, Model model){
        super("Linear Regression");
        this.model = model;
        this.inputFields = inputFields;
    }

    JSONLinearRegression(List<String> inputFields, List<Float> weights, float intercept){
        super("Linear Regression");
        this.model = new Model(weights, intercept);
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
