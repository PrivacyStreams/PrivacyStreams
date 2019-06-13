package io.github.privacystreams.machine_learning;

import android.graphics.RectF;

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
    private int inputSize;

    TFLiteObjectDetectionPrettyOutput(List<String> inputFields, String labelField, int numDetections, int inputSize){
        super(inputFields);
        this.labelField = labelField;
        this.numDetections = numDetections;
        this.inputSize = inputSize;
    }

    @Override
    protected List<Recognition> infer(UQI uqi, Item item) {
        outputLocations = item.getValueByField(inputFields.get(0));
        outputClasses = item.getValueByField(inputFields.get(1));
        outputScores = item.getValueByField(inputFields.get(2));

        Vector<Recognition> recognitions = new Vector<>();
        Vector<String> labels = item.getValueByField(labelField);
        for (int i = 0; i < numDetections; i++) {
            final RectF detection =
                    new RectF(
                            outputLocations[0][i][1] * inputSize,
                            outputLocations[0][i][0] * inputSize,
                            outputLocations[0][i][3] * inputSize,
                            outputLocations[0][i][2] * inputSize);
            // SSD Mobilenet V1 Model assumes class 0 is background class
            // in label file and class labels start from 1 to number_of_classes+1,
            // while outputClasses correspond to class index from 0 to number_of_classes
            int labelOffset = 1;
            recognitions.add(
                    new Recognition(
                            "" + i,
                            labels.get((int) outputClasses[0][i] + labelOffset),
                            outputScores[0][i],
                            detection));
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
