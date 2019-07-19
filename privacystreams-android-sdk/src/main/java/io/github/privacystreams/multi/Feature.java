package io.github.privacystreams.multi;

import io.github.privacystreams.core.Function;

public class Feature {
    private Function operator;
    private String featureName;

    public Feature(Function operator, String featureName){
        this.operator = operator;
        this.featureName = featureName;
    }

    public Function getOperator(){
        return this.operator;
    }

    public String getFeatureName(){
        return this.featureName;
    }
}
