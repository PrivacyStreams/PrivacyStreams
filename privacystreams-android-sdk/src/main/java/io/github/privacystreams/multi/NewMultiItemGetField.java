package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;

class NewMultiItemGetField extends MultiProcessor<Object> {
    ItemType.iType type;
    String field;

    NewMultiItemGetField(ItemType.iType type, String field){
        this.type = type;
        this.field = field;
    }

    protected Object processMulti(UQI uqi, Item item){
        List<ItemWrapper> itemws = item.getValueByField("items");
        ItemWrapper i = itemws.get(0);
        int j = 0;
        while(!i.getType().equals(this.type) && j < itemws.size()){
            j++;
            i = itemws.get(j);
        }
        if(j == itemws.size()){
            this.raiseException(uqi, PSException.FAILED("DATATYPE NOT PRESENT"));
        }
        if(i.isList()){
            List<Object> res = new ArrayList<>();

            for(Item l : i.getList()){
                res.add(l.getValueByField(field));
            }
            return res;
        }
        else{
            return i.getItem().getValueByField(field);
        }
    }
}
