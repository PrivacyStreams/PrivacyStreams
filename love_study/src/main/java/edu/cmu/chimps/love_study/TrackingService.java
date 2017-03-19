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

import com.github.privacystreams.accessibility.UIAction;
import com.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.device.DeviceEvent;
import com.github.privacystreams.utils.time.Duration;

import edu.cmu.chimps.love_study.pam.PAMActivity;

/**
 * Created by fanglinchen on 3/16/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TrackingService extends Service {
    private  static final int NOTIFICATION_ID = 1234;
    private static final int WIFI_BT_SCAN_INTERVAL = 20*60*1000;

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
        UQI uqi = new UQI(this);
//        uqi.getData(Contact.asList(), Purpose.FEATURE("LoveStudy ContactList Collection"))
//                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_access_token), "contact_list"));

//
//        uqi.getData(Message.asIMUpdates(), Purpose.FEATURE("LoveStudy Message Collection"))
//                .debug();
//        uqi.getData(DeviceState.asUpdates(WIFI_BT_SCAN_INTERVAL, DeviceState.Masks.WIFI_AP_LIST
//                | DeviceState.Masks.BLUETOOTH_DEVICE_LIST | DeviceState.Masks.BATTERY_LEVEL),
//                Purpose.FEATURE("Love Study Light Collection"))
//                .project(DeviceState.BATTERY_LEVEL).debug();

//        uqi.getData(Light.asUpdates(),Purpose.FEATURE("Love Study Light Collection"))
//                .filter(Comparators.lt(Light.INTENSITY, 50))
//                .debug();
//                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "light"));
//

//        uqi.getData(DeviceEvent.asUpdates(),Purpose.FEATURE("Love Study Device State Collection"))
//                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(DeviceEvent.TIMESTAMP, Duration.minutes(1))))
//                .localGroupBy("time_round")
//                .debug();
////
        uqi.getData(com.github.privacystreams.notification.Notification.asUpdates(),Purpose.FEATURE("Love Study Device State Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(DeviceEvent.TIMESTAMP, Duration.seconds(30))))
                .localGroupBy("time_round")
                .debug();

//        uqi.getData(Image.readFromStorage(),Purpose.FEATURE("Love Study Image Collection"))
//                .debug();

//        uqi.getData(TextEntry.asUpdates(), Purpose.FEATURE("Love Study Text Entry Collection"))
//                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "text entry"));
        uqi.getData(UIAction.asUpdates(), Purpose.FEATURE("Love Study UIAction Collection")).debug();

//        uqi.getData(BrowserSearch.asUpdates(), Purpose.FEATURE("Love Study Browser Search Collection"))
//                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "Browser Search"));
//        uqi.getData(BrowserVisit.asUpdates(), Purpose.FEATURE("Love Study Browser Visit Collection"))
//                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "Browser Visit"));

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TrackingService","TrackingService");
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
