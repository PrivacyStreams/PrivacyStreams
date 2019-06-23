package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class KMeans extends MLProcessor<Object>{

    List<List<Float>> clusterCenters;

    KMeans(List<String> inputFields, List<List<Float>> clusterCenters){
        super(inputFields);
        this.clusterCenters = clusterCenters;
        this.addParameters(clusterCenters);
    }

    private class KDistance{
        int label;
        float distance;
        KDistance(int label, float distance){
            this.label = label;
            this.distance = distance;
        }
        public int getLabel(){
            return this.label;
        }
        public float getDistance(){
            return this.distance;
        }
    }
    protected Integer infer(UQI uqi, Item item){
        List<Float> point = new ArrayList<>();
        for(int i = 0; i < inputFields.size(); i++){
            Number n = item.getValueByField(inputFields.get(i));
            point.add(n.floatValue());
        }

        List<KDistance> kDistances = new ArrayList<>();
        for(int i = 0; i < clusterCenters.size(); i++){
            float distance = 0;
            List<Float> clusterCenter = clusterCenters.get(i);
            for(int j = 0; j < point.size(); j++){
                distance+=(point.get(j) - clusterCenter.get(j)) * (point.get(j) - clusterCenter.get(j));
            }
            double dd = Float.valueOf(distance).doubleValue();
            double sqrtdd = Math.sqrt(dd);
            distance = Double.valueOf(sqrtdd).floatValue();
            kDistances.add(new KDistance(i, distance));
        }

        Collections.sort(kDistances, new Comparator<KDistance>() {
            @Override
            public int compare(KDistance o1, KDistance o2) {
                return Float.compare(o2.getDistance(), o1.getDistance());
            }
        });

        return kDistances.get(0).getLabel();
    }
}
