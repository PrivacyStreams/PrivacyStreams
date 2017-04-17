package com.github.privacystreams;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.accessibility.BrowserSearch;
import com.github.privacystreams.accessibility.BrowserVisit;
import com.github.privacystreams.accessibility.TextEntry;
import com.github.privacystreams.accessibility.UIAction;
import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.audio.AudioOperators;
import com.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.commons.items.ItemsOperators;
import com.github.privacystreams.commons.list.ListOperators;
import com.github.privacystreams.commons.statistic.StatisticOperators;
import com.github.privacystreams.commons.string.StringOperators;
import com.github.privacystreams.commons.time.TimeOperators;
import com.github.privacystreams.communication.Call;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.items.EmptyItem;
import com.github.privacystreams.core.items.TestItem;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.transformations.filter.Filters;
import com.github.privacystreams.core.transformations.group.Groupers;
import com.github.privacystreams.core.transformations.map.Mappers;
import com.github.privacystreams.core.transformations.select.Selectors;
import com.github.privacystreams.device.BluetoothDevice;
import com.github.privacystreams.device.DeviceEvent;
import com.github.privacystreams.device.DeviceOperators;
import com.github.privacystreams.device.WifiAp;
import com.github.privacystreams.environment.LightEnv;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.image.ImageOperators;
import com.github.privacystreams.location.Geolocation;
import com.github.privacystreams.location.GeolocationOperators;
import com.github.privacystreams.location.LatLng;
import com.github.privacystreams.notification.Notification;
import com.github.privacystreams.storage.DropboxOperators;
import com.github.privacystreams.storage.StorageOperators;
import com.github.privacystreams.utils.Duration;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Some examples of using PrivacyStreams for accessing and processing personal data.
 */
public class Examples {
    private UQI uqi;
    private Purpose purpose;

    public Examples(Context context) {
        this.uqi = new UQI(context);
        this.purpose = Purpose.TEST("Examples"); // Developers should replace with actual purpose in real apps.
    }

    /** Get the SSID of the connected Wifi Ap. */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getConnectedWifiSSID() {
        // First get a list of bluetooth items.
        uqi.getData(WifiAp.getScanResults(), purpose)
            .filter("connected", true)
            .ifPresent("ssid", new Callback<String>() {
                @Override
                protected void onInput(String input) {
                    System.out.println("Connected wifi SSID: " + input);
                }
            });
    }

