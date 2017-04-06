package com.github.privacystreams.notification;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.github.privacystreams.utils.Logging;

import java.util.HashSet;
import java.util.Set;

import static com.github.privacystreams.notification.Notification.ACTION_POSTED;
import static com.github.privacystreams.notification.Notification.ACTION_REMOVED;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PSNotificationListenerService extends android.service.notification.NotificationListenerService {
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
        if (sbn == null) return;
        Notification mNotification = sbn.getNotification();
        if (mNotification == null) return;
        try{
            Bundle extras = mNotification.extras;
            String notificationText = "";
            String notificationTitle = extras.getString(Notification.EXTRA_TITLE);

            if (extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES) == null){
                CharSequence extraText = extras.getCharSequence(Notification.EXTRA_TEXT);
                if (extraText != null)
                    notificationText = extraText.toString();

                for(NotificationEventProvider provider:notificationEventProviders)
                    provider.handleNotificationEvent(mNotification.category,
                            sbn.getPackageName(),
                            notificationTitle,
                            notificationText,
                            ACTION_POSTED);
            }
            else {
                CharSequence[] csa = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
                if (csa == null) return;
                for (CharSequence cs : csa){
                        notificationText = cs.toString();

                        if (notificationText.contains(": ")){
                            for(NotificationEventProvider provider:notificationEventProviders)
                                provider.handleNotificationEvent(mNotification.category,
                                        sbn.getPackageName(),
                                        notificationText.split(": ")[0],
                                        notificationText.split(": ")[1],
                                        ACTION_POSTED);
                        }
                        else if (notificationTitle != null) {
                            for(NotificationEventProvider provider:notificationEventProviders)
                                provider.handleNotificationEvent(mNotification.category,
                                        sbn.getPackageName(),
                                        notificationTitle.split(" @ ")[0],
                                        notificationText,
                                        ACTION_POSTED);
                        }
                }
            }

        } catch(Exception exception){
            Logging.warn(exception.getMessage());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn == null) return;
        Notification mNotification = sbn.getNotification();
        if (mNotification == null) return;
        String notificationTitle = mNotification.extras.getString(Notification.EXTRA_TITLE);
        for(NotificationEventProvider provider:notificationEventProviders)
            provider.handleNotificationEvent(mNotification.category,
                    sbn.getPackageName(),
                    notificationTitle,
                    mNotification.tickerText.toString(),
                    ACTION_REMOVED);

    }

    static void registerProvider(NotificationEventProvider provider){
        notificationEventProviders.add(provider);
    }

    static void unregisterProvider(NotificationEventProvider provider){
        notificationEventProviders.remove(provider);
    }
}
