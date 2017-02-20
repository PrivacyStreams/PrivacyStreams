package com.github.privacystreams;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;
import java.util.Map;

import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.core.providers.browser.BrowserHistory;
import com.github.privacystreams.core.providers.browser.BrowserSearch;
import com.github.privacystreams.core.providers.call.CallLog;
import com.github.privacystreams.core.providers.contact.Contact;
import com.github.privacystreams.core.providers.document.Document;
import com.github.privacystreams.core.providers.dummy.Dummy;
import com.github.privacystreams.core.providers.location.GeoLocation;
import com.github.privacystreams.core.providers.message.Message;
import com.github.privacystreams.core.providers.mock.MockItem;
import com.github.privacystreams.core.providers.notification.Notification;
import com.github.privacystreams.core.providers.photo.Photo;
import com.github.privacystreams.core.providers.sms.SMS;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.utilities.arithmetic.Arithmetics;
import com.github.privacystreams.core.utilities.common.ItemCommons;
import com.github.privacystreams.core.utilities.comparison.Comparisons;
import com.github.privacystreams.core.utilities.list.Lists;
import com.github.privacystreams.core.utilities.location.Locations;
import com.github.privacystreams.core.utilities.photo.Photos;
import com.github.privacystreams.core.utilities.statistic.Statistics;
import com.github.privacystreams.core.utilities.string.Strings;
import com.github.privacystreams.core.utilities.time.Times;
import com.github.privacystreams.core.utils.time.Duration;
import com.github.privacystreams.core.utils.time.TimeUtils;

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


    // For testing
    public void testMockData() {
        uqi
                .getDataItems(MockItem.asRandomUpdates(20, 100, 500), Purpose.test("test"))
                .limit(100)
                .timeout(Duration.seconds(100))
                .map(ItemCommons.setField("time_round", Arithmetics.roundUp(MockItem.TIME_CREATED, Duration.seconds(5))))
                .localGroupBy("time_round")
                .debug();
//                .forEach(Outputs.uploadToDropbox("<dropbox token here>", "dummy"));
    }

    public void testBrowerSearchUpdates(){
        uqi
                .getDataItems(BrowserSearch.asUpdates(),Purpose.ads("browser search"))
                .debug();
    }
    public void testBrowerHistoryUpdates(){
        uqi
                .getDataItems(BrowserHistory.asUpdates(),Purpose.ads("browser history"))
                .debug();
    }
    public void testMessageUpdates(){
        uqi
                .getDataItems(Message.asUpdates(), Purpose.ads("message"))
                .debug();
    }
    public void testDocumentUpdates(){
        uqi
                .getDataItems(Document.asUpdates(),Purpose.ads("document update"))
                .debug();
    }
    public void testImageUpdates(){
        uqi
                .getDataItems(Photo.readFromStorage(),Purpose.ads("photo update"))
                .debug();
    }

    public void testMotionSensor(){


    }

    public void testBrowserSearchUpdates(){
        uqi.
                getDataItems(BrowserSearch.asUpdates(),Purpose.feature("browser_search"))
                .debug();
    }


    public void testBrowserHistoryUpdates(){
        uqi.
                getDataItems(BrowserHistory.asUpdates(),Purpose.feature("browser_history"))
                .debug();
    }

    public void testNotifications() {
        uqi
                .getDataItems(Notification.asUpdates(), Purpose.feature("test"))
                .print();
    }

    // get a count of the #contacts in contact list
    void getContactCount() {
        uqi
                .getDataItems(Contact.asList(), Purpose.feature("estimate how popular you are."))
                .print();
    }

    // get recent called 10 contacts’ names
    List<String> getRecentCalledNames(int n) {
        List<String> recentCalledPhoneNumbers = uqi
                .getDataItems(CallLog.asList(), Purpose.feature("getDataItems recent called phone numbers"))
                .sortBy(CallLog.TIMESTAMP)
                .limit(n)
                .asList(CallLog.PHONE_NUMBER);
        List<String> recentCalledNames = uqi
                .getDataItems(Contact.asList(), Purpose.feature("getDataItems names of recent called phone numbers"))
                .filter(Lists.intersects(Contact.PHONES, recentCalledPhoneNumbers))
                .asList(Contact.NAME);
        return recentCalledNames;
    }

    // get a count of calls since 31Oct2015
    int getCallCountSince() {
        return uqi
                .getDataItems(CallLog.asList(), Purpose.feature("know how many calls you made"))
                .filter(Times.since(CallLog.TIMESTAMP, TimeUtils.format("yyyy-MM-dd", "2015-10-31")))
                .count();
    }


    // get whether at home
    boolean isAtHome() {
        return uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.feature("know whether you are at home."))
                .check(Locations.atHome(GeoLocation.COORDINATES));
    }

    void callbackWhenReceivesMessage(String appName, Callback<String> messageCallback){
        uqi
                .getDataItems(Message.asIncomingMessages(), Purpose.feature(""));
    }

    // get the intent when enter an area, the callback will be invoked when the use enters or exits an area
    void callbackWhenEntersArea(double x, double y, double r, Callback<Boolean> enterAreaCallback) {
        uqi
                .getDataItems(GeoLocation.asUpdates(LocationManager.GPS_PROVIDER, 10, 10), Purpose.feature("know when you enter an area"))
                .setField("inArea", Locations.inArea(GeoLocation.COORDINATES, x,y,r))
                .onChange("inArea", enterAreaCallback);
    }

    // handle two-factor auth SMS message
    void getTwoFactorAuthSMS(String serverPhoneNum, Callback<String> messageCallback) {
        uqi
                .getDataItems(SMS.asIncomingMessages(), Purpose.feature("Two-factor authentication"))
                .filter(Comparisons.eq(SMS.PHONE_NUMBER, serverPhoneNum))
                .filter(Comparisons.eq(SMS.TYPE, SMS.Type.RECEIVED))
                .ifPresent(SMS.TEXT, messageCallback);
    }

    // get location and blur 100 meters for advertisement
    void passLocationToAd() {
        List<Double> coordinates = uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.ads("targeted advertisement"))
                .compute(Locations.blur(GeoLocation.COORDINATES, 100));
    }

    // get postcode of asLastKnown location
    String getPostcode() {
        return uqi
                .getDataItem(GeoLocation.asLastKnown(), Purpose.feature("get postcode for nearby search"))
                .compute(Locations.postcode(GeoLocation.COORDINATES));
    }

    // knowing if a person is making more or less calls than normal
    boolean isMakingMoreCallsThanNormal() {
        int callCountLastWeek = uqi
                .getDataItems(CallLog.asList(), Purpose.feature("get how many calls you made recently"))
                .filter(Times.recent(CallLog.TIMESTAMP, Duration.days(7)))
                .count();
        double callFrequencyLastWeek = (double) callCountLastWeek / 7;
        int callCountLastYear = uqi
                .getDataItems(CallLog.asList(), Purpose.feature("get how many calls you made normally"))
                .filter(Times.recent(CallLog.TIMESTAMP, Duration.days(365)))
                .count();
        double callFrequencyLastYear = (double) callCountLastYear / 365;
        return callFrequencyLastWeek > callFrequencyLastYear;
    }

    // getting all the photo metadata (but not photos)
    List<Map<String, String>> getAllPhotoMetadata() {
        return uqi
                .getDataItems(Photo.readFromStorage(), Purpose.feature("get metadata of the photos in storage"))
                .setField("metadata", Photos.getMetadata(Photo.URI))
                .asList("metadata");
    }

    // getting how loud it is over the past m seconds,  every n minutes
    void getAverageLoudnessEveryNSeconds(int m, int n, Callback<Double> loudnessCallback) {
        uqi
                .getDataItems(Audio.recordPeriodically(Duration.seconds(m), Duration.seconds(n)),
                        Purpose.feature("how loud it is periodically"))
                .setField("loudness", Audios.getLoudness(Audio.URI))
                .forEach("loudness", loudnessCallback);
    }

    // calculating sentiment across all SMS
    double getAverageSentimentOfSMS() {
        return uqi
                .getDataItems(SMS.asHistory(), Purpose.feature("calculate the sentiment across all SMS messages"))
                .setField("sentiment", Strings.sentiment(SMS.TEXT))
                .outputItems(Statistics.average("sentiment"));
    }

    // figure out place where person spends the most time (ie home)
    String getPlaceSpentMostTime() {
        return uqi
                .getDataItems(GeoLocation.asHistory(), Purpose.feature("get the place you spent the most time"))
                .setField("geo_tag", Locations.geotag(GeoLocation.COORDINATES))
                .localGroupBy("geo_tag")
                .setGroupField("time_spent", Statistics.range(GeoLocation.TIMESTAMP))
                .sortBy("time_spent")
                .reverse()
                .first()
                .getField("geo_tag");
    }


    // hard, calculate total number of calls and length of calls per person in call log
    void getTotalNumberOfCallsPerPerson() {
        // each Map element is like {"phone_number": "xxxxxxx", "num_of_calls": 10, "length_of_calls": 30000}
        List<Item> totalNumberOfCallsPerPerson = uqi
                .getDataItems(CallLog.asList(), Purpose.feature("get the tie relationship with people"))
                .groupBy(CallLog.PHONE_NUMBER)
                .setGroupField("num_of_calls", Statistics.count())
                .setGroupField("length_of_calls", Statistics.sum(CallLog.DURATION))
                .project(CallLog.PHONE_NUMBER, "num_of_calls", "length_of_calls")
                .asList();
    }


    // hash the names or phone#s in SMS or call logs, so we can get data like above while mitigating privacy concerns
    List<String> getHashedPhoneNumbersInSMS() {
        return uqi
                .getDataItems(SMS.asHistory(), Purpose.feature("get hashed phone numbers."))
                .setField("hashed_phone_number", Strings.sha1(SMS.PHONE_NUMBER))
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
