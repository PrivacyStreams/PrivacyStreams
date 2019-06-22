package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONSVM extends JSONMachineLearning{
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

    JSONSVM(){

    }
    JSONSVM(String algorithm, List<String> inputFields, Model model){
        super(algorithm);
        this.inputFields = inputFields;
        this.model = model;
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
