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
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MLOperators {
    public String loadJSONFromAsset(AssetManager assets, String jsonFileName) {
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
    public static Function<Item, Object> machineLearning(String json){
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
     * @param weights associated with the fields
     * @return the model result.
     */
    public static Function<Item, Object> linearRegression(List<Float> weights, float intercept, List<String> featureFields) {
        return new LinearRegression(featureFields, weights, intercept);
    }

    public static Function<Item, Object> SVM(List<Float> weights, float intercept, List<String> featureFields){
        return new SVM(featureFields, weights, intercept);
    }

    public static Function<Item, Object> kMeans(List<List<Float>> clusterCenters, List<String> featureFields){
        return new KMeans(featureFields, clusterCenters);
    }

    public static Function<Item, Object> linearRegression(float[] weights, float intercept,
                                                          String ... featureFields) {
        List<Float> w = new ArrayList<>();
        for(float f : weights){
            w.add(Float.valueOf(f));
        }
        return new LinearRegression(Arrays.asList(featureFields), w, intercept);
    }

    public static Function<Item, Object> SVM(float[] weights, float intercept, String ... featureFields){
        List<Float> w = new ArrayList<>();
        for(float f : weights){
            w.add(Float.valueOf(f));
        }
        return new SVM(Arrays.asList(featureFields), w, intercept);
    }

    public static Function<Item, Object> kMeans(float[][] clusterCenters, String ... featureFields){
        List<List<Float>> cc = new ArrayList<>();
        for(float[] center : clusterCenters){
            List<Float> temp = new ArrayList<>();
            for(float f : center){
                temp.add(Float.valueOf(f));
            }
            cc.add(temp);
        }
        return new KMeans(Arrays.asList(featureFields), cc);
    }

    /**
     * Used for grouping together several fields into a tuple,
     * easier to view multiple outputs at once
     * @param inputFields
     * @return the values of the fields specified as a ArrayList (in order)
     */
    public static Function<Item, ArrayList<Object>> tuple(List<String> inputFields){
        return new Tuple(inputFields);
    }

    public static Function<Item, ArrayList<Object>> tuple(String ... inputFields){
        return new Tuple(Arrays.asList(inputFields));
    }

    public static Function<Item, Object> field(Object object){
        return new Field(object);
    }
    /**
     * Java-ML Library stores things as Instances and Datasets,
     * create an instance with the specified fields
     * @param inputFields fields to include
     * @return instance created from the fields
     */
    /*public static Function<Item, Instance> createInstance(ArrayList<String> inputFields){
        return new Instance;
    }*/

    /**
     * Takes a TF Lite Model Interpreter
     * User can customize the interpreter themselves and supply to function
     * One input, One output version
     * @param inputField field from Item containing tensor (array)
     * @param tflite Interpreter
     * @param outputField An object which has the size of the desired output tensor
     * @return
     * OUTPUT SIZE??
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
     * @return
     */
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, Map<Integer, Object> outputs, Interpreter tflite){
        return new TFLiteInterpreterOutputs(inputField, outputs, tflite);
    }

    // Assumes output arrays already present in item fields
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, List<String> outputs, Interpreter tflite){
        return new TFLiteInterpreterOutputs(inputField, outputs, tflite);
    }


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
