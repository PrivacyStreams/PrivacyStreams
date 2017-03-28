package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Provide a stream of SMS messages
 */

class SMSMessageListProvider extends MStreamProvider {

    public SMSMessageListProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_SMS);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }

}
