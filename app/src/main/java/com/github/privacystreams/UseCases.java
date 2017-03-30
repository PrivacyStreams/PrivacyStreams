package com.github.privacystreams;

import android.content.Context;
import android.os.Build;

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
import com.github.privacystreams.communication.CallLog;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
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
import com.github.privacystreams.location.GeoLocation;
import com.github.privacystreams.location.LocationOperators;
import com.github.privacystreams.storage.DropboxOperators;
import com.github.privacystreams.utils.GlobalConfig;
import com.github.privacystreams.utils.time.Duration;
import com.github.privacystreams.utils.time.TimeUtils;

import java.util.List;
import java.util.Map;

import static com.github.privacystreams.commons.items.ItemsOperators.getItemWithMax;
import static com.github.privacystreams.commons.time.TimeOperators.recent;
import static com.github.privacystreams.commons.statistic.StatisticOperators.count;

/**
 * Some show cases of PrivacyStreams
 */
public class UseCases {
    private UQI uqi;

    public UseCases(Context context) {
        this.uqi = new UQI(context);
    }
    /*
     For testing the new lightUpdatesProvider
     */
    public void testLightUpdatesProvider(){
        uqi.getData(LightEnv.asUpdates(), Purpose.FEATURE("light")).debug();
    }

    public void testBlueToothUpatesProvider(){
        uqi.getData(BluetoothDevice.getScanResults(), Purpose.FEATURE("blueTooth device")).debug();
    }

    public void testImage() {
        uqi.getData(Image.readFromStorage(), Purpose.TEST("test"))
                .setField("latLng", ImageOperators.getLatLng(Image.IMAGE_DATA))
                .debug();
    }

//    public void testPhysicalMotionUpdatesProvider(){
//        uqi.getData(AwarenessMotion.asUpdates(),Purpose.FEATURE("Physical Activity")).debug();
//    }

    public void testAudio() {
        uqi.getData(Audio.recordPeriodic(4*1000, 5*1000), Purpose.TEST("test")).debug();
    }

    public void testSMS() {
        uqi.getData(Message.asIncomingSMS(), Purpose.TEST("test")).debug();
    }

    // For testing
    public void testMockData() {
        GlobalConfig.DropboxConfig.accessToken = "wvotIxO75CUAAAAAAAAA8DJw6Cedm6A2Pt-jwHSMBW_KhIYaJUEt9CbgtKe5Vl8O";
        GlobalConfig.DropboxConfig.leastSyncInterval = Duration.seconds(3);
        GlobalConfig.DropboxConfig.onlyOverWifi = false;

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
//                .debug();
                .forEach(DropboxOperators.<Item>uploadTo("mockData.txt", true));
//                .forEach(StorageOperators.<Item>appendTo("PrivacyStreams_dir", "mockData"));

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .setIndependentField("uuid", DeviceOperators.deviceIdGetter())
                .forEach(DropboxOperators.uploadTo(new Function<Item, String>() {
                    @Override
                    public String apply(UQI uqi, Item input) {
                        return input.getValueByField("uuid") + "/mockItem/" + input.getValueByField(Item.TIME_CREATED) + ".json";
                    }
                }, true));
    }

    public void testDeviceState() {
        Purpose purpose = Purpose.TEST("test");
        uqi
                .getData(EmptyItem.asUpdates(5000), purpose)
                .setIndependentField("contact_list", Contact.getAll().compound(Collectors.toItemList()))
                .setIndependentField("wifi_ap_list", uqi.getData(WifiAp.getScanResults(), purpose).getValueGenerator(Collectors.toItemList()))
                .setIndependentField("bluetooth_list", BluetoothDevice.getScanResults().compound(Collectors.toItemList()))
                .setIndependentField("uuid", DeviceOperators.deviceIdGetter())
                .limit(3)
                .debug();
//                .forEach(DropboxOperators.uploadTo(new Function<Item, String>() {
//                    @Override
//                    public String apply(UQI uqi, Item input) {
//                        return input.getValueByField("uuid") + "/device_state/" + input.getValueByField(Item.TIME_CREATED) + ".json";
//                    }
//                }, true));
    }


    /*
     * Getting a stream of text entries and printing
     */
    public void testTextEntry() {
        uqi.getData(TextEntry.asUpdates(), Purpose.FEATURE("test")).debug();
    }

