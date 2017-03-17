package com.github.privacystreams.notification;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

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
                            notificationText);
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
                                        notificationText.split(": ")[1]);
                        }
                        else {
                            for(NotificationEventProvider provider:notificationEventProviders)
                                provider.handleNotificationEvent(mNotification.category,
                                        sbn.getPackageName(),
                                        notificationTitle.split(" @ ")[0],
                                        notificationText);
                        }
                }
            }

        }catch(Exception exception){
            Log.e("exception",exception.toString());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e(TAG,"********** onNOtificationRemoved");
        Log.e(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Intent i = new Intent("edu.cmu.chimps.privacylockscreen.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    static void registerProvider(NotificationEventProvider provider){
        notificationEventProviders.add(provider);
    }

    static void unregisterProvider(NotificationEventProvider provider){
        notificationEventProviders.remove(provider);
    }
}
