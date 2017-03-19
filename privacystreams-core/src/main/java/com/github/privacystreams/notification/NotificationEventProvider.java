package com.github.privacystreams.notification;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
abstract class NotificationEventProvider extends MStreamProvider {

    private boolean registered = false;

    @Override
    protected void provide() {
        MyNotificationListenerService.registerProvider(this);
        registered = true;
    }

    @Override
    protected void onCancelled(UQI uqi) {
        MyNotificationListenerService.unregisterProvider(this);
        registered = false;
    }

    public abstract void handleNotificationEvent(String categoryName, String packageName,
                                                 String titleName,
                                                 String textName,
                                                 String action);
}
