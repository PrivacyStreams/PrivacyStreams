package com.github.privacystreams.core;

import com.github.privacystreams.utils.Logging;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
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

    transient volatile int receiverCount;
    private transient List<Item> streamItems;
    private transient final List<Function<? extends Stream, ?>> receivers;
    private transient final List<EventBus> eventBuses;
    private transient final List<Integer> numSents;

    Stream(UQI uqi) {
        this.uqi = uqi;

        this.receiverCount = 1;
        this.streamItems = new ArrayList<>();
        this.receivers = new ArrayList<>();
        this.eventBuses = new ArrayList<>();
        this.numSents = new ArrayList<>();
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

        int numExistingItems = this.streamItems.size();
        if (numExistingItems == 0 || this.streamItems.get(numExistingItems - 1) != Item.EOS) {
            this.streamItems.add(item);
        }
        this.syncItems();
    }

    private synchronized void syncItems() {
        int numReceived = this.streamItems.size();

        for (int i = 0; i < this.receivers.size(); i++) {
            int numSent = this.numSents.get(i);
            if (numSent < numReceived) {
                this.numSents.set(i, numReceived);
                EventBus eventBus = this.eventBuses.get(i);
                int currentReceiverCount = this.receiverCount;
                for (int itemId = numSent; itemId < numReceived; itemId++) {
                    eventBus.post(streamItems.get(itemId));
                    if (currentReceiverCount != this.receiverCount) break;
                }
            }
        }
    }

    /**
     * Register a function to current stream.
     * @param streamReceiver the function that receives stream items
     */
    public synchronized void register(Function<? extends Stream, ?> streamReceiver) {
        if (this.receivers.size() >= this.receiverCount) {
            Logging.warn("Unknown StreamProvider trying to subscribe to stream!");
            return;
        }
        EventBus eventBus = new EventBus();
        eventBus.register(streamReceiver);
        this.receivers.add(streamReceiver);
        this.eventBuses.add(eventBus);
        this.numSents.add(0);

        Stream.this.syncItems();
    }

    /**
     * Unregister a function from current stream.
     * @param streamReceiver the function that receives stream items
     */
    public synchronized void unregister(Function<? extends Stream, ?> streamReceiver) {
        if (!this.receivers.contains(streamReceiver)) return;
        int receiverId = this.receivers.indexOf(streamReceiver);
        this.eventBuses.get(receiverId).unregister(streamReceiver);
        this.receivers.remove(receiverId);
        this.eventBuses.remove(receiverId);
        this.numSents.remove(receiverId);
        this.receiverCount--;

        if (this.isClosed()) {
            this.getStreamProvider().cancel(this.uqi);
            this.streamItems.clear();
        }
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

    public UQI getUQI() {
        return this.uqi;
    }

    public Stream reuse(int numOfReuses) {
        this.receiverCount = numOfReuses;
        return this;
    }

}
