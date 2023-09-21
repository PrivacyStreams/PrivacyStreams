package io.github.privacystreams.notification;

import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.PermissionUtils;

abstract class NotificationEventProvider extends PStreamProvider {

    NotificationEventProvider() {
        this.addRequiredPermissions(PermissionUtils.USE_NOTIFICATION_SERVICE);
    }

    private boolean registered = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void provide() {
        PSNotificationListenerService.registerProvider(this);
        registered = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCancel(UQI uqi) {
        PSNotificationListenerService.unregisterProvider(this);
        registered = false;
    }

    public abstract void handleNotificationEvent(StatusBarNotification sbn, String action);
}
