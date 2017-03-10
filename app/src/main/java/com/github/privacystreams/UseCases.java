package com.github.privacystreams;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import com.github.privacystreams.accessibility.BrowserHistory;
import com.github.privacystreams.accessibility.BrowserSearch;
import com.github.privacystreams.accessibility.TextEntry;
import com.github.privacystreams.accessibility.UIAction;
import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.communication.Phonecall;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.commons.arithmetic.Arithmetics;
import com.github.privacystreams.core.commons.common.ItemCommons;
import com.github.privacystreams.core.commons.comparison.Comparisons;
import com.github.privacystreams.core.commons.list.Lists;
import com.github.privacystreams.core.commons.statistic.Statistics;
import com.github.privacystreams.core.commons.string.Strings;
import com.github.privacystreams.core.commons.time.Times;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.providers.mock.MockItem;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.utils.time.Duration;
import com.github.privacystreams.core.utils.time.TimeUtils;
import com.github.privacystreams.device.BTDevice;
import com.github.privacystreams.device.DeviceStateChange;
import com.github.privacystreams.device.WifiAp;
import com.github.privacystreams.environment.Light;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.location.GeoLocation;
import com.github.privacystreams.motion.PhysicalActivity;

import java.util.List;
import java.util.Map;

/**
 * Some show cases of PrivacyStreams
 */
public class UseCases {
    private UQI uqi;
    private Context context;

    public UseCases(Context context) {
        this.context = context;
        this.uqi = new UQI(context);
    }
    /*
     For testing the new lightUpdatesProvider
     */
    public void testLightUpdatesProvider(){
        uqi.getDataItems(Light.asUpdates(), Purpose.feature("light")).debug();
    }

    public void testBlueToothUpatesProvider(){
        uqi.getDataItems(BTDevice.asUpdates(), Purpose.feature("blueTooth device")).debug();
    }

