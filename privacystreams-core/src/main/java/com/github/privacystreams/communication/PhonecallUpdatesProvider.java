package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Created by fanglinchen on 2/26/17.
 */

public class PhonecallUpdatesProvider extends MStreamProvider {

    PhonecallUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.PROCESS_OUTGOING_CALLS);
        this.addRequiredPermissions(Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }
}
