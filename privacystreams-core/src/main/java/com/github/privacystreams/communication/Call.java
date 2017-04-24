package com.github.privacystreams.communication;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * The information of a phone call.
 */
@PSItem
public class Call extends Item {

//    /**
//     * The unique id of this call log in Android database.
//     */
//    @PSItemField(type = Long.class)
//    public static final String ID = "id";

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

    Call(String id, Long timestamp, String phone_number, Long duration, String call_type) {
//        this.setFieldValue(ID, id);
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(CONTACT, phone_number);
        this.setFieldValue(DURATION, duration);
        this.setFieldValue(TYPE, call_type);
    }

    /**
     * Provide a list of Call items from the device call log.
     * This provider requires `android.permission.READ_CALL_LOG` permission.
     *
     * @return the stream provider
     */
    // @RequiresPermission(value = Manifest.permission.READ_CALL_LOG)
    public static MStreamProvider getLogs() {
        return new CallLogProvider();
    }

    /**
     * Provide a live stream of Call items.
     * A Call item will be generated if there is a new phone call event.
     * This provider requires `android.permission.PROCESS_OUTGOING_CALLS` permission
     * and `android.permission.READ_PHONE_STATE` permission.
     *
     * @return the stream provider
     */
    // @RequiresPermission(allOf = {Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_PHONE_STATE})
    public static MStreamProvider asUpdates() {
        return new CallUpdatesProvider();
    }
}
