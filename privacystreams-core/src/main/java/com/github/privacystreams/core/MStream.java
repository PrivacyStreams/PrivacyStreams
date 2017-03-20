package com.github.privacystreams.core;

import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.commons.statistic.StatisticOperators;
import com.github.privacystreams.commons.items.ItemsOperators;
import com.github.privacystreams.core.actions.MStreamAction;
import com.github.privacystreams.core.actions.callback.Callbacks;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.core.transformations.M2STransformation;
import com.github.privacystreams.core.transformations.filter.Filters;
import com.github.privacystreams.core.transformations.group.Groupers;
import com.github.privacystreams.core.transformations.limit.Limiters;
import com.github.privacystreams.core.transformations.map.Mappers;
import com.github.privacystreams.core.transformations.select.Selectors;
import com.github.privacystreams.core.transformations.reorder.Reorders;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The interface of MStream (multi-item stream).
 * An MStreamInterface is a stream containing many items, and each item is an instance of {@link Item}.
 *
 * An MStreamInterface is produced by <code>uqi.getData</code> method.
 * @see UQI#getData(com.github.privacystreams.core.providers.MStreamProvider, Purpose)
 *
 * It can be transformed to another MStreamInterface by transformation functions,
 * such as {@link #filter(String, Object)}, {{@link #groupBy(String)}}, {{@link #map(Function)}}, etc.
 *
 * It can also be transformed to an ISingleItemProvider by transformation functions,
 * such as {{@link #getFirst()}}, {{@link #getItemAt(int)}}, etc.
 *
 * Finally, it can be outputted using {{@link #asList()}}, {{@link #count()}}, etc.
 */
public class MStream extends Stream {
    private Function<Void, MStream> streamProvider;

    @Override
    public Function<Void, MStream> getStreamProvider() {
        return this.streamProvider;
    }

    public MStream(UQI uqi, Function<Void, MStream> streamProvider) {
        super(uqi);
        this.streamProvider = streamProvider;
    }

    /**
     * Transform the current MStream to another MStream.
     * @param m2mStreamTransformation the function used to transform the stream
     * @return the transformed stream
     */
    public MStream transform(M2MTransformation m2mStreamTransformation) {
        return new MStream(this.getUQI(), this.streamProvider.compound(m2mStreamTransformation));
    }

    /**
     * Transform the current MStream to an SStream.
     * @param m2sStreamTransformation the function used to transform the stream
     * @return the collected item
     */
    public SStream transform(M2STransformation m2sStreamTransformation) {
        return new SStream(this.getUQI(), this.streamProvider.compound(m2sStreamTransformation));
    }

    /**
     * Output the current MStream.
     * @param mStreamAction the function used to output stream
     */
    public void output(MStreamAction mStreamAction) {
        this.getUQI().setQuery(this.getStreamProvider().compound(mStreamAction));
        this.getUQI().evaluate(true);
    }

    // *****************************
    // Filters
    // Filters are used to include/exclude some items in a stream.

    /**
     * Filter the stream by testing an item with a predicate.
     * Specifically, keep the items that satisfy the predicate.
     * Eg. <code>filter(eq("x", 100))</code> will keep the items whose x field is equal to 100.
     *
     * @param predicate     the predicate to test the item
     * @return The filtered stream.
     */
    public MStream filter(Function<Item, Boolean> predicate) {
        return this.transform(Filters.keep(predicate));
    }

    /**
     * Filter the stream by checking whether a field equals a value.
     * Specifically, keep the items in which the field equals the given value.
     * Eg. <code>filter("x", 100)</code> will keep the items whose x field is equal to 100.
     *
     * @param fieldName     the name of field to compare
     * @param fieldValue    the value to compare with the field
     * @return The filtered stream.
     */
    public <TValue> MStream filter(String fieldName, TValue fieldValue) {
        return this.filter(Comparators.eq(fieldName, fieldValue));
    }

    // *****************************
    // Limiters
    // Limiters are used to limit the length of a stream.

    /**
     * Limit the stream by testing each item with a predicate.
     * Specifically, keep the stream as long as the predicate holds.
     * Eg. <code>limit(eq("x", 100))</code> will keep all items in the stream as long as x field equals to 100,
     * once an item's x value is not equal to 100, the stream stops.
     *
     * @param predicate     the predicate to test the field
     * @return The limited stream.
     */
    public MStream limit(Function<Item, Boolean> predicate) {
        return this.transform(Limiters.limit(predicate));
    }

    /**
     * Limit the stream with a max number of items.
     * Specifically, stop the stream if the count of items exceeds the threshold.
     * Eg. <code>limit(10)</code> will limit the stream to at most 10 items
     *
     * @param maxCount      the max number of items
     * @return The limited stream.
     */
    public MStream limit(int maxCount) {
        return this.transform(Limiters.limitCount(maxCount));
    }

    /**
     * Limit the stream with a timeout,
     * stop the stream after some time.
     * Eg. <code>timeout(Duration.seconds(10))</code> will limit the stream to at most 10 seconds
     *
     * @param timeoutMilliseconds      the timeout millis seconds
     * @return The limited stream.
     */
    public MStream timeout(long timeoutMilliseconds) {
        return this.transform(Limiters.timeout(timeoutMilliseconds));
    }

    // *****************************
    // Mappers
    // Mappers are used to map each item in the stream to another item

    /**
     * Convert each item in the stream with a function.
     * Eg. <code>map(ItemOperators.setField("x", 10))</code> will set the "x" field of each item to 10 in the stream.
     *
     * @param function      the function to convert the item
     * @return The stream with items after mapping
     */
    public MStream map(Function<Item, Item> function) {
        return this.transform(Mappers.mapEachItem(function));
    }

    /**
     * Project each item by including some fields.
     * Other fields will be removed.
     * Eg. <code>project("name", "email")</code> will only keep the "name" and "email" field in each item.
     *
     * @param fieldsToInclude the fields to include
     * @return The stream with items after projection
     */
    public MStream project(String... fieldsToInclude) {
        return this.map(ItemOperators.includeFields(fieldsToInclude));
    }

    /**
     * Set a field to a new value for each item in the stream.
     * The value is computed with a function that take the item as input.
     * Eg. <code>setField("x", Comparators.gt("y", 10))</code> will set a new boolean field "x" to each item,
     * which indicates whether the "y" field is greater than 10.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param functionToComputeValue the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    public <TValue> MStream setField(String fieldToSet, Function<Item, TValue> functionToComputeValue) {
        return this.map(ItemOperators.setField(fieldToSet, functionToComputeValue));
    }

    /**
     * Set a field to a new value for each item in the stream.
     * This transformation can only be used after invoking group methods ({@link #groupBy(String)}, {@link #localGroupBy(String)}).
     * The value is computed with a function that takes the grouped items as input at runtime.
     * Eg. <code>setGroupField("count", Statistic.count())</code> will set a new field "count" to each item,
     * which represents the number of items in the grouped sub stream.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param subStreamFunction the function to compute the new field value, which takes the grouped items as input.
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    public <TValue> MStream setGroupField(String fieldToSet, Function<List<Item>, TValue> subStreamFunction) {
        return this.map(ItemOperators.setGroupField(fieldToSet, subStreamFunction));
    }

    /**
     * Set the value of a new field with a value generator function.
     * The value generator function is independent from current item, which does not need a input (input type is Void).
     * The value generator will be evaluated on demand at runtime.
     * Eg. `setIndependentField("time", TimeOperators.timestampGenerator())` will set the field "time" to a timestamp in each item;
     * `setIndependentField("wifiStatus", DeviceOperators.wifiStatusChecker())` will set the field "wifiStatus" to a boolean indicating whether wifi is connected in each item.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param valueGenerator the function to compute the field value.
     * @param <TValue> the type of the new field value.
     * @return the stream of items with the new field set
     */
    public <TValue> MStream setIndependentField(String fieldToSet, Function<Void, TValue> valueGenerator) {
        return this.map(ItemOperators.setIndependentField(fieldToSet, valueGenerator));
    }

    // *****************************
    // Reorders
    // Reorders are used to change the order of items in the stream

    /**
     * Sort the items according to the value of a field, in ascending order.
     * Eg. <code>sortBy("timestamp")</code> will sort the items in the stream by timestamp field.
     *
     * @param fieldName     the field used to reorder the stream, in ascending order
     * @return The sorted stream
     */
    public MStream sortBy(String fieldName) {
        return this.transform(Reorders.sortBy(fieldName));
    }

    /**
     * Shuffle the items.
     * <code>shuffle()</code> will randomize the order of the items in the stream.
     *
     * @return The shuffled stream
     */
    public MStream shuffle() {
        return this.transform(Reorders.shuffle());
    }

    /**
     * Reverse the order of items
     * <code>reverse()</code> will reverse the order of the items in the stream.
     *
     * @return The reversed stream
     */
    public MStream reverse() {
        return this.transform(Reorders.reverse());
    }

    // *****************************
    // StreamGrouper
    // StreamGrouper are used to group some items to one item

    /**
     * Group the items according to a field.
     * Eg. <code>groupBy("x")</code> will group the items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    public MStream groupBy(String fieldName) {
        return this.transform(Groupers.groupBy(fieldName));
    }

    /**
     * Group the contiguous items according to a field.
     * eg. <code>localGroupBy("x")</code> will group the contiguous items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    public MStream localGroupBy(String fieldName) {
        return this.transform(Groupers.localGroupBy(fieldName));
    }

    // *****************************
    // Output functions
    // Output functions are used to output the items in a stream

    /**
     * Output the items in the stream with a function to a callback.
     * This method will not be blocked.
     * Eg. <code>outputItems(Statistic.count(), new Callback<Integer>(){...})</code>
     * will count the number of items and callback with the number.
     *
     * @param itemsCollector the function used to output current stream
     * @param resultHandler the function to handle the result
     * @param <Tout> the type of the result
     */
    public <Tout> void output(Function<List<Item>, Tout> itemsCollector, Callback<Tout> resultHandler) {
        this.output(Collectors.collectItems(itemsCollector, resultHandler));
    }

    /**
     * Output the items in the stream with a function.
     * This method will block until the result returns.
     * Eg. <code>outputItems(Statistic.count())</code> will output the number of items.
     *
     * @param itemsCollector the function used to output current stream
     * @param <Tout> the type of the result
     * @return the result
     * @throws PrivacyStreamsException if failed to the result.
     */
    public <Tout> Tout output(Function<List<Item>, Tout> itemsCollector) throws PrivacyStreamsException {
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        Callback<Tout> resultHandler = new Callback<Tout>() {
            @Override
            protected void onSuccess(Tout input) {
                resultQueue.add(input);
            }

            @Override
            protected void onFail(PrivacyStreamsException exception) {
                resultQueue.add(exception);
            }
        };
        this.output(itemsCollector, resultHandler);
        try {
            Object resultOrException = resultQueue.take();
            if (resultOrException instanceof PrivacyStreamsException) {
                throw (PrivacyStreamsException) resultOrException;
            }
            return (Tout) resultOrException;
        } catch (InterruptedException e) {
            throw PrivacyStreamsException.INTERRUPTED(e.getMessage());
        }
    }

    /**
     * Get the first item in the stream.
     *
     * @return SStream whose item is the first item of current MStream
     */
    public SStream getFirst() {
        return this.transform(Selectors.getItemAt(0));
    }

    /**
     * Pick an item in the stream.
     *
     * @param index the index of target item.
     * @return SStream whose item is selected from current MStream with the given index
     */
    public SStream getItemAt(int index) {
        return this.transform(Selectors.getItemAt(index));
    }

    /**
     * Select an item in the stream with a function.
     *
     * @param selector the selector funtion to select the target item.
     * @return SStream whose item is selected from current MStream with the given function
     */
    public SStream select(Function<List<Item>, Item> selector) {
        return this.transform(Selectors.select(selector));
    }

    /**
     * Print the items for debugging.
     */
    public void debug() {
        this.forEach(ItemOperators.debug());
    }

    /**
     * Count the number of items.
     *
     * @return the count of number of items in the stream.
     */
    public int count() throws PrivacyStreamsException {
        return this.output(StatisticOperators.count());
    }

    /**
     * Collect the items in the stream to a list.
     * Each item in the list is an instance of {@link Item}.
     *
     * @return a list of key-value maps, each map represents an item
     */
    public List<Item> asList() throws PrivacyStreamsException {
        return this.output(ItemsOperators.asList());
    }

    /**
     * Select a field in each item and output the items to a list.
     *
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    public <TValue> List<TValue> asList(String fieldToSelect) throws PrivacyStreamsException {
        return this.output(ItemsOperators.<TValue>asList(fieldToSelect));
    }

    /**
     * Callback with each item.
     *
     * @param callback the callback to invoke for each item.
     */
    public void forEach(Function<Item, Void> callback) {
        this.output(Callbacks.forEach(callback));
    }

    /**
     * Callback with a certain field of each item.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for each item field
     * @param <TValue> the type of the field
     */
    public <TValue> void forEach(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.forEachField(fieldToSelect, callback));
    }

    /**
     * Callback with an item when the item changes (is different from the previous item).
     *
     * @param callback the callback to invoke for the changed item
     */
    public void onChange(Function<Item, Void> callback) {
        this.output(Callbacks.onChange(callback));
    }

    /**
     * Callback with a field value of an item when the field value changes.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for the changed item field
     * @param <TValue> the type of the field
     */
    public <TValue> void onChange(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.onFieldChange(fieldToSelect, callback));
    }

    /**
     * Callback with an item once one item is present.
     *
     * @param callback the callback to invoke once the item is present
     */
    public void ifPresent(Function<Item, Void> callback) {
        this.output(Callbacks.ifPresent(callback));
    }

    /**
     * Callback with a field value of an item once the field value is present.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke once the field value is present
     * @param <TValue> the type of the field
     */
    public <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.ifFieldPresent(fieldToSelect, callback));
    }

    /**
     * Fork current stream for reusing.
     *
     * @param numOfForks number of reuses
     * @return the forked stream
     */
    public MStream fork(int numOfForks) {
        return (MStream) super.fork(numOfForks);
    }

    /**
     * Get a value generator that can be evaluated on demand.
     * The generator will not be evaluated immediately, instead, it will be evaluated once `apply()` is called.
     *
     * @return the value generator
     */
    public <Tout> Function<Void, Tout> getValueGenerator(Function<MStream, Tout> streamOutputFunction) {
        return this.streamProvider.compound(streamOutputFunction);
    }

}
