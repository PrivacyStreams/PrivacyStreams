package com.github.privacystreams.notification;

import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import java.util.HashSet;
import java.util.Set;

import static com.github.privacystreams.notification.Notification.ACTION_POSTED;
import static com.github.privacystreams.notification.Notification.ACTION_REMOVED;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PSNotificationListenerService extends NotificationListenerService {
    private static Set<NotificationEventProvider> notificationEventProviders = new HashSet<>();

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        for(NotificationEventProvider provider : notificationEventProviders)
            provider.handleNotificationEvent(sbn, ACTION_POSTED);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        for(NotificationEventProvider provider : notificationEventProviders)
            provider.handleNotificationEvent(sbn, ACTION_REMOVED);
    }

    static void registerProvider(NotificationEventProvider provider){
        notificationEventProviders.add(provider);
    }

    static void unregisterProvider(NotificationEventProvider provider){
        notificationEventProviders.remove(provider);
    }
}
