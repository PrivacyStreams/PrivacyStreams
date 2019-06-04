package io.github.privacystreams.machine_learning;

import java.util.List;
import java.util.Vector;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class Tuple extends MLProcessor<Vector<Object>>{
    Vector<Object> tuple;

    Tuple(List<String> inputFields){
        super(inputFields);
    }

    @Override
    protected Vector<Object> infer(UQI uqi, Item item){
        this.tuple = new Vector<Object>(inputFields.size());
        this.addParameters(this.tuple);
        for(int i = 0; i < this.inputFields.size(); i++){
            tuple.add(item.getValueByField(inputFields.get(i)));
        }
        return tuple;
    }
}
