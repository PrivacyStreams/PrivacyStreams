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
            case "linear regression": {
                JSONLinearRegression jlr = gson.fromJson(json, JSONLinearRegression.class);
                return new LinearRegression(jlr.getInputFields(), jlr.getWeights());
            }
            case "SVM": {
                JSONSVM jsvm = gson.fromJson(json, JSONSVM.class);
                return new SVM(jsvm.getInputFields(), jsvm.getNormalVector(), jsvm.getPointOnPlane());
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
     * @param inputFields, the name of the data fields.
     * @param weights associated with the fields
     * @return the model result.
     */
    public static Function<Item, Object> linearRegression(List<String> inputFields, List<Float> weights) {
        return new LinearRegression(inputFields, weights);
    }

    /**
     * Simple 2D SVM Classifier, distinguishes between 2 cases
     * @param inputFields
     * @param func maps the inputField values into a 2D Arraylist
     * @param outputs Arraylist of size 2 which denote what to print depending on what side of the line
     * @return result of SVM and output result
     */
    public static Function<Item, Object> SVM(List<String> inputFields, List<Float> normalVector, List<Float> pointOnPlane){
        return new SVM(inputFields, normalVector, pointOnPlane);
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
     * @param output An object which has the size of the desired output tensor
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

    /**
     *
     * @param inputField
     * @param output
     * @param model
     * @return
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
