package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Created by yuanchun on 21/11/2016.
 * provide a stream of Message messages
 */

class SMSMessageHistoryProvider extends MStreamProvider {

    public SMSMessageHistoryProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_SMS);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }

}
