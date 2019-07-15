package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONSVM extends JSONMachineLearning{
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

    JSONSVM(){

    }
    JSONSVM(List<String> inputFields, Model model){
        super("SVM");
        this.inputFields = inputFields;
        this.model = model;
    }

    JSONSVM(List<String> inputFields, List<Float> weights, float intercept){
        super("SVM");
        this.inputFields = inputFields;
        this.model = new Model(weights, intercept);
    }

    public List<String> getInputFields(){
        return this.inputFields;
    }

    public Model getModel(){
        return this.model;
    }

    public List<Float> getWeights(){
        return this.model.weights;
    }

    public float getIntercept(){
        return this.model.intercept;
    }
}
