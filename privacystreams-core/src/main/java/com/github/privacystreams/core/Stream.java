package com.github.privacystreams.core;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yuanchun on 28/11/2016.
 * Stream is one of the essential classes used in PrivacyStreams.
 * Most personal data access/process operation in PrivacyStreams use Stream as the intermediate.
 *
 * A Stream is consist of one or multiple items.
 * The items are produced by MultiItemStreamProvider functions (like LocationUpdatesProvider, CallLogProvider, etc.),
 * transformed by M2MTransformation functions (like filter, reorder, map, etc.),
 * and outputted by ItemsFunction functions (like print, toList, etc.).
 *
 * Stream producer functions (including MultiItemStreamProvider and M2MTransformation)
 * should make sure the stream is not closed before writing items to it, using:
 *      stream.isClosed()
 * Stream consumer functions (including M2MTransformation and ItemsFunction)
 * should stop reading from Stream if the stream is ended.
 *      If stream.read() returns a null, it means the stream is ended.
 */

public abstract class Stream {
    private final UQI uqi;
    private final EventBus eventBus;

    private volatile boolean isClosed = false;
    private volatile boolean isEmpty = false;
    private volatile boolean isActive = false;

    Stream(UQI uqi) {
        this.uqi = uqi;
        this.eventBus = new EventBus();
    }

    /**
     * Write an item to the stream,
     * or write a null to end the stream.
     * @param item  the item to write to the stream, null indicates the end of the stream
     */
    public void write(Item item) {
        this.eventBus.post(item);
    }

    /**
     * Subscribe current stream
     * @param receiverFunction the function that receives stream items
     */
    public void subscribe(Function<? extends Stream, ?> receiverFunction) {
        this.eventBus.register(receiverFunction);
    }

    /**
     * Unsubscribe current stream
     * @param receiverFunction the function that receives stream items
     */
    public void unSubscribe(Function<? extends Stream, ?> receiverFunction) {
        this.eventBus.unregister(receiverFunction);
    }

    /**
     * Check whether the stream is closed,
     * Stream generator functions should make sure the stream is not closed this writing items to it.
     * @return true if the stream is closed, meaning the stream does not accept new items
     */
    public boolean isClosed() {
        return this.isClosed;
    }

    public abstract Function<Void, ? extends Stream> getStreamProvider();

    /**
     * Close the stream
     * By closing the stream, it does not accept new items from the MultiItemStreamProvider any more.
     */
    public void close() {
        this.isClosed = true;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("streamProvider", this.getStreamProvider().toString());
        return outputMap;
    }

    public String toString() {
        return this.toMap().toString();
    }

    public Context getContext() {
        return this.getUQI().getContext();
    }

    public UQI getUQI() {
        return this.uqi;
    }
}
