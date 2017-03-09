package com.github.privacystreams.communication;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

/**
 * A Phonecall item represents a phonecall record
 */
public class Phonecall extends Item {
    @ItemField(name = TIMESTAMP, type = Long.class, description = "The timestamp of when the phonecall is happened.")
    public static final String TIMESTAMP = "timestamp";

    @ItemField(name = CONTACT, type = String.class, description = "The contact (phone number or name) of the phonecall.")
    public static final String CONTACT = "contact";

    @ItemField(name = DURATION, type = Long.class, description = "The duration of the phonecall, in milliseconds.")
    public static final String DURATION = "duration";

    @ItemField(name = TYPE, type = String.class, description = "The phonecall type, could be \"incoming\", \"outgoing\" or \"missed\".")
    public static final String TYPE = "type";

    public static class Types {
        public static final String INCOMING = "incoming";
        public static final String OUTGOING = "outgoing";
        public static final String MISSED = "missed";
    };

    Phonecall(Long timestamp, String phone_number, Long duration, String call_type) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(CONTACT, phone_number);
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
