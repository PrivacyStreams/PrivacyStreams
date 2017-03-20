package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.utils.Logging;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by yuanchun on 28/11/2016.
 * Stream is one of the essential classes used in PrivacyStreams.
 * Most personal data access/process operation in PrivacyStreams use Stream as the intermediate.
 *
 * A Stream is consist of one or multiple items.
 * The items are produced by MStreamProvider functions (like LocationUpdatesProvider, CallLogProvider, etc.),
 * transformed by M2MTransformation functions (like filter, reorder, map, etc.),
 * and outputted by ItemsFunction functions (like print, toList, etc.).
 *
 * Stream producer functions (including MStreamProvider and M2MTransformation)
 * should make sure the stream is not closed before writing items to it, using:
 *      stream.isClosed()
 * Stream consumer functions (including M2MTransformation and ItemsFunction)
 * should stop reading from Stream if the stream is ended.
 *      If stream.read() returns a null, it means the stream is ended.
 */

public abstract class Stream {
    private final UQI uqi;
    private final EventBus eventBus;

    private final Set<Function<? extends Stream, ?>> streamReceivers;

    private transient volatile int receiverCount = 1;
    private transient Queue<Item> streamCache = new ConcurrentLinkedQueue<>();

    Stream(UQI uqi) {
        this.uqi = uqi;
        this.eventBus = new EventBus();
        this.streamReceivers = new HashSet<>();
    }

    /**
     * Write an item to the stream,
     * or write a null to end the stream.
     * @param item  the item to write to the stream, null indicates the end of the stream
     * @param streamProvider the function that provide current stream
     */
    public void write(Item item, Function<?, ? extends Stream> streamProvider) {
        if (streamProvider != this.getStreamProvider() && streamProvider != this.getStreamProvider().getTail()) {
            Logging.warn("Illegal StreamProvider trying to write stream!");
            return;
        }

//        // If receivers are not ready, cache the item
//        if (this.streamReceivers.size() != this.receiverCount) {
//            if (this.getUQI().isStreamDebug())
//                Logging.debug("Receivers are not ready, caching...");
//            this.streamCache.add(item);
//            return;
//        }
//
//        // send all cached items
//        while (true) {
//            Item cachedItem = this.streamCache.poll();
//            if (cachedItem != null)
//                this.doWrite(cachedItem);
//            else
//                break;
//        }
        while (this.streamReceivers.size() != this.receiverCount) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.doWrite(item);
    }

    private void doWrite(Item item) {
        if (this.getUQI().isStreamDebug())
            Logging.debug("Item " + item + " writing to stream " + this.getStreamProvider());

        this.eventBus.post(item);
    }

    /**
     * Register a function to current stream.
     * @param streamReceiver the function that receives stream items
     */
    public synchronized void register(Function<? extends Stream, ?> streamReceiver) {
        if (this.streamReceivers.size() > this.receiverCount) {
            Logging.warn("Unknown StreamProvider trying to subscribe to stream!");
            return;
        }
        this.eventBus.register(streamReceiver);
        this.streamReceivers.add(streamReceiver);
    }

    /**
     * Unregister a function from current stream.
     * @param streamReceiver the function that receives stream items
     */
    public synchronized void unregister(Function<? extends Stream, ?> streamReceiver) {
        if (!this.streamReceivers.contains(streamReceiver)) return;
        this.eventBus.unregister(streamReceiver);
        this.streamReceivers.remove(streamReceiver);
        this.receiverCount--;
    }

    /**
     * Check whether the stream is closed,
     * Stream generator functions should make sure the stream is not closed this writing items to it.
     * @return true if the stream is closed, meaning the stream does not accept new items
     */
    public boolean isClosed() {
        return this.receiverCount <= 0;
    }

    public abstract Function<Void, ? extends Stream> getStreamProvider();

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

    public Stream fork(int numOfForks) {
        this.receiverCount = numOfForks;
        return this;
    }

}
