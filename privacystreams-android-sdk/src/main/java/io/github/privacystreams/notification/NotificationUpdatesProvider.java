package io.github.privacystreams.notification;


import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

class NotificationUpdatesProvider extends NotificationEventProvider {

    NotificationUpdatesProvider() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleNotificationEvent(StatusBarNotification sbn, String action){
        if (sbn == null) return;
        this.output(new Notification(sbn, action));
    }
}
