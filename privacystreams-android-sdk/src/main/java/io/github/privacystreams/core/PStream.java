package io.github.privacystreams.core;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.debug.DebugOperators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.commons.items.ItemsOperators;
import io.github.privacystreams.commons.statistic.StatisticOperators;
import io.github.privacystreams.core.actions.callback.Callbacks;
import io.github.privacystreams.core.actions.collect.Collectors;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.transformations.filter.Filters;
import io.github.privacystreams.core.transformations.group.Groupers;
import io.github.privacystreams.core.transformations.limit.Limiters;
import io.github.privacystreams.core.transformations.map.Mappers;
import io.github.privacystreams.core.transformations.merge.Mergers;
import io.github.privacystreams.core.transformations.reorder.Reorders;
import io.github.privacystreams.utils.annotations.PSAction;
import io.github.privacystreams.utils.annotations.PSTransformation;

/**
 * The interface of PStream (privacy stream).
 * An PStream is a stream containing privacy sensitive items, and each item is an instance of `Item`.
 * An PStream is produced by `uqi.getData` method.
 *
 * It can be transformed to another PStream using transformation functions,
 * such as `filter`, `groupBy`, `map`, etc.
 *
 * Finally, it can be outputted using `asFieldList()`, `count`, etc.
 */
public class PStream extends Stream {
    private Function<Void, PStream> streamProvider;

    @Override
    public Function<Void, PStream> getStreamProvider() {
        return this.streamProvider;
    }

    public PStream(UQI uqi, Function<Void, PStream> streamProvider) {
        super(uqi);
        this.streamProvider = streamProvider;
    }

    /**
     * Transform the current PStream to another PStream.
     * @param pStreamTransformation the function used to transform the stream
     * @return the transformed stream
     */
    public PStream transform(PStreamTransformation pStreamTransformation) {
        return new PStream(this.getUQI(), this.streamProvider.compound(pStreamTransformation));
    }

    /**
     * Output the current PStream.
     * @param pStreamAction the function used to output stream
     */
    public void output(PStreamAction pStreamAction) {
        this.getUQI().evaluate(this.getStreamProvider().compound(pStreamAction), true);
    }

    // *****************************
    // Filters
    // Filters are used to include/exclude some items in a stream.

    /**
     * Filter the stream by testing an item with a function.
     * Specifically, keep the items that satisfy the function (aka. the function returns true).
     * Eg. `filter(eq("x", 100))` will keep the items whose x field is equal to 100.
     *
     * @param itemChecker the function to check each item.
     * @return The filtered stream.
     */
    @PSTransformation()
    public PStream filter(Function<Item, Boolean> itemChecker) {
        return this.transform(Filters.keep(itemChecker));
    }

    /**
     * Filter the stream by checking whether a field equals a value.
     * Specifically, keep the items in which the field equals the given value.
     * Eg. `filter("x", 100)` will keep the items whose x field is equal to 100.
     *
     * @param fieldName the name of field to check
     * @param fieldValue the value to compare with the field
     * @return The filtered stream.
     */
    @PSTransformation()
    public <TValue> PStream filter(String fieldName, TValue fieldValue) {
        return this.filter(Comparators.eq(fieldName, fieldValue));
    }

    /**
     * Only keep the items that are different from the previous ones in the stream.
     * Eg. a stream [1, 1, 2, 2, 2, 1, 1] will be [1, 2, 1] after `keepChanges()`
     *
     * @return the filtered stream.
     */
    @PSTransformation()
    public PStream keepChanges() {
        return this.transform(Filters.keepChanges());
    }

    /**
     * Only Keep the items whose fields are different from the previous ones in the stream.
     * Similar to `keepChanges()`, but only monitor a certain field
     *
     * @param fieldName the name of field to check whether an item should be kept
     * @return the filtered stream.
     */
    @PSTransformation()
    public PStream keepChanges(String fieldName) {
        return this.transform(Filters.keepChanges(fieldName));
    }

