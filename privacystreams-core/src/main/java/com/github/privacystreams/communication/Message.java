package com.github.privacystreams.communication;

import android.os.Build;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A text message. It could be from SMS, WhatsApp, Facebook, etc.
 */
@PSItem
public class Message extends Item {

    /**
     * The message type, could be "received"/"sent"/"draft"/"pending"/"unknown".
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

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
     * The timestamp of when the message is sent/received.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * Possible message types.
     */
    public static class Types {
        public static final String RECEIVED = "received";
        public static final String SENT = "sent";
        public static final String DRAFT = "draft";
        public static final String PENDING = "pending";
        public static final String UNKNOWN = "unknown";
    };

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
     * @return the provider function
     */

     public static MStreamProvider asUpdatesInIM(){
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
             return new IMUpdatesProvider();
         else {
             Logging.warn("Illegal SDK version.");
             return null;
         }
    }

    /**
     * Provide a live stream of new incoming Message items from the Android Short Message Service (SMS).
     * @return the provider
     */
    public static MStreamProvider asIncomingSMS() {
        return new SMSIncomingMessageProvider();
    }

    /**
     * Provide all Message items from Android Short Message Service SMS.
     * @return the provider
     */
    public static MStreamProvider getAllSMS() {
        return new SMSMessageListProvider();
    }
}
