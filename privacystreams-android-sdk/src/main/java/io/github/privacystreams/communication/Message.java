package io.github.privacystreams.communication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * A text message. It could be from SMS, WhatsApp, Facebook, etc.
 */
@PSItem
public class Message extends Item {

    /**
     * The message content.
     */
    @PSItemField(type = String.class)
    public static final String CONTENT = "content";

    /**
     * The hour and minute of day when server achieved the message
     */
    @PSItemField(type = int.class)
    public static final String LOG_TIME = "log_time";

    public static final int LOG_TIME_NA = 9999;

    /**
     * The package name of the app where message is captured.
     * For example, if it is a Facebook message, package_name will be "com.facebook.orca";
     * If it is an SMS message, package_name will be "system".
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The contact (phone number or name) of the message.
     */
    @PSItemField(type = String.class)
    public static final String CONTACT = "contact";


    /**
     * The timestamp of when the message is sent or received.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The message type, could be "received", "sent", "draft", "pending", or "unknown".
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

    public static final String TYPE_RECEIVED = "received";
    public static final String TYPE_SENT = "sent";
    public static final String TYPE_DRAFT = "draft";
    public static final String TYPE_PENDING = "pending";
    public static final String TYPE_UNKNOWN = "unknown";

    Message(String type, String content, String packageName, String contact, long timestamp) {
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
        this.setFieldValue(LOG_TIME, LOG_TIME_NA);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CONTACT, contact);
        this.setFieldValue(TIMESTAMP, timestamp);
    }

    Message(String type, String content, int logTime, String packageName, String contact, long timestamp) {
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
        this.setFieldValue(LOG_TIME, logTime);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CONTACT, contact);
        this.setFieldValue(TIMESTAMP, timestamp);
    }

    /**
     * Provide a live stream of new Message items in Instant Messenger (IM) apps, including WhatsApp and Facebook.
     * This provider requires Accessibility service turned on.
     *
     * @return the provider function
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static PStreamProvider asUpdatesInIM() {
        return new IMUIUpdatesProvider();
    }

    /**
     * Provide a live stream of new incoming Message items from the Android Short Message Service (SMS).
     * This provider requires `android.permission.RECEIVE_SMS` permission.
     *
     * @return the provider
     */
    // @RequiresPermission(value = Manifest.permission.RECEIVE_SMS)
    public static PStreamProvider asIncomingSMS() {
        return new SMSIncomingMessageProvider();
    }

    /**
     * Provide all Message items from Android Short Message Service SMS.
     * This provider requires `android.permission.READ_SMS` permission.
     *
     * @return the provider
     */
    // @RequiresPermission(value = Manifest.permission.READ_SMS)
    public static PStreamProvider getAllSMS() {
        return new SMSMessageListProvider();
    }
}
