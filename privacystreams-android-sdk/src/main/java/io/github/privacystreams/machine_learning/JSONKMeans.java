package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONKMeans extends JSONMachineLearning{
    public class Model{
        private List<List<Double>> clusterCenters;
        Model(){

        }
        Model(List<List<Double>> clusterCenters){
            this.clusterCenters = clusterCenters;
        }
    }
    private Model model;
    private List<String> inputFields;

    JSONKMeans(){

    }

    JSONKMeans(List<String> inputFields, Model model){
        super("K-Means");
        this.model = model;
        this.inputFields = inputFields;
    }

    JSONKMeans(List<String> inputFields, List<List<Double>> clusterCenters){
        super("K-Means");
        this.model = new Model(clusterCenters);
        this.inputFields = inputFields;
    }

    public List<String> getInputFields(){
        return inputFields;
    }

    public Model getModel(){
        return model;
    }

    public List<List<Double>> getClusterCenters(){
        return model.clusterCenters;
    }

}
