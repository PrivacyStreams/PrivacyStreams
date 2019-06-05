package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.calendar.CalendarEvent;
import io.github.privacystreams.communication.Call;
import io.github.privacystreams.communication.Contact;
import io.github.privacystreams.communication.Email;
import io.github.privacystreams.communication.Message;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.items.EmptyItem;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.device.BluetoothDevice;
import io.github.privacystreams.device.DeviceEvent;
import io.github.privacystreams.device.DeviceState;
import io.github.privacystreams.image.Image;
import io.github.privacystreams.location.Geolocation;
import io.github.privacystreams.sensor.Acceleration;
import io.github.privacystreams.sensor.AirPressure;
import io.github.privacystreams.sensor.AmbientTemperature;
import io.github.privacystreams.sensor.Gravity;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.sensor.LinearAcceleration;
import io.github.privacystreams.sensor.RelativeHumidity;
import io.github.privacystreams.sensor.RotationVector;
import io.github.privacystreams.sensor.StepCounter;


class MultiItemOnce extends PStreamProvider{
    private long duration = 1000;
    private List<MultiItem.ItemType> item_types;
    private List<Object> items = new ArrayList<>();
    private List<Purpose> purposes;
    private int limit = -1;
    private final int asUpdatesDelay = 1000;
    private int cameraId = 0;
    private int mask = 0;
    private long afterTime = 0;
    private long beforeTime = 0;
    private String level = Geolocation.LEVEL_EXACT;

    MultiItemOnce(List<MultiItem.ItemType> item_types, List<Purpose> purposes){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
    }

