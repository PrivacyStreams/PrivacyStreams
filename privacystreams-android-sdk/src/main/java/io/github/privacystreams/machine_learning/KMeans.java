package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class KMeans extends MLProcessor<Number>{

    List<List<Double>> clusterCenters;

    KMeans(List<String> inputFields, List<List<Double>> clusterCenters){
        super(inputFields);
        this.clusterCenters = clusterCenters;
        this.addParameters(clusterCenters);
    }

    private class KDistance{
        int label;
        double distance;
        KDistance(int label, double distance){
            this.label = label;
            this.distance = distance;
        }
        public int getLabel(){
            return this.label;
        }
        public double getDistance(){
            return this.distance;
        }
    }
    protected Integer infer(UQI uqi, Item item){
        List<Double> point = new ArrayList<>();
        for(int i = 0; i < inputFields.size(); i++){
            Number n = item.getValueByField(inputFields.get(i));
            point.add(n.doubleValue());
        }

        List<KDistance> kDistances = new ArrayList<>();
        for(int i = 0; i < clusterCenters.size(); i++){
            double distance = 0;
            List<Double> clusterCenter = clusterCenters.get(i);
            for(int j = 0; j < point.size(); j++){
                distance+=(point.get(j) - clusterCenter.get(j)) * (point.get(j) - clusterCenter.get(j));
            }
            double sqrtdd = Math.sqrt(distance);
            kDistances.add(new KDistance(i, sqrtdd));
        }

        Collections.sort(kDistances, new Comparator<KDistance>() {
            @Override
            public int compare(KDistance o1, KDistance o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });

        return kDistances.get(0).getLabel();
    }
}
