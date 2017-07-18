package com.github.privacystreams.commons.debug;

import android.util.Log;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.PSDebugSocketServer;

import java.util.Locale;

/**
 * Print the item for debugging
 */

final class EchoOperator<T> extends Function<T, T> {

    private final String logTag;
    private final boolean sendToSocket;

    EchoOperator(String logTag, boolean sendOverSocket) {
        this.logTag = Assertions.notNull("logTag", logTag);
        this.sendToSocket = sendOverSocket;
        if (sendOverSocket) this.addRequiredPermissions(android.Manifest.permission.INTERNET);
    }

    @Override
    public T apply(UQI uqi, T input) {
        String logMsg = "";
        if (input instanceof Item) logMsg = ((Item) input).toJson().toString();
        else logMsg = "" + input;

        if (sendToSocket) {
            String message = String.format(Locale.getDefault(), "%s >>> %s", this.logTag, logMsg);
            PSDebugSocketServer.v().send(message);
        }
        else {
            Log.d(this.logTag, logMsg);
        }
        return input;
    }

}
