package com.github.privacystreams.commons.debug;

import android.util.Log;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

import java.util.Locale;

/**
 * Print the item for debugging
 */

final class EchoPrinter<T> extends Function<T, T> {

    private final String logTag;
    private final boolean sendToSocket;

    EchoPrinter(String logTag, boolean sendToSocket) {
        this.logTag = Assertions.notNull("logTag", logTag);
        this.sendToSocket = sendToSocket;
        if (sendToSocket) this.addRequiredPermissions(android.Manifest.permission.INTERNET);
    }

    @Override
    public T apply(UQI uqi, T input) {
        Log.d(this.logTag, "" + input);
        if (sendToSocket) {
            String message = String.format(Locale.getDefault(), "%s >>> %s", this.logTag, "" + input);
            PSDebugSocketServer.v().send(message);
        }
        return input;
    }

}
