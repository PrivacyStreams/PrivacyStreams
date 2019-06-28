package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.Assertions;

class NewTransformList extends MultiProcessor<List<Object>>{
    private ItemType.iType type;
    private Function operator;

    NewTransformList(ItemType.iType type, Function operator){
        this.type = type;
        this.operator = operator;
    }

    protected List<Object> processMulti(UQI uqi, Item item){
        List<ItemWrapper> items = item.getValueByField("items");

        ItemWrapper i = items.get(0);
        int j = 0;
        while(!i.getType().equals(this.type) && j < items.size()){
            j++;
            i = items.get(j);
        }
        if(j == items.size()){
            this.raiseException(uqi, PSException.FAILED("DATATYPE NOT PRESENT"));
        }

        Assertions.isTrue("item type is list", i.isList());
        List<Item> ii = i.getList();
        List<Object> resultList = new ArrayList<>();

        for(Item iitem : ii){
            resultList.add(operator.apply(uqi, iitem));
        }
        return resultList;
    }
}
