package com.github.privacystreams.notification;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import static com.github.privacystreams.notification.Notification.ACTION_POSTED;
import static com.github.privacystreams.notification.Notification.ACTION_REMOVED;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyNotificationListenerService extends android.service.notification.NotificationListenerService {
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

        try{
            Notification mNotification = sbn.getNotification();

            Bundle extras = mNotification.extras;
            String notificationText = "";
            String notificationTitle = extras.getString(Notification.EXTRA_TITLE);


            if (extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)==null){
                notificationText=extras.getCharSequence(Notification.EXTRA_TEXT).toString();

                for(NotificationEventProvider provider:notificationEventProviders)
                    provider.handleNotificationEvent(mNotification.category,
                            sbn.getPackageName(),
                            notificationTitle,
                            notificationText,
                            ACTION_POSTED);
            }
            else{
                CharSequence[] csa = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
                assert csa != null;
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
                        else {
                            for(NotificationEventProvider provider:notificationEventProviders)
                                provider.handleNotificationEvent(mNotification.category,
                                        sbn.getPackageName(),
                                        notificationTitle.split(" @ ")[0],
                                        notificationText,
                                        ACTION_POSTED);
                        }
                }
            }

        }catch(Exception exception){
            Log.e("exception",exception.toString());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        Notification mNotification = sbn.getNotification();
        String notificationTitle = mNotification.extras.getString(Notification.EXTRA_TITLE);
        for(NotificationEventProvider provider:notificationEventProviders)
            provider.handleNotificationEvent(mNotification.category,
                    sbn.getPackageName(), notificationTitle,
                    sbn.getNotification().tickerText.toString(),ACTION_REMOVED);

    }

    static void registerProvider(NotificationEventProvider provider){
        notificationEventProviders.add(provider);
    }

    static void unregisterProvider(NotificationEventProvider provider){
        notificationEventProviders.remove(provider);
    }
}
