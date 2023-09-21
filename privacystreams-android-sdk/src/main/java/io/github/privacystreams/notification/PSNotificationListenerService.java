package io.github.privacystreams.notification;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import java.util.HashSet;
import java.util.Set;

import static io.github.privacystreams.notification.Notification.ACTION_POSTED;
import static io.github.privacystreams.notification.Notification.ACTION_REMOVED;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PSNotificationListenerService extends NotificationListenerService {
    private static Set<NotificationEventProvider> notificationEventProviders = new HashSet<>();
    private final static String TAG = "PSNotificationListener";
    public static boolean enabled = false;

    @Override
    public IBinder onBind(Intent intent) {
        enabled = true;
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        enabled = false;
        return super.onUnbind(intent);
    }

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
        for(NotificationEventProvider provider : notificationEventProviders){
            provider.handleNotificationEvent(sbn, ACTION_POSTED);
        };
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
