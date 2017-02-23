package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * provide a stream of Message messages
 */

class SMSMessageHistoryProvider extends MultiItemStreamProvider {

    public SMSMessageHistoryProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_SMS);
    }

    @Override
    protected void provide(MultiItemStream output) {
        // TODO implement this
    }

}
