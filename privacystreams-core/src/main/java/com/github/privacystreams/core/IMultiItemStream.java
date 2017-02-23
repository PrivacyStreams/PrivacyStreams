package com.github.privacystreams.core;

import java.util.List;

/**
 * Created by yuanchun on 28/11/2016.
 * The interface of MultiItemStream.
 * MultiItemStream is one of the essential classes used in PrivacyStreams.
 * Most personal data access/process operation in PrivacyStreams use MultiItemStream as the intermediate.
 *
 * A MultiItemStream is consist of a list of items.
 * The items are produced by MultiItemStreamProvider functions (like LocationUpdatesProvider, CallLogProvider, etc.),
 * transformed by M2MTransformation functions (like filter, reorder, map, etc.),
 * and outputted by ItemsFunction functions (like print, toList, etc.).
 */

public interface IMultiItemStream {

    /**
     * Check whether the stream is closed,
     * MultiItemStream generator functions should make sure the stream is not closed this writing items to it.
     * @return true if the stream is closed, meaning the stream does not accept new items
     */
    boolean isClosed();

    /**
     * Close the stream
     * By closing the stream, it does not accept new items from the MultiItemStreamProvider any more.
     */
    void close();

    /**
     * Transform the stream to another stream
     * @param mStreamTransformation the function used to transform current stream
     * @return the transformed stream
     */
    IMultiItemStream transform(LazyFunction<MultiItemStream, MultiItemStream> mStreamTransformation);

    /**
     * Collect an item in the stream, the item can be further processed using item functions
     * @param m2sStreamTransformation the function used to convert the current stream to an item
     * @return the collected item
     */
    ISingleItemStream transformToItem(LazyFunction<MultiItemStream, SingleItemStream> m2sStreamTransformation);

    /**
     * Collect the items in the stream for output
     * @param mStreamAction the function used to output current stream
     * @return the collected object
     */
    <Tout> Tout output(Function<MultiItemStream, Tout> mStreamAction);

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
    IMultiItemStream filter(Function<Item, Boolean> predicate);

    /**
     * filter the stream by checking whether a field equals a value,
     * keep the items in which the field equals the given value.
     * eg. filter("x", 10) will keep the items whose x field is equal to 10
     *
     * @param fieldName     the name of field to compare
     * @param fieldValue    the value to compare with the field
     * @return The filtered stream.
     */
    <TValue> IMultiItemStream filter(String fieldName, TValue fieldValue);

    // *****************************
    // Limiters
    // Limiters are used to limit the length of a stream.
    /**
     * limit the stream by testing a field with a predicate,
     * keep the stream as long as the predicate holds.
     * eg. limit(eq("x", 100)) will keep all items in the stream as long as x field equals to 100,
     * once an item's x value is not equal to 100, the stream stops.
     *
     * @param predicate     the predicate to test the field
     * @return The limited stream.
     */
    IMultiItemStream limit(Function<Item, Boolean> predicate);

    /**
     * limit the stream by checking the count of items,
     * stop the stream if the count of items exceeds the threshold.
     * eg. limit(10) will limit the stream to at most 10 items
     *
     * @param maxCount      the threshold of count
     * @return The limited stream.
     */
    IMultiItemStream limit(int maxCount);

    /**
     * limit the stream with a timeout,
     * stop the stream after some time.
     * eg. timeout(Duration.seconds(10)) will limit the stream to at most 10 seconds
     *
     * @param timeoutMilliseconds      the timeout millis seconds
     * @return The limited stream.
     */
    IMultiItemStream timeout(long timeoutMilliseconds);

    // *****************************
    // Mappers
    // Mappers are used to map each item in the stream to another item

    /**
     * Convert an item with a function.
     * eg. map(Photos.blur("image")) will blur the "image" field of each item in the stream
     *
     * @param function      the function to convert the item
     * @return The stream with items after mapping
     */
    IMultiItemStream map(Function<Item, Item> function);

