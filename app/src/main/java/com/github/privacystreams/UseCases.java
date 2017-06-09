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
import com.github.privacystreams.device.BluetoothDevice;
import com.github.privacystreams.device.DeviceEvent;
import com.github.privacystreams.device.DeviceOperators;
import com.github.privacystreams.device.WifiAPOperators;
import com.github.privacystreams.device.WifiAp;
import com.github.privacystreams.communication.Email;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.image.ImageOperators;
import com.github.privacystreams.location.Geolocation;
import com.github.privacystreams.location.GeolocationOperators;
import com.github.privacystreams.location.LatLon;
import com.github.privacystreams.notification.Notification;
import com.github.privacystreams.storage.DropboxOperators;
import com.github.privacystreams.storage.StorageOperators;
import com.github.privacystreams.utils.Duration;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.TimeUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.github.privacystreams.commons.items.ItemsOperators.getItemWithMax;
import static com.github.privacystreams.commons.statistic.StatisticOperators.count;
import static com.github.privacystreams.commons.time.TimeOperators.recent;

/**
 * Some show cases of PrivacyStreams
 */
public class UseCases {
    private UQI uqi;

    public UseCases(Context context) {
        this.uqi = new UQI(context);
    }

    public void testBlueToothUpdatesProvider() {
        uqi.getData(BluetoothDevice.getScanResults(), Purpose.FEATURE("blueTooth device")).debug();
    }

