package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.util.Arrays;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class TFLiteInterpreter extends MLProcessor<Object> {
    Interpreter tflite;
    String outputField;
    String inputField;

    TFLiteInterpreter(String inputField, String outputField, Interpreter tflite){
        super(Arrays.asList(inputField));
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.inputField = inputField;
        this.outputField = outputField;
    }

    protected Object infer(UQI uqi, Item item){
        System.out.println(item.getValueByField(this.inputFields.get(0)));
        System.out.println(item.getValueByField(this.outputField));
        tflite.run(item.getValueByField(this.inputFields.get(0)), item.getValueByField(this.outputField));
        return outputField;
    }
}
