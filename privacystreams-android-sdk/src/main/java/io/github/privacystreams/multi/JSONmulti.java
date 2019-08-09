package io.github.privacystreams.multi;

import com.google.gson.Gson;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.accessibility.BrowserVisit;
import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioOperators;
import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.commons.items.ItemsOperators;
import io.github.privacystreams.commons.list.ListOperators;
import io.github.privacystreams.commons.logic.LogicOperators;
import io.github.privacystreams.commons.statistic.StatisticOperators;
import io.github.privacystreams.commons.string.StringOperators;
import io.github.privacystreams.commons.time.TimeOperators;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.device.BluetoothDevice;
import io.github.privacystreams.device.DeviceEvent;
import io.github.privacystreams.device.DeviceOperators;
import io.github.privacystreams.image.ImageOperators;
import io.github.privacystreams.io.IOOperators;
import io.github.privacystreams.location.GeolocationOperators;
import io.github.privacystreams.sensor.AirPressure;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.sensor.RelativeHumidity;
import io.github.privacystreams.sensor.RotationVector;
import io.github.privacystreams.sensor.StepCounter;
import io.github.privacystreams.utils.Assertions;

public class JSONmulti {
    public JSONmulti(){};

    public JSONmulti(long interval){
        this.interval = interval;
    }

    public String getJSON(){
        return new Gson().toJson(this);
    }

    public abstract class mItem {
        private JSONmultiFeature[] features;
        public mItem(JSONmultiFeature[] features){
            this.features = features;
        }
        public Feature[] getActualFeatures(){
            Feature[] fs = new Feature[features.length];
            for(int i = 0; i < features.length; i++){
                fs[i] = features[i].getFeature();
            }
            return fs;
        }
        public abstract FeatureProvider getFeatureProvider();
    }

