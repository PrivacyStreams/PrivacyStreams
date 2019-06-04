package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class TFLiteInterpreterOutputs extends MLProcessor<Object> {
    Interpreter tflite;
    Map<Integer, java.lang.Object> outputs;

    TFLiteInterpreterOutputs(List<String> inputFields, Map<Integer, java.lang.Object> outputs, Interpreter tflite){
        super(inputFields);
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.outputs = Assertions.notNull("outputs", outputs);
        this.addParameters(outputs);
    }

    protected Object infer(UQI uqi, Item item){
        java.lang.Object[] inputs = new java.lang.Object[this.inputFields.size()];
        for(int i = 0; i < this.inputFields.size(); i++){
            inputs[i] = item.getValueByField(this.inputFields.get(i));
        }
        tflite.runForMultipleInputsOutputs(inputs, outputs);
        return (Object) outputs;
    }
}
