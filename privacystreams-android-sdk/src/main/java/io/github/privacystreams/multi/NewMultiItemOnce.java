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

class NewMultiItemOnce extends PStreamProvider {
    private List<ItemType> item_types;
    private List<Object> items = new ArrayList<>();

    NewMultiItemOnce(List<ItemType> item_types){
        this.item_types = item_types;
    }

    @Override
    protected void provide(){
        for(int i = 0; i < this.item_types.size(); i++) {
            try{
                ItemType item = item_types.get(i);
                switch(item.getType()) {
                    case ACCELERATION:
                        items.add(getUQI().getData(Acceleration.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case AIR_PRESSURE:
                        items.add(getUQI().getData(AirPressure.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case AMBIENT_TEMPERATURE:
                        items.add(getUQI().getData(AmbientTemperature.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case AUDIO:
                        items.add(getUQI().getData(Audio.recordPeriodic(item.getDurationPerRecord(), item.getInterval()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case AUDIO_FROMSTORAGE:
                        items.add(getUQI().getData(Audio.getFromStorage(), item.getPurpose())
                                .reverse()
                                .limit(item.getLimit())
                                .asList());
                        break;

                    case BLUETOOTH_DEVICE:
                        items.add(getUQI().getData(BluetoothDevice.getScanResults(),item.getPurpose())
                                .limit(item.getLimit())
                                .asList());

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
                        items.add(getUQI().getData(CalendarEvent.getAll(), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case CALL:
                        items.add(getUQI().getData(Call.getLogs(), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case CONTACT:
                        items.add(getUQI().getData(Contact.getAll(), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case DEVICE_EVENT:
                        items.add(getUQI().getData(DeviceEvent.asUpdates(), item.getPurpose())
                                .getFirst());
                        break;

                    case DEVICE_STATE:
                        items.add(getUQI().getData(DeviceState.asUpdates(item.getSensorDelay(), item.getMask()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case EMAIL:
                        items.add(getUQI().getData(Email.asGmailHistory(item.getAfterTime(), item.getBeforeTime(), item.getLimit()), item.getPurpose())
                                .asList());
                        break;
                    case EMPTY_ITEM:
                        items.add(getUQI().getData(EmptyItem.asUpdates(item.getInterval()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case GEOLOCATION_LASTKNOWN:
                        items.add(getUQI().getData(Geolocation.asLastKnown(item.getLevel()), item.getPurpose())
                                .getFirst());
                        break;
                    case GEOLOCATION_CURRENT:
                        items.add(getUQI().getData(Geolocation.asCurrent(item.getLevel()), item.getPurpose())
                                .getFirst());
                        break;
                    case GRAVITY:
                        items.add(getUQI().getData(Gravity.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case GYROSCOPE:
                        items.add(getUQI().getData(Gyroscope.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case IMAGE_TAKE:
                        items.add(getUQI().getData(Image.takePhoto(), item.getPurpose())
                                .getFirst());
                        break;
                    case IMAGE_BG:
                        items.add(getUQI().getData(Image.takePhotoBg(item.getCameraId()), item.getPurpose())
                                .getFirst());
                        break;
                    case IMAGE_FROMSTORAGE:
                        items.add(getUQI().getData(Image.readStorage(), item.getPurpose())
                                .reverse()
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case LIGHT:
                        items.add(getUQI().getData(Light.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case LINEAR_ACCELERATION:
                        items.add(getUQI().getData(LinearAcceleration.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case MESSAGE:
                        items.add(getUQI().getData(Message.getAllSMS(), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;

                    //case NOTIFICATION:

                    case RELATIVE_HUMIDITY:
                        items.add(getUQI().getData(RelativeHumidity.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case ROTATION_VECTOR:
                        items.add(getUQI().getData(RotationVector.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    case STEP_COUNTER:
                        items.add(getUQI().getData(StepCounter.asUpdates(item.getSensorDelay()), item.getPurpose())
                                .limit(item.getLimit())
                                .asList());
                        break;
                    /* case TEST_ITEM:
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
                        */
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
            this.output(new NewMultiItem(item_types, items));
        } catch(Exception e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiRecording failed."));
        }
        this.finish();
    }
}
