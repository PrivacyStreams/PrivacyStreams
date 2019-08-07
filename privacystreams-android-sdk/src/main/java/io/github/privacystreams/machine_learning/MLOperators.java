package io.github.privacystreams.machine_learning;

import android.content.res.AssetManager;

import com.google.gson.Gson;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.Assertions;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MLOperators {
    public static String loadJSONFromAsset(AssetManager assets, String jsonFileName) {
        String json = null;
        try {
            InputStream is = assets.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Function<Item, Number> machineLearning(AssetManager assetManager, String jsonFileName){
        return machineLearning(loadJSONFromAsset(assetManager, jsonFileName));
    }

    public static Function<Item, Number> machineLearning(String json){
        Gson gson = new Gson();

        System.out.println("Performing: " + gson.fromJson(json, JSONMachineLearning.class));

        switch (gson.fromJson(json, JSONMachineLearning.class).getAlgorithm()) {
            case "Linear Regression": {
                JSONLinearRegression jlr = gson.fromJson(json, JSONLinearRegression.class);
                return new LinearRegression(jlr.getInputFields(), jlr.getWeights(), jlr.getIntercept());
            }
            case "SVM": {
                JSONSVM jsvm = gson.fromJson(json, JSONSVM.class);
                return new SVM(jsvm.getInputFields(), jsvm.getWeights(), jsvm.getIntercept());
            }
            case "K-Means": {
                JSONKMeans jk = gson.fromJson(json, JSONKMeans.class);
                return new KMeans(jk.getInputFields(), jk.getClusterCenters());
            }
            default: {
                System.out.println("Unsupported algorithm");
                return null;
            }
        }
    }
    /**
     * Linear Regression
     *
     * @param featureFields, the name of the data fields.
     * @param weights associated with the fields of the linear regression model
     * @param intercept of the linear regression model
     * @return the model result.
     */
    public static Function<Item, Number> linearRegression(List<Float> weights, float intercept, List<String> featureFields) {
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.size() == featureFields.size());
        return new LinearRegression(featureFields, weights, intercept);
    }

    /**
     * SVM
     *
     * @param featureFields, the name of the data fields.
     * @param weights associated with the fields of the svm model
     * @param intercept of the svm model
     * @return the model result.
     */
    public static Function<Item, Number> SVM(List<Float> weights, float intercept, List<String> featureFields){
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.size() == featureFields.size());
        return new SVM(featureFields, weights, intercept);
    }

    /**
     * KMeans
     *
     * @param featureFields, the name of the data fields.
     * @param clusterCenters, coordinates of the centers
     * @return the model result.
     */
    public static Function<Item, Number> kMeans(List<List<Double>> clusterCenters, List<String> featureFields){
        Assertions.isTrue("Length of a clusterCenter and feature fields vectors match.", clusterCenters.get(0).size() == featureFields.size());
        return new KMeans(featureFields, clusterCenters);
    }

    /**
     * Linear Regression
     *
     * @param featureFields, the name of the data fields.
     * @param weights associated with the fields of the linear regression model
     * @param intercept of the linear regression model
     * @return the model result.
     */
    public static Function<Item, Number> linearRegression(float[] weights, float intercept,
                                                          String ... featureFields) {
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.length == featureFields.length);
        List<Float> w = new ArrayList<>();
        for(float f : weights){
            w.add(Float.valueOf(f));
        }
        return new LinearRegression(Arrays.asList(featureFields), w, intercept);
    }

    /**
     * SVM
     *
     * @param featureFields, the name of the data fields.
     * @param weights associated with the fields of the svm model
     * @param intercept of the svm model
     * @return the model result.
     */
    public static Function<Item, Number> SVM(float[] weights, float intercept, String ... featureFields){
        Assertions.isTrue("Length of weights and feature fields vectors match.", weights.length == featureFields.length);
            List<Float> w = new ArrayList<>();
            for(float f : weights){
                w.add(Float.valueOf(f));
        }
        return new SVM(Arrays.asList(featureFields), w, intercept);
    }

    /**
     * KMeans
     *
     * @param featureFields, the name of the data fields.
     * @param clusterCenters, coordinates of the centers
     * @return the model result.
     */
    public static Function<Item, Number> kMeans(double[][] clusterCenters, String ... featureFields){
        Assertions.isTrue("Length of a clusterCenter and feature fields vectors match.", clusterCenters[0].length == featureFields.length);
        List<List<Double>> cc = new ArrayList<>();
        for(double[] center : clusterCenters){
            List<Double> temp = new ArrayList<>();
            for(double f : center){
                temp.add(Double.valueOf(f));
            }
            cc.add(temp);
        }
        return new KMeans(Arrays.asList(featureFields), cc);
    }

    /**
     * Used for grouping together several fields into a tuple,
     * easier to view multiple outputs at once
     * @param inputFields, the fields to group together
     * @return the values of the fields specified as a ArrayList (in order)
     */
    public static Function<Item, ArrayList<Object>> tuple(List<String> inputFields){
        return new Tuple(inputFields);
    }

    /**
     * Used for grouping together several fields into a tuple,
     * easier to view multiple outputs at once
     * @param inputFields, the fields to group together
     * @return the values of the fields specified as a ArrayList (in order)
     */
    public static Function<Item, ArrayList<Object>> tuple(String ... inputFields){
        return new Tuple(Arrays.asList(inputFields));
    }


    public static Function<Item, Object> field(Object object){
        return new Field(object);
    }


    /**
     * Takes a TF Lite Model Interpreter
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param tflite Interpreter
     * @param outputField An object which has the size of the desired output tensor
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, String outputField, Interpreter tflite){
        return new TFLiteInterpreter(inputField, outputField, tflite);
    }

    /**
     * Takes a TF Lite Model Interpreter
     * User can customize the interpreter themselves and supply to function
     * Multiple inputs, Multiple output version
     * @param inputField input tensors
     * @param outputs map of order of outputs
     * @param tflite Interpreter
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, Map<Integer, Object> outputs, Interpreter tflite){
        return new TFLiteInterpreterOutputs(inputField, outputs, tflite);
    }

    /**
     * Takes a TF Lite Model Interpreter
     * User can customize the interpreter themselves and supply to function
     * Multiple inputs, Multiple output version
     * @param inputField input tensors
     * @param outputs list of outputs
     * @param tflite Interpreter
     * @return result of model inference
     */
    // Assumes output arrays already present in item fields
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, List<String> outputs, Interpreter tflite){
        return new TFLiteInterpreterOutputs(inputField, outputs, tflite);
    }

    /**
     * Takes a TF Lite Model File
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param model file of the tflite model
     * @param outputField An object which has the size of the desired output tensor
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferModel(String inputField, String outputField, File model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, outputField, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    /**
     * Takes a TF Lite Model MappedByteBuffer
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param model MappedByteBuffer of the model
     * @param outputField An object which has the size of the desired output tensor
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferModel(String inputField, String outputField, MappedByteBuffer model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, outputField, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    /**
     * Multiple outputs version
     *
     * Takes a TF Lite Model File
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param model file of the tflite model
     * @param outputs map of the order of outputs
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferModel(String inputField, Map<Integer, Object> outputs, File model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, outputs, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    /**
     * Multiple outputs version
     *
     * Takes a TF Lite Model MappedByteBuffer
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param model MappedByteBuffer of the model
     * @param outputs map of the order of outputs
     * @return result of model inference
     */
    public static Function<Item, Object> tfLiteInferModel(String inputField, Map<Integer, Object> outputs, MappedByteBuffer model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, outputs, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    public static Function<Item, Object[]> objectDetectionProcessor(String inputField, int inputSize, boolean isQuantized, Integer sensorOrientation){
        //inputfield for bitmap
        return new TFLiteObjectDetectionProcessor(inputField, inputSize, isQuantized, sensorOrientation);
    }

    public static Function<Item, List<Recognition>> objectDetectionRecognizer(List<String> inputFields, String labelField, int numDetections, int inputSize){
        return new TFLiteObjectDetectionPrettyOutput(inputFields, labelField, numDetections, inputSize);
    }

    public static Function<Item, List<Recognition>> imageRecognitionOutput(String labelProbArrayFields, String labelFields, int maxResults){
        return new TFLiteImageRecognitionPrettyOutput(labelProbArrayFields, labelFields, maxResults);
    }
}
