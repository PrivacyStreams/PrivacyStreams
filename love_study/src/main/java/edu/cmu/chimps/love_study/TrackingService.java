package edu.cmu.chimps.love_study;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.github.privacystreams.accessibility.BrowserSearch;
import com.github.privacystreams.accessibility.BrowserVisit;
import com.github.privacystreams.accessibility.TextEntry;
import com.github.privacystreams.accessibility.UIAction;
import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.device.BluetoothDevice;
import com.github.privacystreams.device.DeviceEvent;
import com.github.privacystreams.device.WifiAp;
import com.github.privacystreams.dropbox.DropboxOperators;
import com.github.privacystreams.environment.Light;
import com.github.privacystreams.google_awareness.PhysicalActivity;
import com.github.privacystreams.image.Image;

import edu.cmu.chimps.love_study.pam.PAMActivity;

/**
 * Created by fanglinchen on 3/16/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TrackingService extends Service {
    private  static final int NOTIFICATION_ID = 1234;

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
        uqi.getDataItems(Contact.asList(), Purpose.feature("LoveStudy ContactList Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "contact_list"));

        uqi.getDataItems(Message.asIMUpdates(),Purpose.feature("LoveStudy Message Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "im"));

        uqi.getDataItems(LocationStay.asLocationStayUpdates(LocationManager.GPS_PROVIDER, 10,
                getResources().getString(R.string.google_api_key)),
                Purpose.feature("LoveStudy Location Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "location"));

        uqi.getDataItems(Light.asUpdates(),Purpose.feature("Love Study Light Collection"))
                .filter(Comparators.lt(Light.INTENSITY, 50))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "light"));

        uqi.getDataItems(BluetoothDevice.asUpdates(),Purpose.feature("Love Study BT Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "bluetooth"));

        uqi.getDataItems(WifiAp.asScanList(),Purpose.feature("Love Study Wifi Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "wifi"));

        uqi.getDataItems(DeviceEvent.asUpdates(),Purpose.feature("Love Study Device State Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "device state"));

        uqi.getDataItems(PhysicalActivity.asUpdates(),Purpose.feature("Love Study Physical Activity Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "physical activity"));

        uqi.getDataItems(Image.readFromStorage(),Purpose.feature("Love Study Text Entry Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "device state"));
        uqi.getDataItems(TextEntry.asUpdates(), Purpose.feature("Love Study Text Entry Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "text entry"));
        uqi.getDataItems(UIAction.asUpdates(), Purpose.feature("Love Study UIAction Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "ui action"));
        uqi.getDataItems(BrowserSearch.asUpdates(), Purpose.feature("Love Study Browser Search Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "Browser Search"));
        uqi.getDataItems(BrowserVisit.asUpdates(), Purpose.feature("Love Study Browser Visit Collection"))
                .forEach(DropboxOperators.upload(getResources().getString(R.string.dropbox_api_key), "Browser Visit"));

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
