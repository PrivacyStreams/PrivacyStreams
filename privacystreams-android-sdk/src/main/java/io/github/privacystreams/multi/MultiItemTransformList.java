package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

class MultiItemTransformList extends MultiProcessor<List<Object>>{

    private int logItemIndex;
    private Function operator;

    MultiItemTransformList(int logItemIndex, Function operator){
        this.logItemIndex = logItemIndex;
        this.operator = operator;
    }

    protected List<Object> processMulti(UQI uqi, Item item){
        List<List<Item>> items = item.getValueByField("items");
        List<Item> specificItemList = (List<Item>) items.get(logItemIndex);

        List<Object> resultList = new ArrayList<>();

        for(Item i : specificItemList){
            resultList.add(operator.apply(uqi, i));
        }
        return resultList;
    }
}
