package io.github.privacystreams.machine_learning;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class Tuple extends MLProcessor<ArrayList<Object>>{
    ArrayList<Object> tuple;

    Tuple(List<String> inputFields){
        super(inputFields);
    }

    @Override
    protected ArrayList<Object> infer(UQI uqi, Item item){
        this.tuple = new ArrayList<>(inputFields.size());
        this.addParameters(this.tuple);
        for(int i = 0; i < this.inputFields.size(); i++){
            tuple.add(item.getValueByField(inputFields.get(i)));
        }
        return tuple;
    }
}
