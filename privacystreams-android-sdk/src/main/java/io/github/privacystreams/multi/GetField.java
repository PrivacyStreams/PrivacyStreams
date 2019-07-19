package io.github.privacystreams.multi;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

public class GetField extends ItemOperator<Object> {
    private String fieldName;

    public GetField(String fieldName){
        this.fieldName = fieldName;
    }
    public Object apply (UQI uqi, Item item){
        return item.getValueByField(fieldName);
    }
}
