package com.github.privacystreams.core.utils.permission;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.utils.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 30/12/2016.
 * Fine-grained permission provided by PrivacyStreams
 */

public class PermissionUtils {
    public static <Tout> void report(Function<Void, Tout> function) {
        Logging.debug("Getting personal data.");
        Logging.debug("Function: " + function.toString());
    }
}
