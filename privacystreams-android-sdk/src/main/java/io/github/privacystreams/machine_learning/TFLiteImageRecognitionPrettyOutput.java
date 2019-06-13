package io.github.privacystreams.machine_learning;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class TFLiteImageRecognitionPrettyOutput extends MLProcessor<List<Recognition>>{
    private int maxResults;
    TFLiteImageRecognitionPrettyOutput(String labelProbArrayField, String labelField, int maxResults){
        super(Arrays.asList(new String[]{labelField, labelProbArrayField}));
        this.maxResults = maxResults;
    }
    @Override
    protected List<Recognition> infer(UQI uqi, Item item) {
        List<String> labels = item.getValueByField(inputFields.get(0));
        byte[][] labelProbArray = item.getValueByField(inputFields.get(1));

        PriorityQueue<Recognition> pq =
                new PriorityQueue<Recognition>(
                        3,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });
        for (int i = 0; i < labels.size(); ++i) {
            pq.add(
                    new Recognition(
                            "" + i,
                            labels.size() > i ? labels.get(i) : "unknown",
                            (labelProbArray[0][i] & 0xff) / 255.0f,
                            null));
        }
        final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
        int recognitionsSize = Math.min(pq.size(), maxResults);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

}

