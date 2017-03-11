package com.github.privacystreams.communication;

import android.os.Build;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A Message item represents a message. It could be from SMS, WhatsApp, Facebook, etc.
 */
@PSItem
public class Message extends Item {

    /**
     * The message type, could be "received" or "sent".
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

    public static class Types {
        public static final String RECEIVED = "received";
        public static final String SENT = "sent";
    };

    Message(String type, String content, String packageName, String contact, long timestamp){
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CONTACT, contact);
        this.setFieldValue(TIMESTAMP, timestamp);
    }

    /**
     * Get a provider that provides a live stream of instant messaging messages
     * @return the provider
     */
     public static MultiItemStreamProvider asIMUpdates(){
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
             return new IMUpdatesProvider();
         else {
             Logging.warn("Illegal SDK version.");
             return null;
         }
    }
    /**
     * Get a provider that provides a live stream of incoming Message messages
     * @return the provider
     */
    public static MultiItemStreamProvider asSMSUpdates() {
        return new SMSMessageUpdatesProvider();
    }

    /**
     * Get a provider that provides a stream of Message messages asSMSHistory
     * @return the provider
     */
    public static MultiItemStreamProvider asSMSHistory() {
        // TODO implement SMSHistoryProvider
        return null;
    }
}
