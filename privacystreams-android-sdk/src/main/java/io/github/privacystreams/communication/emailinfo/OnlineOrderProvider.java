package io.github.privacystreams.communication.emailinfo;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;

public class OnlineOrderProvider extends EmailInfoProvider {
    private static final String domain = "order";

    public OnlineOrderProvider(String api_key, String api_secret){
        super(api_key,api_secret,domain);
    }

    @Override
    protected void provide() {
        super.provide();
    }
}
