package io.github.privacystreams.multi;

import io.github.privacystreams.core.PStreamProvider;

public class FeatureProvider {
    private PStreamProvider provider;
    private Feature[] features;

    public FeatureProvider(PStreamProvider provider, Feature ... features){
        this.provider = provider;
        this.features = features;
    }

    public PStreamProvider getProvider(){
        return this.provider;
    }

    public Feature[] getFeatures(){
        return this.features;
    }
}
