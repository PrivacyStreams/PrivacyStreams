package io.github.privacystreams.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.app.db.PSLocationDBHelper;
import io.github.privacystreams.app.db.PStreamDBHelper;

/**
 * The PrivacyStreams always-on service for collecting historic data.
 */

public class PStreamCollectService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;

    List<PStreamDBHelper> dbHelpers = new ArrayList<>();

    @Override
    public void onCreate() {
        this.dbHelpers.add(new PSLocationDBHelper(this));
        for (PStreamDBHelper dbHelper : this.dbHelpers) {
            dbHelper.startCollecting();
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.collect_notification_title))
                .setContentText(getText(R.string.collect_notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.collect_notification_title))
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        for (PStreamDBHelper dbHelper : this.dbHelpers) {
            dbHelper.stopCollecting();
        }

        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