    public void testPhysicalMotionUpdatesProvider(){
        uqi.getDataItems(PhysicalActivity.asUpdates(),Purpose.feature("Physical Activity")).debug();
    }
    // For testing
    public void testMockData() {
        uqi
                .getDataItems(MockItem.asRandomUpdates(20, 100, 500), Purpose.test("test"))
                .limit(100)
                .timeout(Duration.seconds(100))
                .map(ItemCommons.setField("time_round", Arithmetics.roundUp(MockItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .debug();
//                .forEach(Outputs.uploadToDropbox("<dropbox token here>", "dummy"));
    }

    /*
     * Getting a stream of text entries and printing
     */
    public void testTextEntry() {
        uqi.getDataItems(TextEntry.asUpdates(), Purpose.feature("test")).debug();
    }

    public void testWifiUpdates(int seconds){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            uqi.getDataItems(WifiAp.asUpdates(seconds), Purpose.feature("wifi updates")).debug();
        }
    }

    public void testBrowserHistoryUpdates(){
        uqi.getDataItems(BrowserHistory.asUpdates(), Purpose.feature("browser history")).debug();
    }
    public void testBrowserSearchUpdates(){
        uqi.getDataItems(BrowserSearch.asUpdates(), Purpose.feature("browser search")).debug();
    }

    public void testUIAction(){
        uqi.getDataItems(UIAction.asUpdates(), Purpose.feature("ui action")).debug();
    }

    public void testAccessibility(){

    }
    public void testIMUpdates(){
        uqi.getDataItems(Message.asIMUpdates(),Purpose.feature("im updates")).debug();
    }

//    public void testBrowerSearchUpdates(){
//        uqi
//                .getDataItems(BrowserSearch.asUpdates(),Purpose.ads("browser search"))
//                .debug();
//    }
//
//    public void testBrowerHistoryUpdates(){
//        uqi
//                .getDataItems(BrowserHistory.asUpdates(),Purpose.ads("browser history"))
//                .debug();
//    }
//
//    public void testDocumentUpdates(){
//        uqi
//                .getDataItems(Document.asUpdates(),Purpose.ads("document update"))
//                .debug();
//    }
//
//    public void testBrowserSearchUpdates(){
//        uqi.
//                getDataItems(BrowserSearch.asUpdates(),Purpose.feature("browser_search"))
//                .debug();
//    }
//
//
//    public void testBrowserHistoryUpdates(){
//        uqi.
//                getDataItems(BrowserHistory.asUpdates(),Purpose.feature("browser_history"))
//                .debug();
//    }
//
//    public void testNotifications() {
//        uqi
//                .getDataItems(Notification.asUpdates(), Purpose.feature("test"))
//                .print();
//    }

    // get a count of the #contacts in contact list
    void testContacts() {
        try {
            int count = uqi
                    .getDataItems(Contact.asList(), Purpose.feature("estimate how popular you are."))
                    .count();
            System.out.println(count);
        } catch (PrivacyStreamsException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // get recent called 10 contacts’ names
    List<String> getRecentCalledNames(int n) throws PrivacyStreamsException {
        List<String> recentCalledPhoneNumbers = uqi
                .getDataItems(Phonecall.asLogs(), Purpose.feature("getDataItems recent called phone numbers"))
                .sortBy(Phonecall.TIMESTAMP)
                .limit(n)
                .asList(Phonecall.PHONE_NUMBER);
        List<String> recentCalledNames = uqi
                .getDataItems(Contact.asList(), Purpose.feature("getDataItems names of recent called phone numbers"))
                .filter(Lists.intersects(Contact.PHONES, recentCalledPhoneNumbers.toArray()))
                .asList(Contact.NAME);
        return recentCalledNames;
    }

    // get a count of calls since 31Oct2015
    int getCallCountSince() throws PrivacyStreamsException {
        return uqi
                .getDataItems(Phonecall.asLogs(), Purpose.feature("know how many calls you made"))
                .filter(Times.since(Phonecall.TIMESTAMP, TimeUtils.format("yyyy-MM-dd", "2015-10-31")))
                .count();
    }

    void testDeviceStateChangeUpdates(){
        uqi.getDataItems(DeviceStateChange.asUpdates(), Purpose.feature("device states")).debug();
    }

    // get whether at home
    boolean isAtHome() throws PrivacyStreamsException {
        return uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.feature("know whether you are at home."))
                .outputItem(GeoLocation.atHome(GeoLocation.COORDINATES));
    }

    void callbackWhenReceivesMessage(String appName, Callback<String> messageCallback){
        uqi
                .getDataItems(Message.asSMSUpdates(), Purpose.feature(""));
    }

    // get the intent when enter an area, the callback will be invoked when the use enters or exits an area
    void callbackWhenEntersArea(double x, double y, double r, Callback<Boolean> enterAreaCallback) {
        uqi
                .getDataItems(GeoLocation.asUpdates(LocationManager.GPS_PROVIDER, 10, 10), Purpose.feature("know when you enter an area"))
                .setField("inArea", GeoLocation.inArea(GeoLocation.COORDINATES, x,y,r))
                .onChange("inArea", enterAreaCallback);
    }

    // handle two-factor auth Message message
    void getTwoFactorAuthSMS(String serverPhoneNum, Callback<String> messageCallback) {
        uqi
                .getDataItems(Message.asSMSUpdates(), Purpose.feature("Two-factor authentication"))
                .filter(Comparisons.eq(Message.CONTACT, serverPhoneNum))
                .filter(Comparisons.eq(Message.TYPE, Message.Type.RECEIVED))
                .ifPresent(Message.CONTENT, messageCallback);
    }

    // get location and blur 100 meters for advertisement
    void passLocationToAd() throws PrivacyStreamsException {
        List<Double> coordinates = uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.ads("targeted advertisement"))
                .outputItem(GeoLocation.blur(GeoLocation.COORDINATES, 100));
    }

    // get postcode of asLastKnown location
    String getPostcode() throws PrivacyStreamsException {
        return uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.feature("get postcode for nearby search"))
                .outputItem(GeoLocation.asPostcode(GeoLocation.COORDINATES));
    }

    // knowing if a person is making more or less calls than normal
    boolean isMakingMoreCallsThanNormal() throws PrivacyStreamsException {
        int callCountLastWeek = uqi
                .getDataItems(Phonecall.asLogs(), Purpose.feature("get how many calls you made recently"))
                .filter(Times.recent(Phonecall.TIMESTAMP, Duration.days(7)))
                .count();
        double callFrequencyLastWeek = (double) callCountLastWeek / 7;
        int callCountLastYear = uqi
                .getDataItems(Phonecall.asLogs(), Purpose.feature("get how many calls you made normally"))
                .filter(Times.recent(Phonecall.TIMESTAMP, Duration.days(365)))
                .count();
        double callFrequencyLastYear = (double) callCountLastYear / 365;
        return callFrequencyLastWeek > callFrequencyLastYear;
    }

    // getting all the photo metadata (but not photos)
    List<Map<String, String>> getAllPhotoMetadata() throws PrivacyStreamsException {
        return uqi
                .getDataItems(Image.readFromStorage(), Purpose.feature("get metadata of the photos in storage"))
                .setField("metadata", Image.getMetadata(Image.URI))
                .asList("metadata");
    }

    // getting how loud it is over the past m seconds,  every n minutes
    void getAverageLoudnessEveryNSeconds(int m, int n, Callback<Double> loudnessCallback) {
        uqi
                .getDataItems(Audio.recordPeriodically(Duration.seconds(m), Duration.seconds(n)),
                        Purpose.feature("how loud it is periodically"))
                .setField("loudness", Audio.getLoudness(Audio.URI))
                .forEach("loudness", loudnessCallback);
    }

    // calculating sentiment across all Message
    double getAverageSentimentOfSMS() throws PrivacyStreamsException {
        return uqi
                .getDataItems(Message.asSMSHistory(), Purpose.feature("calculate the sentiment across all Message messages"))
                .setField("sentiment", Strings.sentiment(Message.CONTENT))
                .outputItems(Statistics.average("sentiment"));
    }

    // figure out place where person spends the most time (ie home)
    String getPlaceSpentMostTime() throws PrivacyStreamsException {
        return uqi
                .getDataItems(GeoLocation.asHistory(), Purpose.feature("get the place you spent the most time"))
                .setField("geo_tag", GeoLocation.asGeotag(GeoLocation.COORDINATES))
                .localGroupBy("geo_tag")
                .setGroupField("time_spent", Statistics.range(GeoLocation.TIMESTAMP))
                .sortBy("time_spent")
                .reverse()
                .first()
                .getField("geo_tag");
    }


    // hard, calculate total number of calls and length of calls per person in call log
    void getTotalNumberOfCallsPerPerson() throws PrivacyStreamsException {
        // each Map element is like {"phone_number": "xxxxxxx", "num_of_calls": 10, "length_of_calls": 30000}
        List<Item> totalNumberOfCallsPerPerson = uqi
                .getDataItems(Phonecall.asLogs(), Purpose.feature("get the tie relationship with people"))
                .groupBy(Phonecall.PHONE_NUMBER)
                .setGroupField("num_of_calls", Statistics.count())
                .setGroupField("length_of_calls", Statistics.sum(Phonecall.DURATION))
                .project(Phonecall.PHONE_NUMBER, "num_of_calls", "length_of_calls")
                .asList();
    }


    // hash the names or phone#s in Message or call logs, so we can get data like above while mitigating privacy concerns
    List<String> getHashedPhoneNumbersInSMS() throws PrivacyStreamsException {
        return uqi
                .getDataItems(Message.asSMSHistory(), Purpose.feature("get hashed phone numbers."))
                .setField("hashed_phone_number", Strings.sha1(Message.CONTACT))
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