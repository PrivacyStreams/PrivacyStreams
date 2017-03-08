package com.github.privacystreams.communication;

import android.os.Build;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;

/**
 * Created by yuanchun on 07/12/2016.
 * Message message
 */

public class Message extends Item {
    public static final String TYPE = "type";
    public static final String CONTENT = "content";
    public static final String PACKAGE_NAME = "package_name";
    public static final String CONTACT = "contact";
    public static final String TIMESTAMP = "timestamp";


    public enum Type {
        RECEIVED("received"),
        SENT("sent");

        private String type;
        private Type(String type) {
            this.type = type;
        }

        @Override
        public String toString(){
            return type;
        }
    };

    Message(Type type, String content, String packageName, String contact, long timestamp){
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CONTACT, contact);
        this.setFieldValue(TIMESTAMP, timestamp);
    }

    /**
     * Get a provider that provides a live stream of instant messaging messages
     * @return
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
