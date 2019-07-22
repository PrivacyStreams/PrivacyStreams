package io.github.privacystreams.multi;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;

public class VarMultiItem extends Item {
    private FeatureProvider[] fp;
    private List<Item> items;

    VarMultiItem(FeatureProvider[] fp, List<Item> items){
        this.fp = fp;
        this.items = items;
    }

    public static PStreamProvider oneshot(FeatureProvider ... featureProviders) {
        return new VarMultiItemOnce(featureProviders);
    }

    public static PStreamProvider periodic(long interval, FeatureProvider ... fp) {
        return new VarMultiItemPeriodic(interval, fp);
    }

}
