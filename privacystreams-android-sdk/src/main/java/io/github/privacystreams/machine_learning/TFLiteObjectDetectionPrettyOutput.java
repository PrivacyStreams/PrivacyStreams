package io.github.privacystreams.machine_learning;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;


class TFLiteObjectDetectionPrettyOutput extends MLProcessor<List<Recognition>>{

    private int numDetections;
    private String labelField;
    float[][][] outputLocations;
    float[][] outputClasses;
    float[][] outputScores;

    TFLiteObjectDetectionPrettyOutput(List<String> inputFields, String labelField, int numDetections){
        super(inputFields);
        this.labelField = labelField;
        this.numDetections = numDetections;
    }

    @Override
    protected List<Recognition> infer(UQI uqi, Item item) {
        outputLocations = item.getValueByField(inputFields.get(0));
        outputClasses = item.getValueByField(inputFields.get(1));
        outputScores = item.getValueByField(inputFields.get(2));

        Vector<Recognition> recognitions = new Vector<>();
        Vector<String> labels = item.getValueByField(labelField);
        for (int i = 0; i < numDetections; i++) {
            int labelOffset = 1;
            recognitions.add(new Recognition(labels.get((int)outputClasses[0][i]+labelOffset),
                    outputScores[0][i],
                    outputLocations[0][i]));
        }
        Collections.sort(recognitions, new Comparator<Recognition>() {
            @Override
            public int compare(Recognition o1, Recognition o2) {
                return Float.compare(o2.getConfidence(), o1.getConfidence());
            }
        });
        return recognitions;
    }
}
