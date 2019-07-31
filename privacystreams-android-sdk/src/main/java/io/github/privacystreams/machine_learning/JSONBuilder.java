package io.github.privacystreams.machine_learning;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.privacystreams.utils.Assertions;

public class JSONBuilder {
    public static String LINEAR_REGRESSION(List<Float> weights, float intercept, List<String> featureFields){
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.size() == featureFields.size());

        JSONLinearRegression j = new JSONLinearRegression(featureFields, weights, intercept);
        return new Gson().toJson(j);
    }

    public static String LINEAR_REGRESSION(float[] weights, float intercept,
                                                          String ... featureFields) {
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.length == featureFields.length);
        List<Float> w = new ArrayList<>();
        for(float f : weights){
            w.add(Float.valueOf(f));
        }
        JSONLinearRegression j = new JSONLinearRegression(Arrays.asList(featureFields), w, intercept);
        return new Gson().toJson(j);
    }


    public static String KMEANS(List<List<Double>> clusterCenters, List<String> featureFields){
        Assertions.isTrue("Length of a clusterCenter and feature fields vectors match.", clusterCenters.get(0).size()== featureFields.size());

        JSONKMeans j = new JSONKMeans(featureFields, clusterCenters);
        return new Gson().toJson(j);
    }

    public static String KMEANS(double[][] clusterCenters, String ... featureFields) {
        Assertions.isTrue("Length of a clusterCenter and feature fields vectors match.", clusterCenters[0].length == featureFields.length);
        List<List<Double>> cc = new ArrayList<>();
        for (double[] center : clusterCenters) {
            List<Double> temp = new ArrayList<>();
            for (double f : center) {
                temp.add(Double.valueOf(f));
            }
            cc.add(temp);
        }

        JSONKMeans j = new JSONKMeans(Arrays.asList(featureFields), cc);
        return new Gson().toJson(j);
    }

    public static String SVM(List<Float> weights, float intercept, List<String> featureFields){
        JSONSVM j = new JSONSVM(featureFields, weights, intercept);
        return new Gson().toJson(j);
    }

    public static String SVM(float[] weights, float intercept, String ... featureFields) {
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.length == featureFields.length);
        List<Float> w = new ArrayList<>();
        for (float f : weights) {
            w.add(Float.valueOf(f));
        }

        JSONSVM j = new JSONSVM(Arrays.asList(featureFields), w, intercept);
        return new Gson().toJson(j);
    }

}
