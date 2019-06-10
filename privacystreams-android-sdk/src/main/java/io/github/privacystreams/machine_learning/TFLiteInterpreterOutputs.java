package io.github.privacystreams.machine_learning;

import org.tensorflow.lite.Interpreter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class TFLiteInterpreterOutputs extends MLProcessor<Object> {
    Interpreter tflite;
    Map<Integer, java.lang.Object> outputs;
    List<String> outputFields;
    String iField;
    boolean asOutputFields = false;

    TFLiteInterpreterOutputs(String iField, Map<Integer, java.lang.Object> outputs, Interpreter tflite){
        super(Arrays.asList(new String[]{iField}));
        this.iField = Assertions.notNull("iField", iField);
        this.addParameters(iField);
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.outputs = Assertions.notNull("outputs", outputs);
        this.addParameters(outputs);
    }

    TFLiteInterpreterOutputs(String iField, List<String> outputFields, Interpreter tflite){
        super(Arrays.asList(new String[]{iField}));
        this.iField = Assertions.notNull("iField", iField);
        this.addParameters(iField);
        this.tflite = Assertions.notNull("tflite", tflite);
        this.addParameters(tflite);
        this.outputFields = Assertions.notNull("outputFields", outputFields);
        this.addParameters(outputFields);
        asOutputFields = true;
    }

    protected Object infer(UQI uqi, Item item){
        if(asOutputFields){
            outputs = new HashMap<>();
            for(int i = 0; i < outputFields.size(); i++){
                outputs.put(i, item.getValueByField(outputFields.get(i)));
            }
        }
        Object[] inputs = item.getValueByField(iField);
        tflite.runForMultipleInputsOutputs(inputs, outputs);
        return outputs;
    }
}
