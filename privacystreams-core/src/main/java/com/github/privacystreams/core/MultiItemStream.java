package com.github.privacystreams.core;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.privacystreams.core.actions.MultiItemStreamAction;
import com.github.privacystreams.core.actions.callback.Callbacks;
import com.github.privacystreams.core.exceptions.PipelineInterruptedException;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.commons.common.ItemCommons;
import com.github.privacystreams.core.commons.common.StreamCommons;
import com.github.privacystreams.core.commons.comparison.Comparisons;
import com.github.privacystreams.core.commons.print.Printers;
import com.github.privacystreams.core.commons.statistic.Statistics;
import com.github.privacystreams.core.transformations.filter.Filters;
import com.github.privacystreams.core.transformations.group.Groupers;
import com.github.privacystreams.core.transformations.limit.Limiters;
import com.github.privacystreams.core.transformations.map.Mappers;
import com.github.privacystreams.core.transformations.pick.Pickers;
import com.github.privacystreams.core.transformations.reorder.Reorders;

/**
 * Created by yuanchun on 28/11/2016.
 * MultiItemStream is one of the essential classes used in PrivacyStreams.
 * Most personal data access/process operation in PrivacyStreams use MultiItemStream as the intermediate.
 *
 * A MultiItemStream is consist of a list of items.
 * The items are produced by MultiItemStreamProvider functions (like LocationUpdatesProvider, CallLogProvider, etc.),
 * transformed by M2MTransformation functions (like filter, reorder, map, etc.),
 * and outputted by ItemsFunction functions (like print, toList, etc.).
 *
 * MultiItemStream producer functions (including MultiItemStreamProvider and M2MTransformation)
 * should make sure the stream is not closed before writing items to it, using:
 *      stream.isClosed()
 * MultiItemStream consumer functions (including M2MTransformation and ItemsFunction)
 * should stop reading from MultiItemStream if the stream is ended.
 *      If stream.read() returns a null, it means the stream is ended.
 */

public class MultiItemStream extends Stream implements IMultiItemStream {
    private Function<Void, MultiItemStream> streamProvider;

    @Override
    public Function<Void, MultiItemStream> getStreamProvider() {
        return this.streamProvider;
    }

    public MultiItemStream(UQI uqi, Function<Void, MultiItemStream> streamProvider) {
        super(uqi);
        this.streamProvider = streamProvider;
    }

    /**
     * Transform the stream to another stream
     * @param mStreamTransformation the function used to transform current stream
     * @return the transformed stream
     */
    public MultiItemStream transform(Function<MultiItemStream, MultiItemStream> mStreamTransformation) {
        return new MultiItemStream(this.getUQI(), this.streamProvider.compound(mStreamTransformation));
    }

    /**
     * Collect an item in the stream, the item can be further processed using item functions
     * @param m2sStreamTransformation the function used to convert the stream to an item
     * @return the collected item
     */
    public SingleItemStream transformToItem(Function<MultiItemStream, SingleItemStream> m2sStreamTransformation) {
        return new SingleItemStream(this.getUQI(), this.streamProvider.compound(m2sStreamTransformation));
    }

    /**
     * Collect the items in the stream for output
     * @param mStreamAction the function used to output current stream
     */
    public void output(Function<MultiItemStream, Void> mStreamAction) {
        this.getUQI().setQuery(this.getStreamProvider().compound(mStreamAction));
        this.getUQI().evaluate(true);
    }

    // *****************************
    // Filters
    // Filters are used to include/exclude some items in a stream.

    /**
     * filter the stream by testing an item with a predicate,
     * keep the items that satisfy the predicate.
     * eg. filter(eq("x", 100)) will keep the items whose x field is equal to 100
     *
     * @param predicate     the predicate to test the item
     * @return The filtered stream.
     */
    public MultiItemStream filter(Function<Item, Boolean> predicate) {
        return this.transform(Filters.keep(predicate));
    }

