package io.github.privacystreams.multi;
import com.google.gson.Gson;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;

public class MultiItem extends Item {
    private FeatureProvider[] fp;
    private List<Item> items;

    MultiItem(FeatureProvider[] fp, List<Item> items){
        this.fp = fp;
        this.items = items;
    }

    public static PStreamProvider once(FeatureProvider ... featureProviders) {
        return new MultiItemOnce(featureProviders);
    }

    public static PStreamProvider periodic(long interval, FeatureProvider ... fp) {
        return new MultiItemPeriodic(interval, fp);
    }

    public static PStreamProvider fromJSON(String json){
        JSONmulti jm = new Gson().fromJson(json, JSONmulti.class);
        if(jm.getInterval() == 0){
            return new MultiItemOnce(jm.getFeatureProviders());
        }
        else{
            return new MultiItemPeriodic(jm.getInterval(), jm.getFeatureProviders());
        }
    }

}
