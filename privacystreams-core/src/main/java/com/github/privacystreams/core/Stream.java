package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.utils.Logging;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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

    private final Set<Function<? extends Stream, ?>> streamReceivers;

    private transient volatile boolean isClosed = false;
    private transient volatile int receiverCount = 1;

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
        Logging.debug("Stream.write " + item + " from " + streamProvider);
        this.eventBus.post(item);
    }

    /**
     * register a function to current stream
     * @param streamReceiver the function that receives stream items
     */
    public void register(Function<? extends Stream, ?> streamReceiver) {
        this.streamReceivers.add(streamReceiver);
        this.eventBus.register(streamReceiver);
    }

    /**
     * unregister a function from current stream
     * @param streamReceiver the function that receives stream items
     */
    public void unregister(Function<? extends Stream, ?> streamReceiver) {
        this.streamReceivers.remove(streamReceiver);
        this.eventBus.unregister(streamReceiver);
        if (this.streamReceivers.size() == 0) this.isClosed = true;
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
