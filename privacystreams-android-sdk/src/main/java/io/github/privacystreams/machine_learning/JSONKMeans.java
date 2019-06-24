package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONKMeans extends JSONMachineLearning{
    private class Model{
        private List<List<Float>> clusterCenters;
        Model(){

        }
        Model(List<List<Float>> clusterCenters){
            this.clusterCenters = clusterCenters;
        }
    }
    private Model model;
    private List<String> inputFields;

    JSONKMeans(){

    }

    JSONKMeans(String algorithm, List<String> inputFields, Model model){
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

    public List<List<Float>> getClusterCenters(){
        return model.clusterCenters;
    }

}
