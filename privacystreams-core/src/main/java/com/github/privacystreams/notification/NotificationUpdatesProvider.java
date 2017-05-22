package com.github.privacystreams.notification;


import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

class NotificationUpdatesProvider extends NotificationEventProvider {

    NotificationUpdatesProvider() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleNotificationEvent(StatusBarNotification sbn, String action){
        if (sbn == null) return;
        this.output(new Notification(sbn, action));
    }

//    public void handleNotificationEvent(StatusBarNotification sbn, String action, String contactName){
//        if (sbn == null) return;
//        this.output(new Notification(sbn, contactName));
//    }
}
