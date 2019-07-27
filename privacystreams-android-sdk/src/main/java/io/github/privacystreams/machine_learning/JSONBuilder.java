package io.github.privacystreams.machine_learning;

import com.google.gson.Gson;

import java.util.List;

public class JSONBuilder {
    public static String LINEAR_REGRESSION(List<String> inputFields, List<Float> weights,
                                                                    float intercept){
        JSONLinearRegression j = new JSONLinearRegression(inputFields, weights, intercept);
        return new Gson().toJson(j);
    }

    public static String KMEANS(List<String> inputFields, List<List<Double>> clusterCenters){
        JSONKMeans j = new JSONKMeans(inputFields, clusterCenters);
        return new Gson().toJson(j);
    }

    public static String SVM(List<String> inputFields, List<Float> weights, float intercept){
        JSONSVM j = new JSONSVM(inputFields, weights, intercept);
        return new Gson().toJson(j);
    }
}
