package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.util.Arrays;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class TFLiteInterpreter extends MLProcessor<Object> {
    Interpreter tflite;
    Object output;

    TFLiteInterpreter(String inputField, Object output, Interpreter tflite){
        super(Arrays.asList(new String[]{inputField}));
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.output = Assertions.notNull("output", output);
        this.addParameters(output);
    }

    protected Object infer(UQI uqi, Item item){
        tflite.run(item.getValueByField(this.inputFields.get(0)), this.output);
        return this.output;
    }
}