    public void testWifiUpdates(int seconds){
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

    public void testAccessibility(){

    }
    public void testIMUpdates(){
        uqi.getData(Message.asUpdatesInIM(),Purpose.FEATURE("im updates")).debug();
    }

//    public void testBrowerSearchUpdates(){
//        uqi
//                .getData(BrowserSearch.asUpdates(),Purpose.ADS("browser search"))
//                .debug();
//    }
//
//    public void testBrowerHistoryUpdates(){
//        uqi
//                .getData(BrowserVisit.asUpdates(),Purpose.ADS("browser history"))
//                .debug();
//    }
//
//    public void testDocumentUpdates(){
//        uqi
//                .getData(Document.asUpdates(),Purpose.ADS("document update"))
//                .debug();
//    }
//
//    public void testBrowserSearchUpdates(){
//        uqi.
//                getData(BrowserSearch.asUpdates(),Purpose.FEATURE("browser_search"))
//                .debug();
//    }
//
//
//    public void testBrowserHistoryUpdates(){
//        uqi.
//                getData(BrowserVisit.asUpdates(),Purpose.FEATURE("browser_history"))
//                .debug();
//    }
//
//    public void testNotifications() {
//        uqi
//                .getData(Notification.asUpdates(), Purpose.FEATURE("test"))
//                .print();
//    }

    // get a count of the #contacts in contact list
    void testContacts() {
        try {
            int count = uqi
                    .getData(Contact.getAll(), Purpose.FEATURE("estimate how popular you are."))
                    .count();
            System.out.println(count);

            String mostCalledContact = uqi
                    .getData(CallLog.getAll(), Purpose.SOCIAL("finding your closest contact."))
                    .transform(Filters.keep(recent(CallLog.TIMESTAMP, Duration.days(365))))
                    .transform(Groupers.groupBy(CallLog.CONTACT))
                    .transform(Mappers.mapEachItem(ItemOperators.setGroupField("#calls", StatisticOperators.count())))
                    .transform(Selectors.select(getItemWithMax("#calls")))
                    .output(ItemOperators.<String>getField(CallLog.CONTACT));

            mostCalledContact = uqi
                    .getData(CallLog.getAll(), Purpose.SOCIAL("finding your closest contact."))
                    .filter(recent(CallLog.TIMESTAMP, Duration.days(365)))
                    .groupBy(CallLog.CONTACT)
                    .setGroupField("#calls", count())
                    .select(getItemWithMax("#calls"))
                    .getField(CallLog.CONTACT);

            uqi
                    .getData(CallLog.getAll(), Purpose.SOCIAL("finding your closest contact."))
                    .filter(recent(CallLog.TIMESTAMP, Duration.days(365)))
                    .groupBy(CallLog.CONTACT)
                    .setGroupField("#calls", count())
                    .select(getItemWithMax("#calls"))
                    .output(ItemOperators.<String>getField(CallLog.CONTACT), new Callback<String>() {
                        @Override
                        protected void onSuccess(String contact) {
                            System.out.println("Most-called contact: " + contact);
                        }

                        @Override
                        protected void onFail(PrivacyStreamsException e) {
                            System.out.println(e.getMessage());
                        }
                    });

        } catch (PrivacyStreamsException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // get recent called 10 contacts’ names
    List<String> getRecentCalledNames(int n) throws PrivacyStreamsException {
        List<String> recentCalledPhoneNumbers = uqi
                .getData(CallLog.getAll(), Purpose.FEATURE("getData recent called phone numbers"))
                .sortBy(CallLog.TIMESTAMP)
                .limit(n)
                .asList(CallLog.CONTACT);
        List<String> recentCalledNames = uqi
                .getData(Contact.getAll(), Purpose.FEATURE("getData names of recent called phone numbers"))
                .filter(ListOperators.intersects(Contact.PHONES, recentCalledPhoneNumbers.toArray()))
                .asList(Contact.NAME);
        return recentCalledNames;
    }

    // get a count of calls since 31Oct2015
    int getCallCountSince() throws PrivacyStreamsException {
        return uqi
                .getData(CallLog.getAll(), Purpose.FEATURE("know how many calls you made"))
                .filter(TimeOperators.since(CallLog.TIMESTAMP, TimeUtils.fromFormattedString("yyyy-MM-dd", "2015-10-31")))
                .count();
    }

    void testDeviceStateChangeUpdates(){
        uqi.getData(DeviceEvent.asUpdates(), Purpose.FEATURE("device states")).debug();
    }

    // get whether at home
    boolean isAtHome() throws PrivacyStreamsException {
        return uqi
                .getData(GeoLocation.asLastKnown(), Purpose.FEATURE("know whether you are at home."))
                .output(LocationOperators.atHome(GeoLocation.COORDINATES));
    }

    void callbackWhenReceivesMessage(String appName, Callback<String> messageCallback){
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE(""));
    }

    // get the intent when enter an area, the callback will be invoked when the use enters or exits an area
//    void callbackWhenEntersArea(double x, double y, double r, Callback<Boolean> enterAreaCallback) {
//        uqi
//                .getData(GeoLocation.asUpdates(LocationManager.GPS_PROVIDER, 10, 10), Purpose.FEATURE("know when you enter an area"))
//                .setField("inArea", LocationOperators.inArea(GeoLocation.COORDINATES, x,y,r))
//                .onChange("inArea", enterAreaCallback);
//    }

    // handle two-factor auth Message message
    void getTwoFactorAuthSMS(String serverPhoneNum, Callback<String> messageCallback) {
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE("Two-factor authentication"))
                .filter(Comparators.eq(Message.CONTACT, serverPhoneNum))
                .filter(Comparators.eq(Message.TYPE, Message.Types.RECEIVED))
                .ifPresent(Message.CONTENT, messageCallback);
    }

    // get location and blur 100 meters for advertisement
    void passLocationToAd() throws PrivacyStreamsException {
        List<Double> coordinates = uqi
                .getData(GeoLocation.asLastKnown(), Purpose.ADS("targeted advertisement"))
                .output(LocationOperators.blur(GeoLocation.COORDINATES, 100));
    }

    // get postcode of asLastKnown location
    String getPostcode() throws PrivacyStreamsException {
        return uqi
                .getData(GeoLocation.asLastKnown(), Purpose.FEATURE("get postcode for nearby search"))
                .output(LocationOperators.asPostcode(GeoLocation.COORDINATES));
    }

    // knowing if a person is making more or less calls than normal
    boolean isMakingMoreCallsThanNormal() throws PrivacyStreamsException {
        int callCountLastWeek = uqi
                .getData(CallLog.getAll(), Purpose.FEATURE("get how many calls you made recently"))
                .filter(recent(CallLog.TIMESTAMP, Duration.days(7)))
                .count();
        double callFrequencyLastWeek = (double) callCountLastWeek / 7;
        int callCountLastYear = uqi
                .getData(CallLog.getAll(), Purpose.FEATURE("get how many calls you made normally"))
                .filter(recent(CallLog.TIMESTAMP, Duration.days(365)))
                .count();
        double callFrequencyLastYear = (double) callCountLastYear / 365;
        return callFrequencyLastWeek > callFrequencyLastYear;
    }

    // getting all the photo metadata (but not photos)
    List<Map<String, String>> getAllPhotoMetadata() throws PrivacyStreamsException {
        return uqi
                .getData(Image.readFromStorage(), Purpose.FEATURE("get metadata of the photos in storage"))
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
//    double getAverageSentimentOfSMS() throws PrivacyStreamsException {
//        return uqi
//                .getData(Message.getAllSMS(), Purpose.FEATURE("calculate the sentiment across all Message messages"))
//                .setField("sentiment", StringOperators.sentiment(Message.CONTENT))
//                .outputItems(StatisticOperators.average("sentiment"));
//    }

    // figure out place where person spends the most time (ie home)
//    String getPlaceSpentMostTime() throws PrivacyStreamsException {
//        return uqi
//                .getData(GeoLocation.asHistory(), Purpose.FEATURE("get the place you spent the most time"))
//                .setField("geo_tag", LocationOperators.asGeotag(GeoLocation.COORDINATES))
//                .localGroupBy("geo_tag")
//                .setGroupField("time_spent", StatisticOperators.range(GeoLocation.TIMESTAMP))
//                .sortBy("time_spent")
//                .reverse()
//                .getFirst()
//                .getField("geo_tag");
//    }


    // hard, calculate total number of calls and length of calls per person in call log
    void getTotalNumberOfCallsPerPerson() throws PrivacyStreamsException {
        // each Map element is like {"phone_number": "xxxxxxx", "num_of_calls": 10, "length_of_calls": 30000}
        List<Item> totalNumberOfCallsPerPerson = uqi
                .getData(CallLog.getAll(), Purpose.FEATURE("get the tie relationship with people"))
                .groupBy(CallLog.CONTACT)
                .setGroupField("num_of_calls", StatisticOperators.count())
                .setGroupField("length_of_calls", StatisticOperators.sum(CallLog.DURATION))
                .project(CallLog.CONTACT, "num_of_calls", "length_of_calls")
                .asList();
    }


    // hash the names or phone#s in Message or call logs, so we can get data like above while mitigating privacy concerns
    List<String> getHashedPhoneNumbersInSMS() throws PrivacyStreamsException {
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