package com.github.privacystreams.communication;

import android.Manifest;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * a stream of call logs
 */

class PhonecallLogProvider extends MultiItemStreamProvider {

    PhonecallLogProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CALL_LOG);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }

}
