package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Provide a live stream of SMS messages
 */

class SMSMessageUpdatesProvider extends MStreamProvider {

    SMSMessageUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_SMS);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }
}
