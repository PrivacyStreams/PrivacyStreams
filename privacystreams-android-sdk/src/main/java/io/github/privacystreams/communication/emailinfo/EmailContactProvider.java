package io.github.privacystreams.communication.emailinfo;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Logging;


public class EmailContactProvider extends EmailInfoProvider {

    private static final String domain = "contact";

    public EmailContactProvider(String api_key, String api_secret){
        super(api_key,api_secret,domain);
    }

    @Override
    protected void provide() {
        super.provide();
       // Logging.error("now list sifts");
        //listSifts(userName,null,null,domain);
    }
}