    MultiItemOnce(List<MultiItem.ItemType> item_types, List<Purpose> purposes, long duration, int limit){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
        if(duration > 0) {
            this.duration = duration;
            this.addParameters(duration);
        }
        this.duration = duration;
        this.addParameters(duration);
        if(limit > 0) {
            this.limit = limit;
            this.addParameters(limit);
        }
    }
    MultiItemOnce(List<MultiItem.ItemType> item_types, List<Purpose> purposes, long duration, int limit,
                  int cameraId, int mask, String level, long afterTime, long beforeTime){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
        this.duration = duration;
        this.addParameters(duration);
        this.duration = duration;
        this.addParameters(duration);
        this.limit = limit;
        this.addParameters(limit);
        this.cameraId = cameraId;
        this.addParameters(cameraId);
        this.mask = mask;
        this.addParameters(mask);
        this.level = level;
        this.addParameters(level);
        this.afterTime = afterTime;
        this.addParameters(afterTime);
        this.beforeTime = beforeTime;
        this.addParameters(beforeTime);
    }
/*WHAT TO DO
    PURPOSES
    asUpdates: as updates option where updates whenever any of them update?
    logs: optional limit --> as list, default limit?
    different durations
    drop some data types??
    reverse log option?
 */
    @Override
    protected void provide(){
        for(int i = 0; i < this.item_types.size(); i++) {
            try{
                switch(this.item_types.get(i)) {
                    case ACCELERATION:
                        items.add(getUQI().getData(Acceleration.asUpdates(asUpdatesDelay), purposes.get(i))
                                .getFirst());
                        break;
                    case AIR_PRESSURE:
                        items.add(getUQI().getData(AirPressure.asUpdates(asUpdatesDelay), purposes.get(i))
                                .getFirst());
                        break;
                    case AMBIENT_TEMPERATURE:
                        items.add(getUQI().getData(AmbientTemperature.asUpdates(asUpdatesDelay), purposes.get(i))
                                .getFirst());
                        break;
                    case AUDIO:
                        items.add(getUQI().getData(Audio.record(this.duration), Purpose.TEST("testing"))
                                .getFirst());
                        break;
                    case AUDIO_FROMSTORAGE:
                        if (limit > 0) {
                            items.add(getUQI().getData(Audio.getFromStorage(), Purpose.TEST("testing"))
                                    .reverse()
                                    .limit(limit)
                                    .asList());
                        } else {
                            items.add(getUQI().getData(BluetoothDevice.getScanResults(), purposes.get(i))
                                    .reverse()
                                    .asList());
                        }
                        break;

                    case BLUETOOTH_DEVICE:
                        if (limit > 0) {
                            items.add(getUQI().getData(BluetoothDevice.getScanResults(), purposes.get(i))
                                    .limit(limit).asList());
                        } else {
                            items.add(getUQI().getData(BluetoothDevice.getScanResults(), purposes.get(i))
                                    .asList());
                        }
                        break;
                /*    case BROWSER_SEARCH: //asUpdates
                        items.add(getUQI().getData(BrowserSearch.asUpdates(), purposes.get(i))
                                .getFirst());
                        break;
                    case BROWSER_VISIT: //asUpdates
                        items.add(getUQI().getData(BrowserVisit.asUpdates(), purposes.get(i))
                                .getFirst());
                        break;
                */
                    case CALENDAR:
                        if (limit > 0) {
                            items.add(getUQI().getData(CalendarEvent.getAll(), purposes.get(i))
                                    .limit(limit).asList());
                        } else {
                            items.add(getUQI().getData(CalendarEvent.getAll(), purposes.get(i))
                                    .asList());
                        }
                        break;
                    case CALL:
                        if (limit > 0) {
                            items.add(getUQI().getData(Call.getLogs(), purposes.get(i))
                                    .limit(limit).asList());

                        } else {
                            items.add(getUQI().getData(Call.getLogs(), purposes.get(i))
                                    .asList());
                        }

                        break;
                    case CONTACT:
                        if(limit > 0) {
                            items.add(getUQI().getData(Contact.getAll(), purposes.get(i))
                                    .limit(limit).asList());
                        }
                        else{
                            items.add(getUQI().getData(Contact.getAll(), purposes.get(i))
                                    .asList());
                        }
                        break;
                    case DEVICE_EVENT:
                        items.add(getUQI().getData(DeviceEvent.asUpdates(), purposes.get(i))
                                .getFirst());
                        break;

                    case DEVICE_STATE:
                        items.add(getUQI().getData(DeviceState.asUpdates(asUpdatesDelay, mask), purposes.get(i))
                                .getFirst());
                        break;
                    case EMAIL:
                        if(limit > 0 ) {
                            items.add(getUQI().getData(Email.asGmailHistory(afterTime, beforeTime, limit), purposes.get(i))
                                .asList());
                        }
                        else{
                            items.add(getUQI().getData(Email.asGmailHistory(afterTime, beforeTime, 10), purposes.get(i))
                                .asList());
                        }
                        break;
                    case EMPTY_ITEM:
                        items.add(getUQI().getData(EmptyItem.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case GEOLOCATION_LASTKNOWN:
                       items.add(getUQI().getData(Geolocation.asLastKnown(level), purposes.get(i))
                            .getFirst());
                       break;
                    case GEOLOCATION_CURRENT:
                        items.add(getUQI().getData(Geolocation.asCurrent(level), purposes.get(i))
                                .getFirst());
                        break;
                    case GRAVITY:
                        items.add(getUQI().getData(Gravity.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case GYROSCOPE:
                        items.add(getUQI().getData(Gyroscope.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case IMAGE_TAKE:
                        items.add(getUQI().getData(Image.takePhoto(), purposes.get(i))
                            .getFirst());
                        break;
                    case IMAGE_BG:
                        items.add(getUQI().getData(Image.takePhotoBg(cameraId), purposes.get(i))
                            .getFirst());
                        break;
                    case IMAGE_FROMSTORAGE:
                        if (limit > 0) {
                            items.add(getUQI().getData(Image.readStorage(), purposes.get(i))
                                    .reverse()
                                    .limit(limit)
                                    .asList());
                        } else {
                            items.add(getUQI().getData(Image.readStorage(), purposes.get(i))
                                    .reverse()
                                    .asList());
                        }
                        break;
                    case LIGHT:
                        items.add(getUQI().getData(Light.asUpdates(asUpdatesDelay), Purpose.TEST("testing"))
                            .getFirst());
                        break;
                    case LINEAR_ACCELERATION:
                        items.add(getUQI().getData(LinearAcceleration.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case MESSAGE:
                        if(limit > 0) {
                            items.add(getUQI().getData(Message.getAllSMS(), purposes.get(i))
                                    .limit(limit)
                                    .asList());
                        }
                        else{
                            items.add(getUQI().getData(Message.getAllSMS(), purposes.get(i))
                                    .limit(10)
                                    .asList());
                        }
                        break;

                    //case NOTIFICATION:

                    case RELATIVE_HUMIDITY:
                        items.add(getUQI().getData(RelativeHumidity.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case ROTATION_VECTOR:
                        items.add(getUQI().getData(RotationVector.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                    case STEP_COUNTER:
                        items.add(getUQI().getData(StepCounter.asUpdates(asUpdatesDelay), purposes.get(i))
                            .getFirst());
                        break;
                   // case TEST_ITEM:
                    case WIFI_AP: //needs higher api level
                        if(limit > 0){
                      //      items.add(getUQI().getData(WifiAp.getScanResults(), purposes.get(i))
                      //              .limit(limit)
                      //              .asList());
                        }
                        else{
                        //    items.add(getUQI().getData(WifiAp.getScanResults(), purposes.get(i))
                        //        .asList());
                        }
                        break;
                    default:
                        System.out.println("Nonsupported Type");
                        break;
                }
            } catch (PSException e){
                e.printStackTrace();
                this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiRecording failed"));
            }
        }
        try {
            this.output(new MultiItem(item_types, items));
        } catch(Exception e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiRecording failed."));
        }
        this.finish();
    }
}
