package com.github.privacystreams.communication;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * The information of a phone call.
 */
@PSItem
public class CallLog extends Item {

    /**
     * The unique id of this call log.
     */
    @PSItemField(type = Long.class)
    public static final String ID = "id";


    /**
     * The timestamp of when the phone call is happened.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The contact (phone number or name) of the phone call.
     */
    @PSItemField(type = String.class)
    public static final String CONTACT = "contact";

    /**
     * The duration of the phone call, in milliseconds.
     */
    @PSItemField(type = Long.class)
    public static final String DURATION = "duration";

    /**
     * The type of the phone call, could be "incoming", "outgoing" or "missed".
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

    public static final String TYPE_INCOMING = "incoming";
    public static final String TYPE_OUTGOING = "outgoing";
    public static final String TYPE_MISSED = "missed";

    CallLog(String id, Long timestamp, String phone_number, Long duration, String call_type) {
        this.setFieldValue(ID, id);
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(CONTACT, phone_number);
        this.setFieldValue(DURATION, duration);
        this.setFieldValue(TYPE, call_type);
    }

    /**
     * Provide a list of CallLog items from the device call log.
     * @return the stream provider
     */
    public static MStreamProvider getAll() {
        return new CallLogProvider();
    }
}
