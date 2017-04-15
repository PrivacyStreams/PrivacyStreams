package edu.cmu.chimps.love_study;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.github.privacystreams.calendar.CalendarEvent;
import com.github.privacystreams.communication.Call;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.image.Image;

import edu.cmu.chimps.love_study.pam.PAMActivity;

/**
 * Created by fanglinchen on 3/16/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TrackingService extends Service {
    private  static final int NOTIFICATION_ID = 1234;
    private static final int WIFI_BT_SCAN_INTERVAL = 20*60*1000;
    private static final int IMAGE_STORAGE_SCAN_INTERVAL = 1*30*1000;
    UQI uqi;
    private class PollingTask extends RepeatingTask{

        public PollingTask(int frequency) {
            super(frequency);
        }

        @Override
        protected void doWork() {
         uqi.getData(Contact.getAll(), Purpose.FEATURE("LoveStudy ContactList Collection"))
                .debug();

         uqi.getData(CalendarEvent.getAll(), Purpose.FEATURE("LoveStudy Calendar Event Collection"))
                .debug();

         uqi.getData(Image.getFromStorage(),Purpose.FEATURE("Love Study Image Collection"))
                .debug();

         uqi.getData(Call.getLogs(),Purpose.FEATURE("Love Study Call Collection"))
                .debug();

        }
    }


    private void showNotification() {
        Intent notificationIntent = new Intent(this, PAMActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.heart);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Love study is running")
                .setSmallIcon(R.drawable.heart)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);

    }

    public void collectData(){
        Log.e("TrackingService","Collecting Data");

        PollingTask pollingTask = new PollingTask(IMAGE_STORAGE_SCAN_INTERVAL);
        pollingTask.run();
        uqi.getData(Message.asUpdatesInIM(), Purpose.FEATURE("LoveStudy Message Collection"))
                .debug();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TrackingService","TrackingService");
        uqi = new UQI(this);
        if(intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            showNotification();
            collectData();
        }
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