    /**
     * Project each item by including some fields.
     * Other fields will not appear in collectors, such as list(), foreach(Callback ...).
     * eg. project("name", "email") will only keep the "name" and "email" field in each item
     *
     * @param fieldsToInclude the fields to include
     * @return The stream with items after projection
     */
    IMultiItemStream project(String... fieldsToInclude);

    /**
     * add a field to each item in the stream with a function
     * @param newField the new field name
     * @param functionToComputeValue the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    <TValue> IMultiItemStream setField(String newField, Function<Item, TValue> functionToComputeValue);

    /**
     * add a field to each item in the grouped stream with a function
     * this method must be invoked after invoking group method
     * @param newField the new field name
     * @param subStreamFunction the function to compute the new field value,
     *                          the function will be applied to the sub stream of the item
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    <TValue> IMultiItemStream setGroupField(String newField, Function<List<Item>, TValue> subStreamFunction);

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
    IMultiItemStream sortBy(String fieldName);
    
    /**
     * Shuffle the items
     * shuffle() will change the order of the items in the stream
     *
     * @return The shuffled stream
     */
    IMultiItemStream shuffle();

    /**
     * Reverse the order of items
     * reverse() will reverse the order of the items in the stream
     *
     * @return The reversed stream
     */
    IMultiItemStream reverse();

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
    IMultiItemStream groupBy(String fieldName);
    
    /**
     * Group the contiguous items according to a field.
     * eg. localGroupBy("x") will group the contiguous items with same "x" field
     *
     * @param fieldName     the field used to reorder the stream
     * @return The grouped stream
     */
    IMultiItemStream localGroupBy(String fieldName);

    // *****************************
    // Output functions
    // Output functions are used to output the items in a stream

    /**
     * Collect the items in the stream for output
     * @param itemsOutput the function used to output current stream
     * @return the collected object
     */
    <Tout> Tout outputItems(Function<List<Item>, Tout> itemsOutput);

    /**
     * Get the first item in the stream.
     * @return the first item in the stream
     */
    ISingleItemStream first();
    
    /**
     * Pick an item in the stream.
     * @param index the index of target item
     * @return the item with the given index in the stream
     */
    ISingleItemStream pick(int index);

    /**
     * Print the items
     */
    void print();

    /**
     * Debug print the items
     */
    void debug();

    /**
     * Calculate the count of items
     * @return the count of number of items in the stream
     */
    int count();

    /**
     * Collect each item in this stream to a list
     * Each item in the list is a key-value map
     * @return a list of key-value maps, each map represents an item
     */
    List<Item> asList();

    /**
     * Select a field in each item and output the items to a list
     * @param fieldToSelect the field to select
     * @param <TValue> the type of field value
     * @return a list of field values
     */
    <TValue> List<TValue> asList(String fieldToSelect);

    /**
     * callback with each item
     * @param callback the callback to invoke for each item
     */
    void forEach(Function<Item, Void> callback);

    /**
     * callback with a certain field of each item
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for each item field
     * @param <TValue> the type of the field
     */
    <TValue> void forEach(String fieldToSelect, Callback<TValue> callback);

    /**
     * callback with an item map when the item changes
     * @param callback the callback to invoke for the changed item
     */
    void onChange(Function<Item, Void> callback);

    /**
     * callback with a field value of an item when the field value changes
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke for the changed item field
     * @param <TValue> the type of the field
     */
    <TValue> void onChange(String fieldToSelect, Callback<TValue> callback);

    /**
     * callback with an item map once one item is present
     * @param callback the callback to invoke once the item is present
     */
    void ifPresent(Function<Item, Void> callback);

    /**
     * callback with a field value of an item once the field value is present
     * @param fieldToSelect the name of the field to callback with
     * @param callback the callback to invoke once the field value is present
     * @param <TValue> the type of the field
     */
    <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback);
}
