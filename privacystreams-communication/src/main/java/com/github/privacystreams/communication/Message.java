package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 07/12/2016.
 * Message message
 */

public class Message extends Item {
    public static final String TYPE = "type";
    public static final String CONTENT = "content";
    public static final String APP = "app";
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