    public class AccelerationItem extends mItem{
        private int sensorDelay;
        public AccelerationItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Acceleration.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class AirPressureItem extends mItem{
        private int sensorDelay;
        public AirPressureItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.AirPressure.asUpdates(sensorDelay), getActualFeatures());
        }
    }


    public class AmbientTemperatureItem extends mItem{
        private int sensorDelay;
        public AmbientTemperatureItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.AmbientTemperature.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class AudioItem extends mItem{
        private long duration;
        public AudioItem(long duration, JSONmultiFeature[] features){
            super(features);
            this.duration = duration;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.audio.Audio.record(duration), getActualFeatures());
        }
    }

    public class BluetoothDeviceItem extends mItem{
        public BluetoothDeviceItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.BluetoothDevice.getScanResults(), getActualFeatures());
        }
    }

    public class BrowserSearchItem extends mItem{
        public BrowserSearchItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.accessibility.BrowserSearch.asUpdates(), getActualFeatures());
        }
    }

    public class BrowserVisitItem extends mItem{
        public BrowserVisitItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.accessibility.BrowserVisit.asUpdates(), getActualFeatures());
        }
    }

    public class CalendarEventItem extends mItem{
        public CalendarEventItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.calendar.CalendarEvent.getAll(), getActualFeatures());
        }
    }

    public class CallLogItem extends mItem{
        public CallLogItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Call.getLogs(), getActualFeatures());
        }
    }

    public class CallUpdatesItem extends mItem{
        public CallUpdatesItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Call.asUpdates(), getActualFeatures());
        }
    }

    public class ContactItem extends mItem{
        public ContactItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Contact.getAll(), getActualFeatures());
        }
    }

    public class DeviceEventItem extends mItem{
        public DeviceEventItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.DeviceEvent.asUpdates(), getActualFeatures());
        }
    }

    public class DeviceStateItem extends mItem{
        private long interval;
        private int mask;
        public DeviceStateItem(long interval, int mask, JSONmultiFeature[] features){
            super(features);
            this.interval = interval;
            this.mask = mask;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.DeviceState.asUpdates(interval, mask), getActualFeatures());
        }
    }

    public class EmailHistoryItem extends mItem{
        private long afterTime;
        private long beforeTime;
        private int maxNumberOfResults;
        public EmailHistoryItem(long afterTime, long beforeTime, int maxNumberOfResults, JSONmultiFeature[] features){
            super(features);
            this.afterTime = afterTime;
            this.beforeTime = beforeTime;
            this.maxNumberOfResults = maxNumberOfResults;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Email.asGmailHistory(afterTime, beforeTime, maxNumberOfResults), getActualFeatures());
        }
    }

    public class EmailUpdatesItem extends mItem{
        private long frequency;
        public EmailUpdatesItem(long frequency, JSONmultiFeature[] features){
            super(features);
            this.frequency = frequency;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Email.asGmailUpdates(frequency), getActualFeatures());
        }
    }

    public class EmptyItem extends mItem{
        private long interval;
        public EmptyItem(long interval, JSONmultiFeature[] features){
            super(features);
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.EmptyItem.asUpdates(interval), getActualFeatures());
        }
    }

    public class GeolocationUpdatesItem extends mItem{
        private long interval;
        private String level;
        public GeolocationUpdatesItem(long interval, String level, JSONmultiFeature[] features){
            super(features);
            this.interval = interval;
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asUpdates(interval, level), getActualFeatures());
        }
    }

    public class GeolocationCurrentItem extends mItem{
        private String level;
        public GeolocationCurrentItem(String level, JSONmultiFeature[] features){
            super(features);
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asCurrent(level), getActualFeatures());
        }
    }

    public class GeolocationLastKnownItem extends mItem{
        private String level;
        public GeolocationLastKnownItem(String level, JSONmultiFeature[] features){
            super(features);
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asLastKnown(level), getActualFeatures());
        }
    }

    public class GravityItem extends mItem{
        private int sensorDelay;
        public GravityItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Gravity.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class GyroscopeItem extends mItem{
        private int sensorDelay;
        public GyroscopeItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Gyroscope.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class ImageCameraItem extends mItem{
        public ImageCameraItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhoto(), getActualFeatures());
        }
    }

    public class ImageStorageItem extends mItem{
        public ImageStorageItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.readStorage(), getActualFeatures());
        }
    }

    public class ImageBgItem extends mItem{
        private int cameraId;
        public ImageBgItem(int cameraId, JSONmultiFeature[] features){
            super(features);
            this.cameraId = cameraId;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhotoBg(cameraId), getActualFeatures());
        }
    }

    public class ImageBgPeriodicItem extends mItem{
        private int cameraId;
        private long interval;
        public ImageBgPeriodicItem(int cameraId, long interval, JSONmultiFeature[] features){
            super(features);
            this.cameraId = cameraId;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhotoBgPeriodic(cameraId, interval), getActualFeatures());
        }
    }

    public class LightItem extends mItem{
        private int sensorDelay;
        public LightItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Light.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class LinearAccelerationItem extends mItem{
        private int sensorDelay;
        public LinearAccelerationItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.LinearAcceleration.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class MessageUpdatesIMItem extends mItem{
        public MessageUpdatesIMItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.asUpdatesInIM(), getActualFeatures());
        }
    }

    public class MessageIncomingSMSItem extends mItem{
        public MessageIncomingSMSItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.asIncomingSMS(), getActualFeatures());
        }
    }

    public class MessageAllSMSItem extends mItem{
        public MessageAllSMSItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.getAllSMS(), getActualFeatures());
        }
    }

    public class NotificationItem extends mItem{
        public NotificationItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.notification.Notification.asUpdates(), getActualFeatures());
        }
    }

    public class RelativeHumidityItem extends mItem{
        private int sensorDelay;
        public RelativeHumidityItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.RelativeHumidity.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class RotationVectorItem extends mItem{
        private int sensorDelay;
        public RotationVectorItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.RotationVector.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class StepCounterItem extends mItem{
        private int sensorDelay;
        public StepCounterItem(int sensorDelay, JSONmultiFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.StepCounter.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class TestUpdatesFromItem extends mItem{
        private List testObjects;
        private long interval;
        public TestUpdatesFromItem(List testObjects, long interval, JSONmultiFeature[] features){
            super(features);
            this.testObjects = testObjects;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.asUpdatesFrom(testObjects, interval), getActualFeatures());
        }
    }

    public class TestUpdatesItem extends mItem{
        private int maxInt;
        private double maxDouble;
        private long interval;
        public TestUpdatesItem(int maxInt, double maxDouble, long interval, JSONmultiFeature[] features){
            super(features);
            this.maxInt = maxInt;
            this.maxDouble = maxDouble;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.asUpdates(maxInt, maxDouble, interval), getActualFeatures());
        }
    }

    public class TestAllFromItem extends mItem{
        private List testObjects;
        public TestAllFromItem(List testObjects, JSONmultiFeature[] features){
            super(features);
            this.testObjects = testObjects;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.getAllFrom(testObjects), getActualFeatures());
        }
    }

    public class TestGetAllRandomItem extends mItem{
        private int maxInt;
        private double maxDouble;
        private int count;
        public TestGetAllRandomItem(int maxInt, double maxDouble, int count, JSONmultiFeature[] features){
            super(features);
            this.maxInt = maxInt;
            this.maxDouble = maxDouble;
            this.count = count;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.getAllRandom(maxInt, maxDouble, count), getActualFeatures());
        }
    }

    public class WifiItem extends mItem{
        public WifiItem(JSONmultiFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.WifiAp.getScanResults(), getActualFeatures());
        }
    }

    private long interval;
    public JSONmulti setPeriodicInterval(long interval){
        this.interval = interval;
        return this;
    }

    private AccelerationItem Acceleration;
    public JSONmulti ACCELERATION(int sensorDelay, JSONmultiFeature[] features){
        this.Acceleration = new AccelerationItem(sensorDelay, features);
        return this;
    }

    private AirPressureItem AirPressure;
    public JSONmulti AIRPRESSURE(int sensorDelay, JSONmultiFeature[] features){
        this.AirPressure = new AirPressureItem(sensorDelay, features);
        return this;
    }

    private AmbientTemperatureItem AmbientTemperature;
    public JSONmulti AMBIENTTEMPERATURE(int sensorDelay, JSONmultiFeature[] features){
        this.AmbientTemperature = new AmbientTemperatureItem(sensorDelay, features);
        return this;
    }

    private AudioItem Audio;
    public JSONmulti AUDIO(long duration, JSONmultiFeature[] features){
        this.Audio = new AudioItem(duration, features);
        return this;
    }

    private BluetoothDeviceItem BluetoothDevice;
    public JSONmulti BLUETOOTHDEVICE(JSONmultiFeature[] features){
        this.BluetoothDevice = new BluetoothDeviceItem(features);
        return this;
    }

    private BrowserSearchItem BrowserSearch;
    public JSONmulti BROWSERSEARCH(JSONmultiFeature[] features){
        this.BrowserSearch = new BrowserSearchItem(features);
        return this;
    }

    private BrowserVisitItem BrowserVisit;
    public JSONmulti BROWSERVISIT(JSONmultiFeature[] features){
        this.BrowserVisit = new BrowserVisitItem(features);
        return this;
    }

    private CalendarEventItem CalendarEvent;
    public JSONmulti CALENDAREVENT(JSONmultiFeature[] features){
        this.CalendarEvent = new CalendarEventItem(features);
        return this;
    }

    private CallUpdatesItem CallUpdates;
    public JSONmulti CALLUPDATES(JSONmultiFeature[] features){
        this.CallUpdates = new CallUpdatesItem(features);
        return this;
    }

    private CallLogItem CallLog;
    public JSONmulti CALLLOG(JSONmultiFeature[] features){
        this.CallLog = new CallLogItem(features);
        return this;
    }

    private ContactItem Contact;
    public JSONmulti CONTACT(JSONmultiFeature[] features){
        this.Contact = new ContactItem(features);
        return this;
    }

    private DeviceEventItem DeviceEvent;
    public JSONmulti DEVICEEVENT(JSONmultiFeature[] features){
        this.DeviceEvent = new DeviceEventItem(features);
        return this;
    }

    private DeviceStateItem DeviceState;
    public JSONmulti DEVICESTATE(long interval, int mask, JSONmultiFeature[] features){
        this.DeviceState = new DeviceStateItem(interval, mask, features);
        return this;
    }

    private EmailHistoryItem EmailHistory;
    public JSONmulti EMAILHISTORY(long afterTime, long beforeTime, int maxNumberOfResults, JSONmultiFeature[] features){
        this.EmailHistory = new EmailHistoryItem(afterTime, beforeTime, maxNumberOfResults, features);
        return this;
    }

    private EmailUpdatesItem EmailUpdates;
    public JSONmulti EMAILUPDATES(long frequency, JSONmultiFeature[] features){
        this.EmailUpdates = new EmailUpdatesItem(frequency, features);
        return this;
    }

    private EmptyItem EmptyItem;
    public JSONmulti EMPTY(long interval, JSONmultiFeature[] features){
        this.EmptyItem = new EmptyItem(interval, features);
        return this;
    }

    private GeolocationUpdatesItem GeolocationUpdates;
    public JSONmulti GEOLOCATIONUPDATES(long interval, String level, JSONmultiFeature[] features){
        this.GeolocationUpdates = new GeolocationUpdatesItem(interval, level, features);
        return this;
    }

    private GeolocationLastKnownItem GeolocationLastKnown;
    public JSONmulti GEOLOCATIONLASTKNOWN(String level, JSONmultiFeature[] features){
        this.GeolocationLastKnown = new GeolocationLastKnownItem(level, features);
        return this;
    }

    private GeolocationCurrentItem GeolocationCurrent;
    public JSONmulti GEOLOCATIONCURRENT(String level, JSONmultiFeature[] features){
        this.GeolocationCurrent = new GeolocationCurrentItem(level, features);
        return this;
    }

    private GravityItem Gravity;
    public JSONmulti GRAVITY(int sensorDelay, JSONmultiFeature[] features){
        this.Gravity = new GravityItem(sensorDelay, features);
        return this;
    }

    private GyroscopeItem Gyroscope;
    public JSONmulti GYROSCOPE(int sensorDelay, JSONmultiFeature[] features){
        this.Gyroscope = new GyroscopeItem(sensorDelay, features);
        return this;
    }

    private ImageBgItem ImageBg;
    public JSONmulti IMAGEBG(int cameraId, JSONmultiFeature[] features){
        this.ImageBg = new ImageBgItem(cameraId, features);
        return this;
    }

    private ImageBgPeriodicItem ImageBgPeriodic;
    public JSONmulti IMAGEBGPERIODIC(int cameraId, long interval, JSONmultiFeature[] features){
        this.ImageBgPeriodic = new ImageBgPeriodicItem(cameraId, interval, features);
        return this;
    }

    private ImageCameraItem ImageCamera;
    public JSONmulti IMAGECAMERA(JSONmultiFeature[] features){
        this.ImageCamera = new ImageCameraItem(features);
        return this;
    }

    private ImageStorageItem ImageStorage;
    public JSONmulti IMAGESTORAGE(JSONmultiFeature[] features){
        this.ImageStorage = new ImageStorageItem(features);
        return this;
    }

    private LightItem Light;
    public JSONmulti LIGHT(int sensorDelay, JSONmultiFeature[] features){
        this.Light = new LightItem(sensorDelay, features);
        return this;
    }

    private LinearAccelerationItem LinearAcceleration;
    public JSONmulti LINEARACCELERATION(int sensorDelay, JSONmultiFeature[] features){
        this.LinearAcceleration = new LinearAccelerationItem(sensorDelay, features);
        return this;
    }

    private MessageAllSMSItem MessageAllSMS;
    public JSONmulti MESSAGEALLSMS(JSONmultiFeature[] features){
        this.MessageAllSMS = new MessageAllSMSItem(features);
        return this;
    }

    private MessageIncomingSMSItem MessageIncomingSMS;
    public JSONmulti MESSAGEINCOMINGSMS(JSONmultiFeature[] features){
        this.MessageIncomingSMS = new MessageIncomingSMSItem(features);
        return this;
    }

    private MessageUpdatesIMItem MessageUpdatesIM;
    public JSONmulti MESSAGEUPDATES(JSONmultiFeature[] features){
        this.MessageUpdatesIM = new MessageUpdatesIMItem(features);
        return this;
    }

    private NotificationItem Notification;
    public JSONmulti NOTIFICATION(JSONmultiFeature[] features){
        this.Notification = new NotificationItem(features);
        return this;
    }

    private RelativeHumidityItem RelativeHumidity;
    public JSONmulti RELATIVEHUMIDITY(int sensorDelay, JSONmultiFeature[] features){
        this.RelativeHumidity = new RelativeHumidityItem(sensorDelay, features);
        return this;
    }

    private RotationVectorItem RotationVector;
    public JSONmulti ROTATIONVECTOR(int sensorDelay, JSONmultiFeature[] features){
        this.RotationVector = new RotationVectorItem(sensorDelay, features);
        return this;
    }

    private StepCounterItem StepCounter;
    public JSONmulti STEPCOUNTER(int sensorDelay, JSONmultiFeature[] features){
        this.StepCounter = new StepCounterItem(sensorDelay, features);
        return this;
    }

    private TestAllFromItem TestAllFrom;
    public JSONmulti TESTALLFROM(List testObjects, JSONmultiFeature[] features){
        this.TestAllFrom = new TestAllFromItem(testObjects, features);
        return this;
    }

    private TestGetAllRandomItem TestGetAllRandom;
    public JSONmulti TESTGETALLRANDOM(int maxInt, double maxDouble, int count, JSONmultiFeature[] features){
        this.TestGetAllRandom = new TestGetAllRandomItem(maxInt, maxDouble, count, features);
        return this;
    }

    private TestUpdatesFromItem TestUpdatesFrom;
    public JSONmulti TESTUPDATESFROM(List testObjects, long interval, JSONmultiFeature[] features){
        this.TestUpdatesFrom = new TestUpdatesFromItem(testObjects, interval, features);
        return this;
    }

    private TestUpdatesItem TestUpdates;
    public JSONmulti TESTUPDATES(int maxInt, double maxDouble, long interval, JSONmultiFeature[] features){
        this.TestUpdates = new TestUpdatesItem(maxInt, maxDouble, interval, features);
        return this;
    }

    private WifiItem WifiAp;
    public JSONmulti WIFIAP(JSONmultiFeature[] features){
        this.WifiAp = new WifiItem(features);
        return this;
    }

    public FeatureProvider[] getFeatureProviders(){
        List<FeatureProvider> fps = new ArrayList<>();
        if(Acceleration != null){
            System.out.println("Exist Acceleration");
            fps.add(Acceleration.getFeatureProvider());
        }
        if(AirPressure != null){
            System.out.println("Exist Air Pressure");
            fps.add(AirPressure.getFeatureProvider());
        }
        if(AmbientTemperature != null){
            System.out.println("Exist Ambient Temperature");
            fps.add(AmbientTemperature.getFeatureProvider());
        }
        if(Audio != null){
            System.out.println("Exist Audio");
            fps.add(Audio.getFeatureProvider());
        }
        if(BluetoothDevice != null){
            System.out.println("Exist Bluetooth Device");
            fps.add(BluetoothDevice.getFeatureProvider());
        }
        if(BrowserSearch != null){
            System.out.println("Exist Browser Search");
            fps.add(BrowserSearch.getFeatureProvider());
        }
        if(BrowserVisit != null){
            System.out.println("Exist Browser Visit");
            fps.add(BrowserVisit.getFeatureProvider());
        }
        if(CalendarEvent != null){
            System.out.println("Exist Calendar Event");
            fps.add(CalendarEvent.getFeatureProvider());
        }
        if(CallUpdates != null){
            System.out.println("Exist Call Updates");
            fps.add(CallUpdates.getFeatureProvider());
        }
        if(CallLog != null){
            System.out.println("Exist Call Log");
            fps.add(CallLog.getFeatureProvider());
        }
        if(Contact != null){
            System.out.println("Exist Contact");
            fps.add(Contact.getFeatureProvider());
        }
        if(DeviceEvent != null){
            System.out.println("Exist Device Event");
            fps.add(DeviceEvent.getFeatureProvider());
        }
        if(DeviceState != null){
            System.out.println("Exist Device State");
            fps.add(DeviceState.getFeatureProvider());
        }
        if(EmailHistory != null){
            System.out.println("Exist Email History");
            fps.add(EmailHistory.getFeatureProvider());
        }
        if(EmailUpdates != null){
            System.out.println("Exist Email Updates");
            fps.add(EmailUpdates.getFeatureProvider());
        }
        if(this.EmptyItem!= null){
            System.out.println("Exist Empty Item");
            fps.add(this.EmptyItem.getFeatureProvider());
        }
        if(GeolocationCurrent != null){
            System.out.println("Exist Geolocation Current");
            fps.add(GeolocationCurrent.getFeatureProvider());
        }
        if(GeolocationLastKnown != null){
            System.out.println("Exist Geolocation Last Known");
            fps.add(GeolocationLastKnown.getFeatureProvider());
        }
        if(GeolocationUpdates != null){
            System.out.println("Exist Geolocation Updates");
            fps.add(GeolocationUpdates.getFeatureProvider());
        }
        if(Gravity != null){
            System.out.println("Exist Gravity");
            fps.add(Gravity.getFeatureProvider());
        }
        if(Gyroscope != null){
            System.out.println("Exist Gyroscope");
            fps.add(Gyroscope.getFeatureProvider());
        }
        if(ImageBg != null){
            System.out.println("Exist Image Bg");
            fps.add(ImageBg.getFeatureProvider());
        }
        if(ImageBgPeriodic != null){
            System.out.println("Exist Image Bg Periodic");
            fps.add(ImageBgPeriodic.getFeatureProvider());
        }
        if(ImageCamera != null){
            System.out.println("Exist Image Camera");
            fps.add(ImageCamera.getFeatureProvider());
        }
        if(ImageStorage != null){
            System.out.println("Exist Image Storage");
            fps.add(ImageStorage.getFeatureProvider());
        }
        if(Light != null){
            System.out.println("Exist Light");
            fps.add(Light.getFeatureProvider());
        }
        if(LinearAcceleration != null){
            System.out.println("Exist Linear Acceleration");
            fps.add(LinearAcceleration.getFeatureProvider());
        }
        if(MessageAllSMS != null){
            System.out.println("Exist Message All SMS");
            fps.add(MessageAllSMS.getFeatureProvider());
        }
        if(MessageUpdatesIM != null){
            System.out.println("Exist Message Updates IM");
            fps.add(MessageUpdatesIM.getFeatureProvider());
        }
        if(MessageIncomingSMS != null){
            System.out.println("Exist Message Incoming SMS");
            fps.add(MessageIncomingSMS.getFeatureProvider());
        }
        if(Notification != null){
            System.out.println("Exist Notification");
            fps.add(Notification.getFeatureProvider());
        }
        if(RelativeHumidity != null){
            System.out.println("Exist Relative Humidity");
            fps.add(RelativeHumidity.getFeatureProvider());
        }
        if(RotationVector != null){
            System.out.println("Exist Rotation Vector");
            fps.add(RotationVector.getFeatureProvider());
        }
        if(StepCounter != null){
            System.out.println("Exist Step Counter");
            fps.add(StepCounter.getFeatureProvider());
        }
        if(TestAllFrom != null){
            System.out.println("Exist Test All From");
            fps.add(TestAllFrom.getFeatureProvider());
        }
        if(TestGetAllRandom != null){
            System.out.println("Exist Test Get All Random");
            fps.add(TestGetAllRandom.getFeatureProvider());
        }
        if(TestUpdatesFrom != null){
            System.out.println("Exist Test Updates From");
            fps.add(TestUpdatesFrom.getFeatureProvider());
        }
        if(TestUpdates != null){
            System.out.println("Exist Test Updates");
            fps.add(TestUpdates.getFeatureProvider());
        }
        if(WifiAp != null){
            System.out.println("Exist Wifi Ap");
            fps.add(WifiAp.getFeatureProvider());
        }
        System.out.println("FeatureProviders: " + fps);
        return fps.toArray(new FeatureProvider[fps.size()]);
    }

    public long getInterval(){
        return interval;
    }

}