    /**
     * filter the stream by checking whether a field equals a value,
     * keep the items in which the field equals the given value.
     * eg. filter("x", 10) will keep the items whose x field is equal to 10
     *
     * @param fieldName     the name of field to compare
     * @param fieldValue    the value to compare with the field
     * @return The filtered stream.
     */
    public <TValue> MultiItemStream filter(String fieldName, TValue fieldValue) {
        return this.filter(Comparisons.eq(fieldName, fieldValue));
    }

    // *****************************
    // Limiters
    // Limiters are used to limit the length of a stream.

    /**
     * Limit the stream by testing a field with a predicate,
     * keep the stream as long as the predicate holds.
     * eg. limit(eq("x", 100)) will keep all items in the stream as long as x field equals to 100,
     * once an item's x value is not equal to 100, the stream stops.
     *
     * @param predicate     the predicate to test the field
     * @return The limited stream.
     */
    public MultiItemStream limit(Function<Item, Boolean> predicate) {
        return this.transform(Limiters.limit(predicate));
    }

    /**
     * Limit the stream by checking the count of items,
     * stop the stream if the count of items exceeds the threshold.
     * eg. limit(10) will limit the stream to at most 10 items
     *
     * @param maxCount      the threshold of count
     * @return The limited stream.
     */
    public MultiItemStream limit(int maxCount) {
        return this.transform(Limiters.limitCount(maxCount));
    }

    /**
     * Limit the stream with a timeout,
     * stop the stream after some time.
     * eg. timeout(Duration.seconds(10)) will limit the stream to at most 10 seconds
     *
     * @param timeoutMilliseconds      the timeout millis seconds
     * @return The limited stream.
     */
    public MultiItemStream timeout(long timeoutMilliseconds) {
        return this.transform(Limiters.timeout(timeoutMilliseconds));
    }

    // *****************************
    // Mappers
    // Mappers are used to map each item in the stream to another item

    /**
     * Convert an item with a function.
     * eg. map(blur_photo("image")) will blur the "image" field of each item in the stream
     *
     * @param function      the function to convert the item
     * @return The stream with items after mapping
     */
    public MultiItemStream map(Function<Item, Item> function) {
        return this.transform(Mappers.mapEachItem(function));
    }

    /**
     * Project each item by including some fields.
     * Other fields will not appear in collectors, such as list(), foreach(Callback ...).
     * eg. project("name", "email") will only keep the "name" and "email" field in each item
     *
     * @param fieldsToInclude the fields to include
     * @return The stream with items after projection
     */
    public MultiItemStream project(String... fieldsToInclude) {
        return this.map(ItemCommons.includeFields(fieldsToInclude));
    }

    /**
     * Add a field to each item in the stream with a function
     * @param newField the new field name
     * @param functionToComputeValue the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    public <TValue> MultiItemStream setField(String newField, Function<Item, TValue> functionToComputeValue) {
        return this.map(ItemCommons.setField(newField, functionToComputeValue));
    }

    /**
     * add a field to each item in the grouped stream with a function
     * this method must be invoked after invoking group method
     * @param newField the new field name
     * @param subStreamFunction the function to compute the new field value,
     *                          the function will be applied to the sub stream of the item
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    public <TValue> MultiItemStream setGroupField(String newField, Function<List<Item>, TValue> subStreamFunction) {
        return this.setField(newField, ItemCommons.outputSubStream(subStreamFunction));
    }

    // *****************************
    // Reorders
    // Reorders are used to change the order of items in the stream

    /**
     * Sort the items according to the value of a field, in ascending order.
     * eg. sortBy("timestamp") will reorder the stream based on timestamp field
     *
     * @param fieldName     the field used to reorder the stream, in ascending order
     * @return The sorted stream
     */
    public MultiItemStream sortBy(String fieldName) {
        return this.transform(Reorders.sortBy(fieldName));
    }

    /**
     * Shuffle the items in the stream
     * shuffle() will change the order of the items in the stream
     *
     * @return The shuffled stream
     */
    public MultiItemStream shuffle() {
        return this.transform(Reorders.shuffle());
    }

    /**
     * Reverse the order of items in the stream
     * reverse() will reverse the order of the items in the stream
     *
     * @return The reversed stream
     */
    public MultiItemStream reverse() {
        return this.transform(Reorders.reverse());
    }

