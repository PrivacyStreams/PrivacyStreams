package com.github.privacystreams.core;

import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.debug.DebugOperators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.commons.items.ItemsOperators;
import com.github.privacystreams.commons.statistic.StatisticOperators;
import com.github.privacystreams.core.actions.MStreamAction;
import com.github.privacystreams.core.actions.callback.Callbacks;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.core.transformations.M2STransformation;
import com.github.privacystreams.core.transformations.filter.Filters;
import com.github.privacystreams.core.transformations.group.Groupers;
import com.github.privacystreams.core.transformations.limit.Limiters;
import com.github.privacystreams.core.transformations.map.Mappers;
import com.github.privacystreams.core.transformations.reorder.Reorders;
import com.github.privacystreams.core.transformations.select.Selectors;
import com.github.privacystreams.utils.annotations.PSAction;
import com.github.privacystreams.utils.annotations.PSTransformation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The interface of MStream (multi-item stream).
 * An MStream is a stream containing many items, and each item is an instance of `Item`.
 * An MStream is produced by `uqi.getData` method.
 *
 * It can be transformed to another MStream by transformation functions,
 * such as `filter`, `groupBy`, `map`, etc.
 *
 * It can also be transformed to an SStream by transformation functions,
 * such as `getFirst`, `getItemAt`, etc.
 *
 * Finally, it can be outputted using `asList()`, `count`, etc.
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
        this.getUQI().evaluate(this.getStreamProvider().compound(mStreamAction), true);
    }

    // *****************************
    // Filters
    // Filters are used to include/exclude some items in a stream.

    /**
     * Filter the stream by testing an item with a function.
     * Specifically, keep the items that satisfy the function (aka. the function returns true).
     * Eg. `filter(eq("x", 100))` will keep the items whose x field is equal to 100.
     *
     * @param itemChecker     the function to check each item.
     * @return The filtered stream.
     */
    @PSTransformation()
    public MStream filter(Function<Item, Boolean> itemChecker) {
        return this.transform(Filters.keep(itemChecker));
    }

    /**
     * Filter the stream by checking whether a field equals a value.
     * Specifically, keep the items in which the field equals the given value.
     * Eg. `filter("x", 100)` will keep the items whose x field is equal to 100.
     *
     * @param fieldName     the name of field to check
     * @param fieldValue    the value to compare with the field
     * @return The filtered stream.
     */
    @PSTransformation()
    public <TValue> MStream filter(String fieldName, TValue fieldValue) {
        return this.filter(Comparators.eq(fieldName, fieldValue));
    }

    // *****************************
    // Limiters
    // Limiters are used to limit the length of a stream.

    /**
     * Limit the stream by checking each item with a function.
     * Specifically, keep the stream as long as the checker holds (aka. the checker function returns true).
     * Eg. `limit(eq("x", 100))` will keep all items in the stream as long as x field equals to 100,
     * once an item's x value is not equal to 100, the stream stops.
     *
     * @param itemChecker     the function to check each item.
     * @return The limited stream.
     */
    @PSTransformation()
    public MStream limit(Function<Item, Boolean> itemChecker) {
        return this.transform(Limiters.limit(itemChecker));
    }

    /**
     * Limit the stream with a max number of items.
     * Specifically, stop the stream if the count of items exceeds the threshold.
     * Eg. `limit(10)` will limit the stream to at most 10 items
     *
     * @param maxCount      the max number of items
     * @return The limited stream.
     */
    @PSTransformation()
    public MStream limit(int maxCount) {
        return this.transform(Limiters.limitCount(maxCount));
    }

    /**
     * Limit the stream with a timeout, stop the stream after time out.
     * Eg. `timeout(Duration.seconds(10))` will limit the stream to at most 10 seconds
     *
     * @param timeoutMilliseconds      the timeout milliseconds
     * @return The limited stream.
     */
    @PSTransformation()
    public MStream timeout(long timeoutMilliseconds) {
        return this.transform(Limiters.timeout(timeoutMilliseconds));
    }

    // *****************************
    // Mappers
    // Mappers are used to map each item in the stream to another item

    /**
     * Convert each item in the stream with a function.
     * Eg. `map(ItemOperators.setField("x", 10))` will set the "x" field of each item to 10 in the stream.
     *
     * @param itemConverter      the function to map each item to another item
     * @return The stream with items after mapping
     */
    @PSTransformation()
    public MStream map(Function<Item, Item> itemConverter) {
        return this.transform(Mappers.mapEachItem(itemConverter));
    }

    /**
     * Project each item by including some fields.
     * Other fields will be removed.
     * Eg. `project("name", "email")` will only keep the "name" and "email" field in each item.
     *
     * @param fieldsToInclude the fields to include
     * @return The stream with items after projection
     */
    @PSTransformation()
    public MStream project(String... fieldsToInclude) {
        return this.map(ItemOperators.includeFields(fieldsToInclude));
    }

    /**
     * Set a field to a new value for each item in the stream.
     * The value is computed with a function that take the item as input.
     * Eg. `setField("x", Comparators.gt("y", 10))` will set a new boolean field "x" to each item,
     * which indicates whether the "y" field is greater than 10.
     *
     * @param fieldToSet the name of the field to set.
     * @param fieldValueComputer the function to compute the value of the new field based on each item.
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    @PSTransformation()
    public <TValue> MStream setField(String fieldToSet, Function<Item, TValue> fieldValueComputer) {
        return this.map(ItemOperators.setField(fieldToSet, fieldValueComputer));
    }

    /**
     * Set a field to a new value for each item in the stream.
     * This transformation can only be used after invoking group methods (`groupBy`, `localGroupBy`).
     * The value is computed with a function that takes the grouped items as input at runtime.
     * Eg. `setGroupField("count", StatisticOperators.count())` will set a new field "count" to each item,
     * which represents the number of items in the grouped sub stream.
     *
     * @param fieldToSet the name of the field to set.
     * @param fieldValueComputer the function to compute the new field value, which takes the list of grouped items as input.
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    @PSTransformation()
    public <TValue> MStream setGroupField(String fieldToSet, Function<List<Item>, TValue> fieldValueComputer) {
        return this.map(ItemOperators.setGroupField(fieldToSet, fieldValueComputer));
    }

    /**
     * Set the value of a new field with a value generator function.
     * The value generator function is independent from current item, which does not need a input (input type is Void).
     * The value generator will be evaluated on demand at runtime.
     * Eg. `setIndependentField("time", TimeOperators.getCurrentTime())` will set the field "time" to a timestamp in each item;
     * `setIndependentField("wifiStatus", DeviceOperators.isWifiConnected())` will set the field "wifiStatus" to a boolean indicating whether wifi is connected in each item.
     *
     * @param fieldToSet the name of the field to set.
     * @param valueGenerator the function to compute the field value.
     * @param <TValue> the type of the new field value.
     * @return the stream of items with the new field set
     */
    @PSTransformation()
    public <TValue> MStream setIndependentField(String fieldToSet, Function<Void, TValue> valueGenerator) {
        return this.map(ItemOperators.setIndependentField(fieldToSet, valueGenerator));
    }

    // *****************************
    // Reorders
    // Reorders are used to change the order of items in the stream

    /**
     * Sort the items according to the value of a field, in ascending order.
     * Eg. `sortBy("timestamp")` will sort the items in the stream by timestamp field.
     *
     * @param fieldName     the field used to sort the items in current stream, in ascending order
     * @return The stream with sorted items.
     */
    @PSTransformation(changeOrder = true)
    public MStream sortBy(String fieldName) {
        return this.transform(Reorders.sortBy(fieldName));
    }

    /**
     * Shuffle the items.
     * `shuffle()` will randomize the order of the items in the stream.
     *
     * @return The stream with shuffled items.
     */
    @PSTransformation(changeOrder = true)
    public MStream shuffle() {
        return this.transform(Reorders.shuffle());
    }

    /**
     * Reverse the order of items
     * `reverse()` will reverse the order of the items in the stream.
     *
     * @return The stream with reversed items.
     */
    @PSTransformation(changeOrder = true)
    public MStream reverse() {
        return this.transform(Reorders.reverse());
    }

    // *****************************
    // StreamGrouper
    // StreamGrouper are used to group some items to one item

    /**
     * Group the items according to a field.
     * After grouping, the item will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg. `groupBy("x")` will group the items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param fieldName     the field used to group the items in current stream.
     * @return The grouped stream
     */
    @PSTransformation(changeOrder = true)
    public MStream groupBy(String fieldName) {
        return this.transform(Groupers.groupBy(fieldName));
    }

    /**
     * Group the **contiguous** items according to a field.
     * After grouping, the item will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg.  `localGroupBy("x")` will group the contiguous items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    @PSTransformation
    public MStream localGroupBy(String fieldName) {
        return this.transform(Groupers.localGroupBy(fieldName));
    }

    // *****************************
    // Output functions
    // Output functions are used to output the items in a stream

    /**
     * Output the items in the stream with a function, and pass the result to a callback.
     * This method will not be blocked.
     * Eg. `outputItems(StatisticOperators.count(), new Callback<Integer>(){...})`
     * will count the number of items and callback with the number.
     *
     * @param resultComputer the function used to compute result based on the items in current stream
     * @param resultHandler the function to handle the result
     * @param <Tout> the type of the result
     */
    @PSAction(blocking = false)
    public <Tout> void output(Function<List<Item>, Tout> resultComputer, Callback<Tout> resultHandler) {
        this.output(Collectors.collectItems(resultComputer, resultHandler));
    }

    /**
     * Output the items in the stream with a function.
     * This method will block until the result returns.
     * Eg. `output(StatisticOperators.count())` will output the number of items.
     *
     * @param itemsCollector the function used to output current stream
     * @param <Tout> the type of the result
     * @return the result
     * @throws PSException if failed to the result.
     */
    @PSAction(blocking = true)
    public <Tout> Tout output(Function<List<Item>, Tout> itemsCollector) throws PSException {
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        Callback<Tout> resultHandler = new Callback<Tout>() {
            @Override
            protected void onInput(Tout input) {
                resultQueue.add(input);
            }

            @Override
            protected void onFail(PSException exception) {
                resultQueue.add(exception);
            }
        };
        this.output(itemsCollector, resultHandler);
        try {
            Object resultOrException = resultQueue.take();
            if (resultOrException instanceof PSException) {
                throw (PSException) resultOrException;
            }
            return (Tout) resultOrException;
        } catch (InterruptedException e) {
            throw PSException.INTERRUPTED(e.getMessage());
        }
    }

    /**
     * Get the first item in the stream.
     *
     * @return SStream whose item is the first item of current MStream
     */
    @PSTransformation
    public SStream getFirst() {
        return this.transform(Selectors.getItemAt(0));
    }

    /**
     * Pick an item in the stream.
     *
     * @param index the index of target item.
     * @return SStream whose item is selected from current MStream with the given index
     */
    @PSTransformation
    public SStream getItemAt(int index) {
        return this.transform(Selectors.getItemAt(index));
    }

    /**
     * Select an item in the stream with a function.
     *
     * @param selector the selector funtion to select the target item.
     * @return SStream whose item is selected from current MStream with the given function
     */
    @PSTransformation
    public SStream select(Function<List<Item>, Item> selector) {
        return this.transform(Selectors.select(selector));
    }

    /**
     * Print the items for debugging.
     */
    @PSAction(blocking = false)
    public void debug() {
        this.forEach(DebugOperators.<Item>debug());
    }

    /**
     * Print the items in current stream.
     *
     * @param logTag the log tag to use in printing current stream
     */
    @PSTransformation
    public MStream logAs(String logTag) {
        return this.map(DebugOperators.<Item>logAs(logTag));
    }

    /**
     * Count the number of items.
     *
     * @return the count of number of items in the stream.
     */
    @PSAction(blocking = true)
    public int count() throws PSException {
        return this.output(StatisticOperators.count());
    }

    /**
     * Collect the items in the stream to a list.
     * Each item in the list is an instance of `Item`.
     *
     * @return a list of key-value maps, each map represents an item
     */
    @PSAction(blocking = true)
    public List<Item> asList() throws PSException {
        return this.output(ItemsOperators.asList());
    }

    /**
     * Select a field in each item and output the items to a list.
     *
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    @PSAction(blocking = true)
    public <TValue> List<TValue> asList(String fieldToSelect) throws PSException {
        return this.output(ItemsOperators.<TValue>asList(fieldToSelect));
    }

    /**
     * Callback with each item.
     *
     * @param callback the callback to invoke for each item.
     */
    @PSAction(blocking = false)
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
    @PSAction(blocking = false)
    public <TValue> void forEach(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.forEachField(fieldToSelect, callback));
    }

    /**
     * Callback with an item when the item changes (is different from the previous item).
     *
     * @param callback the callback to invoke for the changed item
     */
    @PSAction(blocking = false)
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
    @PSAction(blocking = false)
    public <TValue> void onChange(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.onFieldChange(fieldToSelect, callback));
    }

    /**
     * Callback with an item once one item is present.
     *
     * @param callback the callback to invoke once the item is present
     */
    @PSAction(blocking = false)
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
    @PSAction(blocking = false)
    public <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.ifFieldPresent(fieldToSelect, callback));
    }

    /**
     * Reuse current stream.
     *
     * @param numOfReuses number of reuses
     * @return the stream ready for reuse
     */
    @PSTransformation
    public MStream reuse(int numOfReuses) {
        this.getUQI().reuse(this, numOfReuses);
        return this;
    }

    /**
     * Get a value generator that can be evaluated on demand.
     * The generator will not be evaluated immediately, instead, it will be evaluated once `apply()` is called.
     *
     * @param streamOutputter the function to output the stream
     * @return the value generator
     */
    public <Tout> Function<Void, Tout> getValueGenerator(Function<MStream, Tout> streamOutputter) {
        return this.streamProvider.compound(streamOutputter);
    }

}
