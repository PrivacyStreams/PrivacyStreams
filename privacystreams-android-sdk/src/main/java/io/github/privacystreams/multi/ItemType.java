package io.github.privacystreams.multi;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.purposes.Purpose;

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
    private int mask;
    private long afterTime;
    private long beforeTime;
    private String level;
    private int cameraId;
    private Purpose purpose;

    private ItemType(iType type, Purpose purpose){
        this.type = type;
        this.purpose = purpose;
    }

    private ItemType(iType type, String level, Purpose purpose){
        this.type = type;
        this.level = level;
        this.purpose = purpose;
    }
    private ItemType(iType type, int limit, Purpose purpose){
        this.type = type;
        if (type.equals(iType.IMAGE_BG)){
            this.cameraId = limit;
        }
        else {
            this.limit = limit;
        }
        this.purpose = purpose;
    }

    private ItemType(iType type, int sensorDelay, int limit, Purpose purpose){
        this.type = type;
        this.sensorDelay = sensorDelay;
        this.limit = limit;
        this.purpose = purpose;
    }

    private ItemType(iType type, int sensorDelay, int mask, int limit, Purpose purpose){
        this.type = type;
        this.sensorDelay = sensorDelay;
        this.mask = mask;
        this.limit = limit;
        this.purpose = purpose;
    }

    private ItemType(iType type, long interval, int limit, Purpose purpose){
        this.type = type;
        this.interval = interval;
        this.limit = limit;
        this.purpose = purpose;
    }

    private ItemType(iType type, long durationPerRecord, long interval, int limit, Purpose purpose){
        this.type = type;
        if(type.equals(iType.EMAIL)){
            this.afterTime = durationPerRecord;
            this.beforeTime = interval;
        }
        else {
            this.durationPerRecord = durationPerRecord;
            this.interval = interval;
            this.limit = limit;
        }
        this.purpose = purpose;
    }

    public static ItemType ACCELERATION(int sensorDelay, int limit, Purpose purpose){
        return new ItemType(iType.ACCELERATION, sensorDelay, limit, purpose);
    }

    public static ItemType AIR_PRESSURE(int sensorDelay, int limit, Purpose purpose){
        return new ItemType(iType.AIR_PRESSURE, sensorDelay, limit, purpose);
    }

    public static ItemType AMBIENT_TEMPERATURE(int sensorDelay, int limit, Purpose purpose){
        return new ItemType(iType.AMBIENT_TEMPERATURE, sensorDelay, limit, purpose);
    }

    public static ItemType AUDIO(long durationPerRecord, long interval, int limit, Purpose purpose){
        return new ItemType(iType.AUDIO, durationPerRecord, interval, limit, purpose);
    }

    public static ItemType AUDIO_FROMSTORAGE(int limit, Purpose purpose){
        return new ItemType(iType.AUDIO_FROMSTORAGE, limit, purpose);
    }

    public static ItemType BLUETOOTH_DEVICE(int limit, Purpose purpose){
        return new ItemType(iType.BLUETOOTH_DEVICE, limit, purpose);
    }

    public static ItemType CALENDAR(int limit, Purpose purpose){
        return new ItemType(iType.CALENDAR, limit, purpose);
    }

    public static ItemType CALL(int limit, Purpose purpose){
        return new ItemType(iType.CALL, limit, purpose);
    }

    public static ItemType CONTACT(int limit, Purpose purpose){
        return new ItemType(iType.CONTACT, limit, purpose);
    }

    public static ItemType DEVICE_EVENT(Purpose purpose){
        return new ItemType(iType.DEVICE_EVENT, purpose);
    }

    public static ItemType DEVICE_STATE(int sensorDelay, int mask, int limit, Purpose purpose){
        return new ItemType(iType.DEVICE_STATE, sensorDelay, mask, limit, purpose);
    }

    public static ItemType EMAIL(long afterTime, long beforeTime, int maxNumberOfResults, Purpose purpose){
        return new ItemType(iType.EMAIL, afterTime, beforeTime, maxNumberOfResults, purpose);
    }

    public static ItemType EMPTY_ITEM(long interval, int limit, Purpose purpose) {
        return new ItemType(iType.EMPTY_ITEM, interval, limit, purpose);
    }

    public static ItemType GEOLOCATION_CURRENT (String level, Purpose purpose){
        return new ItemType(iType.GEOLOCATION_CURRENT, level, purpose);
    }

    public static ItemType GEOLOCATION_LASTKNOWN (String level, Purpose purpose) {
        return new ItemType(iType.GEOLOCATION_LASTKNOWN, level, purpose);
    }

    public static ItemType GRAVITY (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.GRAVITY, sensorDelay, limit, purpose);
    }

    public static ItemType GYROSCOPE(int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.GYROSCOPE, sensorDelay, limit, purpose);
    }
    public static ItemType IMAGE_BG (int cameraId, Purpose purpose) {
        return new ItemType(iType.IMAGE_BG, cameraId, purpose);
    }

    public static ItemType IMAGE_FROMSTORAGE (int limit, Purpose purpose) {
        return new ItemType(iType.IMAGE_FROMSTORAGE, limit, purpose);
    }

    public static ItemType IMAGE_TAKE (Purpose purpose) {
        return new ItemType(iType.IMAGE_TAKE, purpose);
    }

    public static ItemType LIGHT (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.LIGHT, sensorDelay, limit, purpose);
    }

    public static ItemType LINEAR_ACCELERATION (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.LINEAR_ACCELERATION, sensorDelay, limit, purpose);
    }

    public static ItemType MESSAGE (int limit, Purpose purpose) {
        return new ItemType(iType.MESSAGE, limit, purpose);
    }

    public static ItemType RELATIVE_HUMIDITY (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.RELATIVE_HUMIDITY, sensorDelay, limit, purpose);
    }

    public static ItemType ROTATION_VECTOR (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.ROTATION_VECTOR, sensorDelay, limit, purpose);
    }

    public static ItemType STEP_COUNTER (int sensorDelay, int limit, Purpose purpose) {
        return new ItemType(iType.STEP_COUNTER, sensorDelay, limit, purpose);
    }

    /*public static ItemType WIFI_AP (int limit, Purpose purpose) {
        return new ItemType(iType.WIFI_AP, limit, purpose);
    }*/

    public iType getType(){
        return this.type;
    }

    public int getSensorDelay(){
        return this.sensorDelay;
    }

    public int getLimit(){
        return this.limit;
    }

    public long getDurationPerRecord(){
        return this.durationPerRecord;
    }

    public long getInterval(){
        return this.interval;
    }

    public int getMask(){
        return this.mask;
    }

    public long getAfterTime(){
        return this.afterTime;
    }

    public long getBeforeTime(){
        return this.beforeTime;
    }

    public String getLevel(){
        return this.level;
    }

    public int getCameraId(){
        return this.cameraId;
    }

    public Purpose getPurpose(){
        return this.purpose;
    }
}
