package io.github.privacystreams.app;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityEvent;

/**
 * The PrivacyStreams service.
 */

public class PStreamService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
