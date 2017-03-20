package com.github.privacystreams.core;

import com.github.privacystreams.core.actions.MStreamAction;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.core.transformations.M2STransformation;

import java.util.List;

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
public interface MStreamInterface {
    /**
     * Transform the current MStream to another MStream.
     * @param m2mStreamTransformation the function used to transform the stream
     * @return the transformed stream
     */
    MStreamInterface transform(M2MTransformation m2mStreamTransformation);

    /**
     * Transform the current MStream to an SStream.
     * @param m2sStreamTransformation the function used to transform the stream
     * @return the collected item
     */
    SStreamInterface transform(M2STransformation m2sStreamTransformation);

    /**
     * Output the current multi-item stream.
     * @param mStreamAction the function used to output stream
     */
    void output(MStreamAction mStreamAction);

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
    MStreamInterface filter(Function<Item, Boolean> predicate);

    /**
     * Filter the stream by checking whether a field equals a value.
     * Specifically, keep the items in which the field equals the given value.
     * Eg. <code>filter("x", 100)</code> will keep the items whose x field is equal to 100.
     *
     * @param fieldName     the name of field to compare
     * @param fieldValue    the value to compare with the field
     * @return The filtered stream.
     */
    <TValue> MStreamInterface filter(String fieldName, TValue fieldValue);

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
    MStreamInterface limit(Function<Item, Boolean> predicate);

    /**
     * Limit the stream with a max number of items.
     * Specifically, stop the stream if the count of items exceeds the threshold.
     * Eg. <code>limit(10)</code> will limit the stream to at most 10 items
     *
     * @param maxCount      the max number of items
     * @return The limited stream.
     */
    MStreamInterface limit(int maxCount);

    /**
     * Limit the stream with a timeout,
     * stop the stream after some time.
     * Eg. <code>timeout(Duration.seconds(10))</code> will limit the stream to at most 10 seconds
     *
     * @param timeoutMilliseconds      the timeout millis seconds
     * @return The limited stream.
     */
    MStreamInterface timeout(long timeoutMilliseconds);

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
    MStreamInterface map(Function<Item, Item> function);

    /**
     * Project each item by including some fields.
     * Other fields will be removed.
     * Eg. <code>project("name", "email")</code> will only keep the "name" and "email" field in each item.
     *
     * @param fieldsToInclude the fields to include
     * @return The stream with items after projection
     */
    MStreamInterface project(String... fieldsToInclude);

    /**
     * Set a field to a new value for each item in the stream.
     * The value is computed with a function that take the item as input.
     * Eg. <code>setField("x", Comparators.gt("y", 10))</code> will set a new boolean field "x" to each item,
     * which indicates whether the "y" field is greater than 10.
     *
     * @param newField the new field name
     * @param functionToComputeValue the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    <TValue> MStreamInterface setField(String newField, Function<Item, TValue> functionToComputeValue);

    /**
     * Set a field to a new value for each item in the stream.
     * This transformation can only be used after invoking group methods ({@link #groupBy(String)}, {@link #localGroupBy(String)}).
     * The value is computed with a function that takes the grouped items as input.
     * Eg. <code>setGroupField("count", Statistic.count())</code> will set a new field "count" to each item,
     * which represents the number of items in the grouped sub stream.
     *
     * @param newField the new field name
     * @param subStreamFunction the function to compute the new field value, which takes the grouped items as input.
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    <TValue> MStreamInterface setGroupField(String newField, Function<List<Item>, TValue> subStreamFunction);

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
    MStreamInterface sortBy(String fieldName);
    
    /**
     * Shuffle the items.
     * <code>shuffle()</code> will randomize the order of the items in the stream.
     *
     * @return The shuffled stream
     */
    MStreamInterface shuffle();

    /**
     * Reverse the order of items
     * <code>reverse()</code> will reverse the order of the items in the stream.
     *
     * @return The reversed stream
     */
    MStreamInterface reverse();

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
    MStreamInterface groupBy(String fieldName);
    
    /**
     * Group the contiguous items according to a field.
     * eg. <code>localGroupBy("x")</code> will group the contiguous items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    MStreamInterface localGroupBy(String fieldName);

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
    <Tout> void output(Function<List<Item>, Tout> itemsCollector, Callback<Tout> resultHandler);

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
    <Tout> Tout output(Function<List<Item>, Tout> itemsCollector) throws PrivacyStreamsException;

    /**
     * Get the first item in the stream.
     *
     * @return SStream whose item is the first item of current MStream
     */
    SStreamInterface getFirst();
    
    /**
     * Pick an item in the stream.
     *
     * @param index the index of target item.
     * @return SStream whose item is selected from current MStream with the given index
     */
    SStreamInterface getItemAt(int index);

    /**
     * Select an item in the stream with a function.
     *
     * @param selector the selector funtion to select the target item.
     * @return SStream whose item is selected from current MStream with the given function
     */
    SStreamInterface select(Function<List<Item>, Item> selector);

    /**
     * Print the items for debugging.
     */
    void debug();

    /**
     * Count the number of items.
     *
     * @return the count of number of items in the stream.
     */
    int count() throws PrivacyStreamsException;

    /**
     * Collect the items in the stream to a list.
     * Each item in the list is an instance of {@link Item}.
     *
     * @return a list of key-value maps, each map represents an item
     */
    List<Item> asList() throws PrivacyStreamsException;

    /**
     * Select a field in each item and output the items to a list.
     *
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    <TValue> List<TValue> asList(String fieldToSelect) throws PrivacyStreamsException;

    /**
     * Callback with each item.
     *
     * @param callback the callback to invoke for each item.
     */
    void forEach(Function<Item, Void> callback);

    /**
     * Callback with a certain field of each item.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for each item field
     * @param <TValue> the type of the field
     */
    <TValue> void forEach(String fieldToSelect, Callback<TValue> callback);

    /**
     * Callback with an item when the item changes (is different from the previous item).
     *
     * @param callback the callback to invoke for the changed item
     */
    void onChange(Function<Item, Void> callback);

    /**
     * Callback with a field value of an item when the field value changes.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for the changed item field
     * @param <TValue> the type of the field
     */
    <TValue> void onChange(String fieldToSelect, Callback<TValue> callback);

    /**
     * Callback with an item once one item is present.
     *
     * @param callback the callback to invoke once the item is present
     */
    void ifPresent(Function<Item, Void> callback);

    /**
     * Callback with a field value of an item once the field value is present.
     *
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke once the field value is present
     * @param <TValue> the type of the field
     */
    <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback);

    /**
     * Fork current stream for reusing.
     * @param numOfForks number of reuses
     * @return the forked stream
     */
    MStreamInterface fork(int numOfForks);
}
