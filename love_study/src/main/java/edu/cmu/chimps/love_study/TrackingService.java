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
import com.github.privacystreams.communication.CallLog;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.location.Geolocation;
import com.github.privacystreams.utils.time.Duration;
import com.google.android.gms.location.LocationRequest;

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

         uqi.getData(Image.readFromStorage(),Purpose.FEATURE("Love Study Image Collection"))
                .debug();

         uqi.getData(CallLog.getAll(),Purpose.FEATURE("Love Study CallLog Collection"))
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
//
        uqi.getData(Message.asUpdatesInIM(), Purpose.FEATURE("LoveStudy Message Collection"))
                .debug();
////        uqi.getData(DeviceState.asUpdates(WIFI_BT_SCAN_INTERVAL, DeviceState.Masks.WIFI_AP_LIST
////                | DeviceState.Masks.BLUETOOTH_DEVICE_LIST | DeviceState.Masks.BATTERY_LEVEL),
////                Purpose.FEATURE("Love Study LightEnv Collection"))
////                .project(DeviceState.BATTERY_LEVEL).debug();
//
//        uqi.getData(Light.asUpdates(),Purpose.FEATURE("Love Study Light Collection"))
//                .filter(Comparators.lt(LightEnv.INTENSITY, 50))
//                .debug();
//
        uqi.getData(Geolocation.asUpdates(Duration.minutes(1), Duration.seconds(30),
                LocationRequest.PRIORITY_HIGH_ACCURACY), Purpose.FEATURE("know when you enter an area"))
                .debug();
////        uqi.getData(DeviceEvent.asUpdates(),Purpose.FEATURE("Love Study Device State Collection"))
////                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(DeviceEvent.TIMESTAMP, Duration.minutes(1))))
////                .localGroupBy("time_round")
////                .debug();
//////
//        uqi.getData(com.github.privacystreams.notification.Notification.asUpdates(),Purpose.FEATURE("Love Study Device State Collection"))
//                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(DeviceEvent.TIMESTAMP, Duration.seconds(30))))
//                .localGroupBy("time_round")
//                .debug();
//

//        uqi.getData(TextEntry.asUpdates(), Purpose.FEATURE("Love Study Text Entry Collection"))
//                .debug();
//        uqi.getData(UIAction.asUpdates(), Purpose.FEATURE("Love Study UIAction Collection"))
//                .setField("serialized_node", new Function<Item, String>() {
//                    @Override
//                    public String apply(UQI uqi, Item input) {
//                        AccessibilityNodeInfo node = input.getValueByField(UIAction.ROOT_VIEW);
//                        SerializedAccessibilityNodeInfo serialized = SerializedAccessibilityNodeInfo.serialize(node);
//                        return uqi.getGson().toJson(serialized);
//                    }
//                })
//            .debug();
//
//        uqi.getData(BrowserSearch.asUpdates(), Purpose.FEATURE("Love Study Browser Search Collection"))
//                .debug();
//        uqi.getData(BrowserVisit.asUpdates(), Purpose.FEATURE("Love Study Browser Visit Collection"))
//                .debug();

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
