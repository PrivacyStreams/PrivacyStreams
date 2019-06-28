package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class GetIndex extends MultiProcessor<Object>{
    private String field;
    private int index;

    GetIndex(String field, int index){
        this.field = field;
        this.index = index;
    }

    protected Object processMulti(UQI uqi, Item item){
        List<Object> objects = item.getValueByField(field);
        return objects.get(index);
    }
}