    // *****************************
    // StreamGrouper
    // StreamGrouper are used to group some items to one item

    /**
     * Group the items according to a field.
     * eg. groupBy("x") will group the items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    public MultiItemStream groupBy(String fieldName) {
        return this.transform(Groupers.groupBy(fieldName));
    }

    /**
     * Group the contiguous items according to a field.
     * eg. localGroupBy("x") will group the contiguous items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    public MultiItemStream localGroupBy(String fieldName) {
        return this.transform(Groupers.localGroupBy(fieldName));
    }

    // *****************************
    // Output functions
    // Output functions are used to output the items in a stream

    public <Tout> void outputItems(Function<List<Item>, Tout> itemsOutputFunction, Function<Tout, Void> resultHandler) {
        this.output(new MultiItemStreamAction<>(itemsOutputFunction, resultHandler));
    }

    public <Tout> Tout outputItems(Function<List<Item>, Tout> itemsOutputFunction) throws PrivacyStreamsException {
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        Function<Tout, Void> resultHandler = new Callback<Tout>() {
            @Override
            protected void onSuccess(Tout input) {
                resultQueue.add(input);
            }

            @Override
            protected void onFail(PrivacyStreamsException exception) {
                resultQueue.add(exception);
            }
        };
        this.outputItems(itemsOutputFunction, resultHandler);
        try {
            Object resultOrException = resultQueue.take();
            if (resultOrException instanceof PrivacyStreamsException) {
                throw (PrivacyStreamsException) resultOrException;
            }
            return (Tout) resultOrException;
        } catch (InterruptedException e) {
            throw new PipelineInterruptedException();
        }
    }

    /**
     * Get the first item in the stream.
     * @return the first item in the stream
     */
    public SingleItemStream first() {
        return this.transformToItem(Pickers.pick(0));
    }

    /**
     * Pick an item in the stream.
     * @param index the index of target item
     * @return the item with the given index in the stream
     */
    public SingleItemStream pick(int index) {
        return this.transformToItem(Pickers.pick(index));
    }

    /**
     * Print the items
     */
    public void print() {
        this.forEach(Printers.print());
    }

    /**
     * Debug print the items
     */
    public void debug() {
        this.forEach(Printers.debug());
    }

    /**
     * Calculate the count of items
     * @return the count of number of items in the stream
     */
    public int count() throws PrivacyStreamsException {
        return this.outputItems(Statistics.count());
    }

    /**
     * Collect each item in this stream to a list
     * Each item in the list is a key-value map
     * @return a list of key-value maps, each map represents an item
     */
    public List<Item> asList() throws PrivacyStreamsException {
        return this.outputItems(StreamCommons.asList());
    }

    /**
     * Select a field in each item and output the items to a list
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    public <TValue> List<TValue> asList(String fieldToSelect) throws PrivacyStreamsException {
        return this.outputItems(StreamCommons.<TValue>asList(fieldToSelect));
    }

    /**
     * callback with each item
     * @param callback the callback to invoke for each item
     */
    public void forEach(Function<Item, Void> callback) {
        this.output(Callbacks.forEach(callback));
    }

    /**
     * callback with a certain field of each item
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for each item field
     * @param <TValue> the type of the field
     */
    public <TValue> void forEach(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.forEachField(fieldToSelect, callback));
    }

    /**
     * callback with an item map when the item changes
     * @param callback the callback to invoke for the changed item
     */
    public void onChange(Function<Item, Void> callback) {
        this.output(Callbacks.onChange(callback));
    }

    /**
     * callback with a field value of an item when the field value changes
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for the changed item field
     * @param <TValue> the type of the field
     */
    public <TValue> void onChange(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.onFieldChange(fieldToSelect, callback));
    }

    /**
     * callback with an item map once one item is present
     * @param callback the callback to invoke once the item is present
     */
    public void ifPresent(Function<Item, Void> callback) {
        this.output(Callbacks.ifPresent(callback));
    }

    /**
     * callback with a field value of an item once the field value is present
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke once the field value is present
     * @param <TValue> the type of the field
     */
    public <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.ifFieldPresent(fieldToSelect, callback));
    }
}
