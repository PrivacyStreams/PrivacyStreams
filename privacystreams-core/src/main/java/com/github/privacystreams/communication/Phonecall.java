package com.github.privacystreams.communication;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 07/12/2016.
 * A Phonecall item represents a phonecall record
 */

public class Phonecall extends Item {
    public static final String TIMESTAMP = "timestamp";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String DURATION = "duration";
    public static final String TYPE = "type";

    public static class Types {
        public static final String INCOMING = "incoming";
        public static final String OUTGOING = "outgoing";
        public static final String MISSED = "missed";
    };

    Phonecall(Long timestamp, String phone_number, Long duration, String call_type) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(PHONE_NUMBER, phone_number);
        this.setFieldValue(DURATION, duration);
        this.setFieldValue(TYPE, call_type);
    }

    /**
     * Get a provider that provides a stream of Phonecall asLogs
     * @return the stream provider
     */
    public static MultiItemStreamProvider asLogs() {
        return new PhonecallLogProvider();
    }
}
