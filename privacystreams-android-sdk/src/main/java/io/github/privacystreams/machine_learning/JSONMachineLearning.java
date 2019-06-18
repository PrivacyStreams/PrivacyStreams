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
    class FieldWeightPair{
        private String field;
        private float weight;

        FieldWeightPair(String field, float weight){
            this.field = field;
            this.weight = weight;
        }

        public String getField(){
            return this.field;
        }
        public float getWeight(){
            return this.weight;
        }
    }
}
