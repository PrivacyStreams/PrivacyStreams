package io.github.privacystreams.machine_learning;


class JSONMachineLearning {
    private String algorithm;
    JSONMachineLearning(){

    }
    JSONMachineLearning(String algorithm){
        this.algorithm = algorithm;
    }

    public String getAlgorithm(){
        return this.algorithm;
    }
}