    public void testImage() {
//        uqi.getData(Image.getFromStorage(), Purpose.TEST("test"))
//                .setField("lat_lon", ImageOperators.getLatLon(Image.IMAGE_DATA))
//                .debug();
        uqi.getData(Image.takeFromCamera(), Purpose.UTILITY("taking picture."))
                .setField("imagePath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .setField("faceCount", ImageOperators.countFaces(Image.IMAGE_DATA))
                .setField("text", ImageOperators.extractText(Image.IMAGE_DATA))
//                .setField("hasCharacter", ImageOperators.hasCharacter(Image.IMAGE_DATA))
                .debug();
//                .ifPresent("imagePath", new Callback<String>() {
//                    @Override
//                    protected void onInput(String imagePath) {
//                        System.out.println(imagePath);
//                    }
//                    @Override
//                    protected void onFail(PSException exception) {
//                        exception.printStackTrace();
//                    }
//                });
    }

    public void testAudio() {
        uqi.getData(Audio.recordPeriodic(1000, 1000), Purpose.HEALTH("monitoring sleep."))
                .setField("loudness", AudioOperators.calcLoudness("audio_data"))
                .forEach("loudness", new Callback<Double>() {
                    @Override
                    protected void onInput(Double input) {
                        System.out.println("Current loudness is " + input + " dB.");
                    }
                });

    }

    public void testReuse() {
        try {
            MStream stream = uqi
                    .getData(TestItem.getAllRandom(20, 100, 100), Purpose.TEST("test"))
                    .limit(100)
                    .reuse(3);
            int count = stream.count();
            System.out.println(String.format(Locale.getDefault(), "%d", count));
            int gt5Count = stream.filter(Comparators.gt(TestItem.X, 5)).count();
            System.out.println(String.format(Locale.getDefault(), "%d %d", count, gt5Count));
            int lte5Count = stream.logAs("3").filter(Comparators.lte(TestItem.X, 5)).logAs("4").count();
            System.out.println(String.format(Locale.getDefault(), "%d %d %d", count, gt5Count, lte5Count));
        } catch (PSException e) {
            e.printStackTrace();
        }

    }

    public void testLocation() {
        Globals.LocationConfig.useGoogleService = true;
        MStream locationStream = uqi.getData(Geolocation.asUpdates(1000, Geolocation.LEVEL_CITY), Purpose.TEST("test"))
                .setField("distorted_lat_lon", GeolocationOperators.distort(Geolocation.LAT_LON, 1000))
                .setField("distortion", GeolocationOperators.distanceBetween(Geolocation.LAT_LON, "distorted_lat_lon"))
                .reuse(2);

        locationStream.debug();
        locationStream.forEach("distorted_lat_lon", new Callback<LatLon>() {
            @Override
            protected void onInput(LatLon input) {
                System.out.println(input);
            }
        });

        try {
            Thread.sleep(100000);
            uqi.stopAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void testCurrentLocation() {
        Globals.LocationConfig.useGoogleService = true;
        uqi.getData(Geolocation.asCurrent(Geolocation.LEVEL_CITY), Purpose.TEST("test")).debug();

    }

    public void testCall() {
        uqi.getData(Call.asUpdates(), Purpose.TEST("test")).debug();
    }


    public void testSMS() {
        uqi.getData(Message.asIncomingSMS(), Purpose.TEST("test")).debug();
    }


    public void testEmailUpdates(){uqi.getData(Email.asGmailUpdates(),Purpose.TEST("test")).debug();}

    public void testEmailList(){
        uqi.getData(Email.asGmailList(System.currentTimeMillis()-Duration.hours(100),
                System.currentTimeMillis()-Duration.hours(50),
                100),Purpose.TEST("test")).debug();
    }
    // For testing
    public void testMockData() {
        Globals.DropboxConfig.accessToken = "access_token_here";
        Globals.DropboxConfig.leastSyncInterval = Duration.seconds(3);
        Globals.DropboxConfig.onlyOverWifi = false;

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .forEach(StorageOperators.<Item>writeTo("PrivacyStreams/testData.txt", true, true));

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .setIndependentField("uuid", DeviceOperators.getDeviceId())
                .forEach(DropboxOperators.uploadTo(new Function<Item, String>() {
                    @Override
                    public String apply(UQI uqi, Item input) {
                        return input.getValueByField("uuid") + "/mockItem/" + input.getValueByField(Item.TIME_CREATED) + ".json";
                    }
                }, true));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void testDeviceState() {
        Purpose purpose = Purpose.TEST("test");
        uqi
                .getData(EmptyItem.asUpdates(5000), purpose)
                .setIndependentField("contact_list", Contact.getAll().compound(Collectors.toItemList()))
                .setIndependentField("wifi_ap_list", uqi.getData(WifiAp.getScanResults(), purpose).getValueGenerator(Collectors.toItemList()))
                .setIndependentField("bluetooth_list", BluetoothDevice.getScanResults().compound(Collectors.toItemList()))
                .setIndependentField("uuid", DeviceOperators.getDeviceId())
                .limit(3)
                .debug();
    }


    /*
     * Getting a stream of text entries and printing
     */
    public void testTextEntry() {
        uqi.getData(TextEntry.asUpdates(), Purpose.TEST("test")).debug();
    }

    /*
     * Getting a stream of notifications and printing
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void testNotification() {
        uqi.getData(Notification.asUpdates(), Purpose.TEST("test")).debug();
    }

    public void testWifiUpdates(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            uqi.getData(WifiAp.getScanResults(), Purpose.FEATURE("wifi updates")).debug();
        }
    }

    public void testBrowserHistoryUpdates(){
        uqi.getData(BrowserVisit.asUpdates(), Purpose.FEATURE("browser history")).debug();
    }

    public void testBrowserSearchUpdates(){
        uqi.getData(BrowserSearch.asUpdates(), Purpose.FEATURE("browser search")).debug();
    }

    public void testUIAction(){
        uqi.getData(UIAction.asUpdates(), Purpose.FEATURE("ui action")).debug();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void testIMUpdates(){
        uqi.getData(Message.asUpdatesInIM(),Purpose.FEATURE("im updates")).debug();
    }

    // get a count of the #contacts in contact list
    void testContacts() {
        try {
            int count = uqi
                    .getData(Contact.getAll(), Purpose.FEATURE("estimate how popular you are."))
                    .count();
            System.out.println(count);

            uqi
                    .getData(Call.getLogs(), Purpose.SOCIAL("finding your closest contact."))
                    .filter(recent("timestamp", Duration.days(365)))
                    .groupBy("contact")
                    .setGroupField("#calls", count())
                    .select(getItemWithMax("#calls"))
                    .ifPresent("contact", new Callback<String>() {
                        @Override
                        protected void onInput(String contact) {
                            System.out.println("Most-called contact: " + contact);
                        }

                        @Override
                        protected void onFail(PSException e) {
                            System.out.println(e.getMessage());
                        }
                    });

        } catch (PSException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // get recent called 10 contacts’ names
    public void getRecentCalledNames(int n) {
        try {
            List<String> recentCalledPhoneNumbers = uqi
                    .getData(Call.getLogs(), Purpose.FEATURE("getData recent called phone numbers"))
                    .logAs("1")
                    .sortBy(Call.TIMESTAMP)
                    .logAs("2")
                    .limit(n)
                    .asList(Call.CONTACT);
            System.out.println(recentCalledPhoneNumbers);
            List<String> recentCalledNames = uqi
                    .getData(Contact.getAll(), Purpose.FEATURE("getData names of recent called phone numbers"))
                    .filter(ListOperators.intersects(Contact.PHONES, recentCalledPhoneNumbers.toArray()))
                    .asList(Contact.NAME);
            System.out.println(recentCalledNames);
        }
        catch (PSException e) {
            e.printStackTrace();
        }
    }

    // get a count of calls since 31Oct2015
    int getCallCountSince() throws PSException {
        return uqi
                .getData(Call.getLogs(), Purpose.FEATURE("know how many calls you made"))
                .filter(TimeOperators.since(Call.TIMESTAMP, TimeUtils.fromFormattedString("yyyy-MM-dd", "2015-10-31")))
                .count();
    }

    void testDeviceStateChangeUpdates(){
        uqi.getData(DeviceEvent.asUpdates(), Purpose.FEATURE("device states")).debug();
    }

    // TODO Problem set: use this function for test case.
    boolean isAtHome() throws PSException {
        return uqi
                .getData(WifiAp.getScanResults(), Purpose.FEATURE("know whether you are at home."))
                .filter(Comparators.eq(WifiAp.CONNECTED, true))
                .filter(WifiAPOperators.atHome(WifiAp.SSID))
                .count()==1;
    }

    void callbackWhenReceivesMessage(String appName, Callback<String> messageCallback){
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE(""));
    }

    // get the intent when enter an area, the callback will be invoked when the use enters or exits an area
//    void callbackWhenEntersArea(double x, double y, double r, Callback<Boolean> enterAreaCallback) {
//        uqi
//                .getData(Geolocation.asUpdates(LocationManager.GPS_PROVIDER, 10, 10), Purpose.FEATURE("know when you enter an area"))
//                .setField("inCircle", GeolocationOperators.inCircle(Geolocation.COORDINATES, x,y,r))
//                .onChange("inCircle", enterAreaCallback);
//    }

    // handle two-factor auth Message message
    void getTwoFactorAuthSMS(String serverPhoneNum, Callback<String> messageCallback) {
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE("Two-factor authentication"))
                .filter(Comparators.eq(Message.CONTACT, serverPhoneNum))
                .filter(Comparators.eq(Message.TYPE, Message.TYPE_RECEIVED))
                .ifPresent(Message.CONTENT, messageCallback);
    }

    // get location and distort 100 meters for advertisement
//    void passLocationToAd() throws PSException {
//        List<Double> coordinates = uqi
//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.ADS("targeted advertisement"))
//                .output(GeolocationOperators.distort(Geolocation.LAT_LON, 100),);
//    }
//
//    // get postcode of asLastKnown location
//    String getPostcode() throws PSException {
//        return uqi

//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.FEATURE("get postcode for nearby search"));

//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.FEATURE("get postcode for nearby search"))

////                .output(GeolocationOperators.(Geolocation.COORDINATES));
//    }

    // knowing if a person is making more or less calls than normal
    boolean isMakingMoreCallsThanNormal() throws PSException {
        int callCountLastWeek = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get how many calls you made recently"))
                .filter(recent(Call.TIMESTAMP, Duration.days(7)))
                .count();
        double callFrequencyLastWeek = (double) callCountLastWeek / 7;
        int callCountLastYear = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get how many calls you made normally"))
                .filter(recent(Call.TIMESTAMP, Duration.days(365)))
                .count();
        double callFrequencyLastYear = (double) callCountLastYear / 365;
        return callFrequencyLastWeek > callFrequencyLastYear;
    }

    // getting all the photo metadata (but not photos)
    List<Map<String, String>> getAllPhotoMetadata() throws PSException {
        return uqi
                .getData(Image.getFromStorage(), Purpose.FEATURE("get metadata of the photos in storage"))
                .setField("metadata", ImageOperators.getExif(Image.IMAGE_DATA))
                .asList("metadata");
    }

    // getting how loud it is over the past m seconds,  every n minutes
    void getAverageLoudnessEveryNSeconds(int m, int n, Callback<Double> loudnessCallback) {
        uqi
                .getData(Audio.recordPeriodic(Duration.seconds(m), Duration.seconds(n)),
                        Purpose.FEATURE("how loud it is periodically"))
                .setField("loudness", AudioOperators.calcLoudness(Audio.AUDIO_DATA))
                .forEach("loudness", loudnessCallback);
    }

    // calculating sentiment across all Message
//    double getAverageSentimentOfSMS() throws PSException {

////        return uqi
////                .getData(Message.getAllSMS(), Purpose.FEATURE("calculate the sentiment across all Message messages"));

//        return uqi
//                .getData(Message.getAllSMS(), Purpose.FEATURE("calculate the sentiment across all Message messages"))

////                .setField("sentiment", StringOperators.sentiment(Message.CONTENT))
////                .outputItems(StatisticOperators.average("sentiment"));
//    }

    // figure out place where person spends the most time (ie home)
//    String getPlaceSpentMostTime() throws PSException {
//        return uqi
//                .getData(Geolocation.asHistory(), Purpose.FEATURE("get the place you spent the most time"))
//                .setField("geo_tag", GeolocationOperators.asGeotag(Geolocation.COORDINATES))
//                .localGroupBy("geo_tag")
//                .setGroupField("time_spent", StatisticOperators.range(Geolocation.TIMESTAMP))
//                .sortBy("time_spent")
//                .reverse()
//                .getFirst()
//                .getField("geo_tag");
//    }


    // hard, calculate total number of calls and length of calls per person in call log
    void getTotalNumberOfCallsPerPerson() throws PSException {
        // each Map element is like {"phone_number": "xxxxxxx", "num_of_calls": 10, "length_of_calls": 30000}
        List<Item> totalNumberOfCallsPerPerson = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get the tie relationship with people"))
                .groupBy(Call.CONTACT)
                .setGroupField("num_of_calls", StatisticOperators.count())
                .setGroupField("length_of_calls", StatisticOperators.sum(Call.DURATION))
                .project(Call.CONTACT, "num_of_calls", "length_of_calls")
                .asList();
    }


    // hash the names or phone#s in Message or call logs, so we can get data like above while mitigating privacy concerns
    List<String> getHashedPhoneNumbersInSMS() throws PSException {
        return uqi
                .getData(Message.getAllSMS(), Purpose.FEATURE("get hashed phone numbers."))
                .setField("hashed_phone_number", StringOperators.sha1(Message.CONTACT))
                .asList("hashed_phone_number");
    }

    // only get location data when accelerometer motion is above a threshold
    // TODO need stream fusion
    void getLocationWhenMoving() {
    }

    // hard, when plugged in get data xyz at rate R, but when not plugged in get data xy at rate R'
    // TODO need stream fusion
    void upgradeGranularityWhenPlugged() {
    }

    // hard, build a sleep monitor that combines streams of data from microphone loudness, location, accelerometer, light, etc
    // TODO need stream fusion
    void sleepMonitor() {
    }

}
