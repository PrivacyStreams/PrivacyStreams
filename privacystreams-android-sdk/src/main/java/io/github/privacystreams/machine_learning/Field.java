package io.github.privacystreams.machine_learning;

import java.util.Vector;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class Field extends MLProcessor<Object>{
    Object object;
    Field(Object object){
        super(new Vector<String>());
        this.object = object;
        this.addParameters(object);
    }

    @Override
    protected Object infer(UQI uqi, Item item){
        return object;
    }
}
