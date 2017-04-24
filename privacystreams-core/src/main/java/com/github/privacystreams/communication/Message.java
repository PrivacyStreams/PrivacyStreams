package com.github.privacystreams.communication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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

    Message(String type, String content, String packageName, String contact, long timestamp){
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
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
    public static MStreamProvider asUpdatesInIM(){
        return new IMUpdatesProvider();
    }

    /**
     * Provide a live stream of new incoming Message items from the Android Short Message Service (SMS).
     * This provider requires `android.permission.RECEIVE_SMS` permission.
     *
     * @return the provider
     */
    // @RequiresPermission(value = Manifest.permission.RECEIVE_SMS)
    public static MStreamProvider asIncomingSMS() {
        return new SMSIncomingMessageProvider();
    }

    /**
     * Provide all Message items from Android Short Message Service SMS.
     * This provider requires `android.permission.READ_SMS` permission.
     *
     * @return the provider
     */
    // @RequiresPermission(value = Manifest.permission.READ_SMS)
    public static MStreamProvider getAllSMS() {
        return new SMSMessageListProvider();
    }
}
