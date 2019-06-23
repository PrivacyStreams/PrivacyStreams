package io.github.privacystreams.multi;

import io.github.privacystreams.core.Item;

public class ItemType {
    public enum iType  {ACCELERATION, AIR_PRESSURE, AMBIENT_TEMPERATURE, AUDIO_FROMSTORAGE,
        AUDIO, BLUETOOTH_DEVICE, CALENDAR, CALL, CONTACT, DEVICE_EVENT, DEVICE_STATE, EMAIL,
        EMPTY_ITEM, GEOLOCATION_CURRENT, GEOLOCATION_LASTKNOWN, GRAVITY, GYROSCOPE, IMAGE_BG,
        IMAGE_FROMSTORAGE, IMAGE_TAKE, LIGHT, LINEAR_ACCELERATION, MESSAGE, RELATIVE_HUMIDITY,
        ROTATION_VECTOR, STEP_COUNTER, TEST_ITEM, WIFI_AP }

    private iType type;
    private int sensorDelay;
    private int limit;
    private long durationPerRecord;
    private long interval;

    private ItemType(iType type, int limit){
        this.type = type;
        this.limit = limit;
    }

    private ItemType(iType type, int sensorDelay, int limit){
        this.type = type;
        this.sensorDelay = sensorDelay;
        this.limit = limit;
    }

    private ItemType(iType type, long durationPerRecord, long interval, int limit){
        this.type = type;
        this.durationPerRecord = durationPerRecord;
        this.interval = interval;
        this.limit = limit;
    }

    public static ItemType ACCELERATION(int sensorDelay, int limit){
        return new ItemType(iType.ACCELERATION, sensorDelay, limit);
    }

    public static ItemType AIR_PRESSURE(int sensorDelay, int limit){
        return new ItemType(iType.AIR_PRESSURE, sensorDelay, limit);
    }

    public static ItemType AMBIENT_TEMPERATURE(int sensorDelay, int limit){
        return new ItemType(iType.AMBIENT_TEMPERATURE, sensorDelay, limit);
    }

    public static ItemType AUDIO(long durationPerRecord, long interval, int limit){
        return new ItemType(iType.AUDIO, durationPerRecord, interval, limit);
    }

    public static ItemType AUDIO_FROMSTORAGE(int limit){
        return new ItemType(iType.AUDIO_FROMSTORAGE, limit);
    }

    public static ItemType BLUETOOTH_DEVICE(int limit){
        return new ItemType(iType.BLUETOOTH_DEVICE, limit);
    }

    public static ItemType CALENDAR(int limit){
        return new ItemType(iType.CALENDAR, limit);
    }

    public static ItemType CALL(int limit){
        return new ItemType(iType.CALL, limit);
    }

    public static ItemType CONTACT(int limit){
        return new ItemType(iType.CONTACT, limit);
    }

}
