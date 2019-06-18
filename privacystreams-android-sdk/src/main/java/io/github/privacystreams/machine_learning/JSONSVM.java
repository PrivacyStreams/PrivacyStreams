package io.github.privacystreams.machine_learning;

import java.util.List;

class JSONSVM extends JSONMachineLearning{
    private List<String> inputFields;
    private List<Float> plane;
    private List<Float> normalVector;
    private List<Float> pointOnPlane;

    JSONSVM(){

    }
    JSONSVM(String algorithm, List<String> inputFields, List<Float> normalVector, List<Float> pointOnPlane){
        super(algorithm);
        this.inputFields = inputFields;
        this.normalVector = normalVector;
        this.pointOnPlane = pointOnPlane;
    }

    public List<String> getInputFields(){
        return this.inputFields;
    }

    public List<Float> getNormalVector(){
        return this.normalVector;
    }

    public List<Float> getPointOnPlane(){
        return this.pointOnPlane;
    }
}
