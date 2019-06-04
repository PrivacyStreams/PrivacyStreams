package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MLOperators {
    /**
     * Linear Regression
     *
     * @param inputFields, the name of the data fields.
     * @param weights associated with the fields
     * @return the model result.
     */
    public static Function<Item, Double> linearRegression(List<String> inputFields, List<Double> weights) {
        return new LinearRegression(inputFields, weights);
    }

    /**
     * Simple 2D SVM Classifier, distinguishes between 2 cases
     * @param inputFields
     * @param func maps the inputField values into a 2D vector
     * @param outputs Vector of size 2 which denote what to print depending on what side of the line
     * @return result of SVM and output result
     */
    /*public static Function<Item, String> simpleSVM(Vector<String> inputFields, Function<Vector<Object>>, Vector<String> outputs){
        return new classifier
    }*/

    /**
     * Used for grouping together several fields into a tuple,
     * easier to view multiple outputs at once
     * @param inputFields
     * @return the values of the fields specified as a vector (in order)
     */
    public static Function<Item, Vector<Object>> tuple(List<String> inputFields){
        return new Tuple(inputFields);
    }

    /**
     * Java-ML Library stores things as Instances and Datasets,
     * create an instance with the specified fields
     * @param inputFields fields to include
     * @return instance created from the fields
     */
    /*public static Function<Item, Instance> createInstance(Vector<String> inputFields){
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
    public static Function<Item, Object> tfLiteInferInterpreter(String inputField, Object output, Interpreter tflite){
        return new TFLiteInterpreter(inputField, output, tflite);
    }

    /**
     * Takes a TF Lite Model Interpreter
     * User can customize the interpreter themselves and supply to function
     * Multiple inputs, Multiple output version
     * @param inputFields input tensors
     * @param outputs map of order of outputs
     * @param tflite Interpreter
     * @return
     */
    public static Function<Item, Object> tfLiteInferInterpreter(List<String> inputFields, Map<Integer, Object> outputs, Interpreter tflite){
        return new TFLiteInterpreterOutputs(inputFields, outputs, tflite);
    }

    /**
     *
     * @param inputField
     * @param output
     * @param model
     * @return
     */
    public static Function<Item, Object> tfLiteInferModel(String inputField, Object output, File model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, output, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    public static Function<Item, Object> tfLiteInferModel(String inputField, Object output, MappedByteBuffer model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputField, output, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }
    public static Function<Item, Object> tfLiteInferModel(List<String> inputFields, Map<Integer, Object> outputs, File model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputFields, outputs, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }
    public static Function<Item, Object> tfLiteInferModel(List<String> inputFields, Map<Integer, Object> outputs, MappedByteBuffer model){
        try {
            Interpreter tflite = new Interpreter(model);
            return tfLiteInferInterpreter(inputFields, outputs, tflite);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("Wrong model argument");
            return null;
        }
    }

    public static Function<Item, Object> textRecognitionText(String inputField){
        return new MLKitTextRecognition(inputField, true);
    }

    public static Function<Item, Object> textRecognitionAll(String inputField){
        return new MLKitTextRecognition(inputField, false);
    }
}
