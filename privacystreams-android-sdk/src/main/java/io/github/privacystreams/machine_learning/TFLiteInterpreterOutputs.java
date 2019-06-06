package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class TFLiteInterpreterOutputs extends MLProcessor<Object> {
    Interpreter tflite;
    Map<Integer, java.lang.Object> outputs;
    String iField;

    TFLiteInterpreterOutputs(String iField, Map<Integer, java.lang.Object> outputs, Interpreter tflite){
        super(Arrays.asList(new String[]{iField}));
        this.iField = Assertions.notNull("iField", iField);
        this.addParameters(iField);
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.outputs = Assertions.notNull("outputs", outputs);
        this.addParameters(outputs);
    }

    protected Object infer(UQI uqi, Item item){
        Object[] inputs = item.getValueByField(iField);
        tflite.runForMultipleInputsOutputs(inputs, outputs);
        return outputs;
    }
}