    /** Get the mac addresses of surrounding bluetooth devices. */
    public void getBluetoothMacAddresses() {
        try {
            List<String> macAddresses = uqi.getData(BluetoothDevice.getScanResults(), purpose)
                    .asList("mac_address");
            System.out.println("Bluetooth devices: " + macAddresses);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Get the location metadata of all local images. */
    public void getImageMetadata() {
        uqi.getData(Image.getFromStorage(), purpose)
                .setField("lat_lng", ImageOperators.getLatLng("image_data"))
                .setField("image_path", ImageOperators.getFilepath("image_data"))
                .project("lat_lng", "image_path")
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        if (input == null) return;
                        LatLng latLng = input.getValueByField("lat_lng");
                        String imagePath = input.getValueByField("image_path");
                        double lat = latLng.getLatitude();
                        double lng = latLng.getLongitude();
                        System.out.println("image: " + imagePath + ", lat:" + lat + ", lng:" + lng);
                    }
                });
    }

    /** Take a photo with camera and get the new photo's path. */
    public void takePhoto() {
        try {
            String photo_path = uqi.getData(Image.takeFromCamera(), purpose)
                    .setField("photo_path", ImageOperators.getFilepath("image_data"))
                    .getField("photo_path");
            System.out.println("The new photo's path is " + photo_path);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Record audio in next 10 seconds and get loudness. */
    public void getCurrentLoudness() {
        try {
            Double loudness = uqi.getData(Audio.record(10*1000), purpose)
                    .setField("loudness", AudioOperators.calcLoudness("audio_data"))
                    .getField("loudness");
            System.out.println("Current loudness is " + loudness + " dB.");
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Get fine-grained location updates once per second. */
    public void monitorLocationUpdates() {
        uqi.getData(Geolocation.asUpdates(1000, Geolocation.LEVEL_EXACT), purpose)
                .forEach("lat_lng", new Callback<LatLng>() {
                    @Override
                    protected void onInput(LatLng latLng) {
                        System.out.println("lat=" + latLng.getLatitude() + ", lng=" + latLng.getLongitude());
                    }
                });
    }

    /** Get current city-level location. */
    public void getCityLocation() {
        try {
            LatLng latLng = uqi.getData(Geolocation.asCurrent(Geolocation.LEVEL_CITY), purpose)
                    .getField("lat_lng");
            System.out.println("Current location: lat=" + latLng.getLatitude() + ", lng=" + latLng.getLongitude());
        } catch (PSException e) {
            e.printStackTrace();
        }

    }

    /** Get a stream of notifications and print each notification. */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void printNotification() {
        uqi.getData(Notification.asUpdates(), purpose).debug();
    }

    /** Monitor user's browser search events. */
    public void getBrowserSearchEvents() {
        uqi.getData(BrowserSearch.asUpdates(), purpose)
                .forEach("text", new Callback<String>() {
                    @Override
                    protected void onInput(String input) {
                        System.out.println("The user searched: " + input);
                    }
                });
    }

    /** Get the phone number of the contact with the most calls in recent 1 year. */
    public void getContactWithMostCalls() {
        try {
            String phoneNum = uqi.getData(Call.getLogs(), purpose)
                    .filter(TimeOperators.recent("timestamp", 365*24*60*60*1000L))
                    .groupBy("contact")
                    .setGroupField("#calls", StatisticOperators.count())
                    .select(ItemsOperators.getItemWithMax("#calls"))
                    .getField("contact");
            System.out.println("The phone number with the most calls: " + phoneNum);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Get the names of contacts that the user had phone call with. */
    public void getCalledNames() {
        try {
            List<String> calledPhoneNumbers = uqi.getData(Call.getLogs(), purpose)
                    .asList("contact");
            List<String> calledNames = uqi.getData(Contact.getAll(), purpose)
                    .filter(ListOperators.intersects("phone_numbers", calledPhoneNumbers.toArray()))
                    .asList("name");
            System.out.println("The user had phone call with: " + calledNames);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Calculate total number of calls and length of calls for each contact in call log. */
    public void getNumCallsEachContact() {
        try {
            List<Item> items = uqi.getData(Call.getLogs(), purpose)
                    .groupBy("contact")
                    .setGroupField("num_of_calls", StatisticOperators.count())
                    .setGroupField("length_of_calls", StatisticOperators.sum("duration"))
                    .project("contact", "num_of_calls", "length_of_calls")
                    .asList();
            for (Item item : items) {
                String contact = item.getValueByField("contact");
                Integer numCalls = item.getValueByField("num_of_calls");
                Double lenCalls = item.getValueByField("length_of_calls");
            }
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Get all received SMS messages and hashed phone number. */
    public void getReceivedMessages() {
        try {
            List<Item> items = uqi.getData(Message.getAllSMS(), purpose)
                    .filter("type", Message.TYPE_RECEIVED)
                    .setField("hashed_phone", StringOperators.sha1("contact"))
                    .project("content", "hashed_phone")
                    .asList();
            for (Item item : items) {
                String msgContent = item.getValueByField("content");
                String hashedPhone = item.getValueByField("hashed_phone");
                System.out.println("Received message: " + msgContent);
                System.out.println("The hashed phone number: " + hashedPhone);
            }
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /** Monitor messages in IM apps (WhatsApp, Facebook Messenger, etc.). */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void monitorMessagesIM(){
        uqi.getData(Message.asUpdatesInIM(), purpose)
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        String type = input.getValueByField("type");
                        String content = input.getValueByField("content");
                        String contact = input.getValueByField("contact");
                        if ("sent".equals(type)) {
                            System.out.println("Sent a message to " + contact + ": " + content);
                        }
                        else if ("received".equals(type)) {
                            System.out.println("Received a message from " + contact + ": " + content);
                        }
                    }
                });
    }

    /**
     * Upload the user's device id and current location to Dropbox.
     * This method requires [Dropbox configured](https://privacystreams.github.io/pages/install_dropbox_service.html).
     */
    public void uploadCurrentLocation() {
        Globals.DropboxConfig.accessToken = "Your Dropbox access token here.";
        Globals.DropboxConfig.leastSyncInterval = 60*60*1000L; // Upload to Dropbox once per hour.
        Globals.DropboxConfig.onlyOverWifi = false; // Upload only over WIFI.

        uqi.getData(Geolocation.asUpdates(10*60*1000L, Geolocation.LEVEL_EXACT), purpose)
                .setIndependentField("uuid", DeviceOperators.getDeviceId())
                .project("lat_lng", "uuid")
                .forEach(DropboxOperators.<Item>uploadTo("Location.txt", true));
    }

}
