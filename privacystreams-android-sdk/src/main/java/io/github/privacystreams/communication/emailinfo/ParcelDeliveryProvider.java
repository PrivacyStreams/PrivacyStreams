package io.github.privacystreams.communication.emailinfo;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;

public class ParcelDeliveryProvider extends EmailInfoProvider {
    private static final String domain = "parceldelivery";

    public ParcelDeliveryProvider(String api_key, String api_secret){
        super(api_key,api_secret,domain);
    }

    @Override
    protected void provide() {
        super.provide();
    }
}