    /**
     * Sample the items based on a given interval. The items sent within the time interval
     * since last item are dropped.
     * Eg. If a stream has items sent at 1ms, 3ms, 7ms, 11ms and 40ms,
     * `sampleByInterval(10)` will only keep the items sent at 1ms, 11ms and 40ms.
     *
     * @param minInterval the minimum interval (in milliseconds) between each two items.
     * @return the filtered stream.
     */
    @PSTransformation()
    public PStream sampleByInterval(long minInterval) {
        return this.transform(Filters.sampleByInterval(minInterval));
    }

    /**
     * Sample the items based on a given step count. The items are filtered to make sure
     * `stepCount` number of items are dropped between each two new items.
     * Eg. `sampleByCount(2)` will keep the 1st, 4th, 7th, 10th, ... items
     *
     * @param stepCount the num of items to drop since last item
     * @return the filtered stream
     */
    @PSTransformation()
    public PStream sampleByCount(int stepCount) {
        return this.transform(Filters.sampleByCount(stepCount));
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
     * @param itemChecker the function to check each item.
     * @return The limited stream.
     */
    @PSTransformation()
    public PStream limit(Function<Item, Boolean> itemChecker) {
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
    public PStream limit(int maxCount) {
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
    public PStream timeout(long timeoutMilliseconds) {
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
    public PStream map(Function<Item, Item> itemConverter) {
        return this.transform(Mappers.mapEachItem(itemConverter));
    }

    /**
     * Make the items be sent in a fixed interval.
     * Eg. If a stream has items sent at 1ms, 3ms, 7ms, 11ms and 40ms,
     * `inFixedInterval(10)` will send items at 7ms, 11ms, 11ms and 40ms, in a 10ms interval.
     *
     * @param fixedInterval the fixed interval in milliseconds.
     * @return The stream with items after mapping
     */
    @PSTransformation()
    public PStream inFixedInterval(long fixedInterval) {
        return this.transform(Mappers.inFixedInterval(fixedInterval));
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
    public PStream project(String... fieldsToInclude) {
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
    public <TValue> PStream setField(String fieldToSet, Function<Item, TValue> fieldValueComputer) {
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
    public <TValue> PStream setGroupField(String fieldToSet, Function<List<Item>, TValue> fieldValueComputer) {
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
    public <TValue> PStream setIndependentField(String fieldToSet, Function<Void, TValue> valueGenerator) {
        return this.map(ItemOperators.setIndependentField(fieldToSet, valueGenerator));
    }

    // *****************************
    // Mergers
    // Mergers are used to merge the items in the stream and the items in another stream

    /**
     * Union the items in current stream with another stream.
     *
     * @param anotherStreamProvider     the provider of another stream
     * @return The merged stream.
     */
    @PSTransformation()
    public PStream union(PStreamProvider anotherStreamProvider) {
        return this.transform(Mergers.union(anotherStreamProvider));
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
    public PStream sortBy(String fieldName) {
        return this.transform(Reorders.sortBy(fieldName));
    }

    /**
     * Shuffle the items.
     * `shuffle()` will randomize the order of the items in the stream.
     *
     * @return The stream with shuffled items.
     */
    @PSTransformation(changeOrder = true)
    public PStream shuffle() {
        return this.transform(Reorders.shuffle());
    }

    /**
     * Reverse the order of items
     * `reverse()` will reverse the order of the items in the stream.
     *
     * @return The stream with reversed items.
     */
    @PSTransformation(changeOrder = true)
    public PStream reverse() {
        return this.transform(Reorders.reverse());
    }

    // *****************************
    // StreamGrouper
    // StreamGrouper are used to group some items to one item

    /**
     * Group the items according to a field.
     * After grouping, the items in the new stream will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg. `groupBy("x")` will group the items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param groupField the field used to group the items in current stream.
     * @return The grouped stream
     */
    @PSTransformation(changeOrder = true)
    public PStream groupBy(String groupField) {
        return this.transform(Groupers.groupBy(groupField));
    }

    /**
     * Group the **contiguous** items according to a field.
     * After grouping, the items in the new stream will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg.  `localGroupBy("x")` will group the contiguous items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param groupField the field used to group the items in current stream.
     * @return The grouped stream
     */
    @PSTransformation
    public PStream localGroupBy(String groupField) {
        return this.transform(Groupers.localGroupBy(groupField));
    }

    /**
     * Un-group a list field in each item to multiple items.
     * Each element in the list will be a new field in each item of the new stream.
     * After un-grouping, the items in the new streams will have the same amount of fields
     * as the original stream.
     * However, the list field (`unGroupField`) will be replaced by a new field (`newField`).
     * Eg.  `unGroup("emails", "email")` will un-group the "emails" field (which is a list)
     * in an item to several new items with a "email" field.
     *
     * @param unGroupField the field to un-group, whose value should be a list
     * @param newField the new field name in the new stream
     * @return The un-grouped stream
     */
    @PSTransformation
    public PStream unGroup(String unGroupField, String newField) {
        return this.transform(Groupers.unGroup(unGroupField, newField));
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
     * @return the first item of current PStream
     */
    @PSAction(blocking = true)
    public Item getFirst() throws PSException {
        return this.getItemAt(0);
    }

    /**
     * Get the first value of the given field in the stream.
     *
     * @return the first item of current PStream
     */
    @PSAction(blocking = true)
    public <TValue> TValue getFirst(String fieldName) throws PSException {
        return this.getFieldAt(fieldName, 0);
    }

    /**
     * Pick the N-th item in the stream. N is the index.
     *
     * @param index the index of target item.
     * @return the item selected from the current PStream
     */
    @PSAction(blocking = true)
    public Item getItemAt(int index) throws PSException {
        List<Item> items = this.limit(index + 1).asList();
        if (items != null && items.size() > index) {
            return items.get(index);
        }
        return null;
    }

    /**
     * Get the N-th value of a given field. N is the index.
     *
     * @param fieldName the name of the field to select
     * @param index the index of target item.
     * @return the item selected from the current PStream
     */
    @PSAction(blocking = true)
    public <TValue> TValue getFieldAt(String fieldName, int index) throws PSException {
        List<TValue> fieldValues = this.limit(index + 1).asList(fieldName);
        if (fieldValues != null && fieldValues.size() > index) {
            return fieldValues.get(index);
        }
        return null;
    }

    /**
     * Do nothing with the items.
     */
    @PSAction(blocking = false)
    public void idle() {
        this.forEach(new IdleFunction<Item, Void>());
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
    public PStream logAs(String logTag) {
        return this.map(DebugOperators.<Item>logAs(logTag));
    }

    /**
     * Print the items in current stream over socket.
     *
     * @param logTag the log tag to use in printing current stream
     */
    @PSTransformation
    public PStream logOverSocket(String logTag) {
        return this.map(DebugOperators.<Item>logOverSocket(logTag));
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
        return this.output(ItemsOperators.asFieldList());
    }

    /**
     * Select a field in each item and output the field values to a list.
     *
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    @PSAction(blocking = true)
    public <TValue> List<TValue> asList(String fieldToSelect) throws PSException {
        return this.output(ItemsOperators.<TValue>asFieldList(fieldToSelect));
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
    public <TValue> void forEach(String fieldToSelect, Function<TValue, Void> callback) {
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
    public <TValue> void onChange(String fieldToSelect, Function<TValue, Void> callback) {
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
    public <TValue> void ifPresent(String fieldToSelect, Function<TValue, Void> callback) {
        this.output(Callbacks.ifFieldPresent(fieldToSelect, callback));
    }

    /**
     * Reuse current stream.
     *
     * @param numOfReuses number of reuses
     * @return the stream ready for reuse
     */
    @PSTransformation
    public PStream reuse(int numOfReuses) {
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
    public <Tout> Function<Void, Tout> getValueGenerator(Function<PStream, Tout> streamOutputter) {
        return this.streamProvider.compound(streamOutputter);
    }

}
