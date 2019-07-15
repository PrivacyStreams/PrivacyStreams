package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONKMeans extends JSONMachineLearning{
    public class Model{
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

    JSONKMeans(List<String> inputFields, Model model){
        super("K-Means");
        this.model = model;
        this.inputFields = inputFields;
    }

    JSONKMeans(List<String> inputFields, List<List<Float>> clusterCenters){
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

    public List<List<Float>> getClusterCenters(){
        return model.clusterCenters;
    }

}
