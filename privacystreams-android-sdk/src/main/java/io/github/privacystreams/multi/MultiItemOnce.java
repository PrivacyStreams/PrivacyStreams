package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.Assertions;

public class MultiItemOnce extends PStreamProvider {
    private FeatureProvider[] features;

    MultiItemOnce(FeatureProvider[] features){
        this.features = features;
    }

    public FeatureProvider[] getFeatureProvider(){
        return this.features;
    }

    protected void provide(){
        MultiItem multiItem = null;
        try {
            multiItem = recordOnce(this.getUQI(), this.features);
            this.output(multiItem);
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiRecording failed."));
        }
        this.finish();
    }

    // ASSUMPTION: PROVIDERS PROVIDE ONE ITEM
    static MultiItem recordOnce(UQI uqi, FeatureProvider[] features){
        List<Item> items = new ArrayList<>();
        MultiItem multiItem = new MultiItem(features, items);

        for(FeatureProvider fp : features){
            Item item = null;
            try{
                item = uqi.getData(fp.getProvider(), Purpose.LIB_INTERNAL("Multi-Item Construction")).getFirst();
                items.add(item);
            } catch (PSException pe){
                pe.printStackTrace();
                System.out.println("Failed in retrieving provider for multi-item");
            }
            Assertions.notNull("Item from multi-item", item);
            for(Feature f : fp.getFeatures()){
                multiItem.setFieldValue(f.getFeatureName(), f.getOperator().apply(uqi, item));
                item.setFieldValue(f.getFeatureName(), f.getOperator().apply(uqi, item));
            }
        }
        return multiItem;
    }
}