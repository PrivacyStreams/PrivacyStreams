package com.github.privacystreams.core;

import java.util.Map;

/**
 * Created by yuanchun on 29/11/2016.
 * The interface of SingleItemStream class.
 * An SingleItemStream is the basic element in a stream.
 * An SingleItemStream can be directly produced by a provider (e.g. CurrentLocationProvider)
 * or from a stream (e.g. stream.first()).
 * Similar to a MultiItemStream, an SingleItemStream could be transformed and collected with multiple functions.
 */
public interface ISingleItemStream {
    /**
     * Transform an item using a transformation function
     *
     * @param itemTransformation the function used to transform the current item
     * @return the transformed item
     */
    ISingleItemStream transform(LazyFunction<SingleItemStream, SingleItemStream> itemTransformation);

    /**
     * Collect the item for output
     *
     * @param itemAction the function used to output the current item
     * @param <T>           the type of output
     * @return the output
     */
    <T> T output(Function<SingleItemStream, T> itemAction);

    /**
     * Convert the item with a function.
     * eg. map(Images.blur("image")) will blur the "image" field of the item
     *
     * @param function      the function to convert the item
     * @return The item after mapping
     */
    ISingleItemStream map(Function<Item, Item> function);

    /**
     * Project the item by including some fields.
     * Other fields will not appear in collectors, such as toMap().
     * eg. project("name", "email") will only keep the "name" and "email" field in the item
     *
     * @param fieldsToInclude the fields to include
     * @return The item after projection
     */
    ISingleItemStream project(String... fieldsToInclude);

    /**
     * set the value of a field with a function
     * @param newField the new field name
     * @param functionToComputeField the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the item with the new field set
     */
    <TValue> ISingleItemStream setField(String newField, Function<Item, TValue> functionToComputeField);

    /**
     * Collect the item for output
     *
     * @param itemCollector the function used to output the current item
     * @param <T>           the type of output
     * @return the output
     */
    <T> T outputItem(Function<Item, T> itemCollector);

    /**
     * get the value of a field
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    <TValue> TValue getField(String field);

    /**
     * Check whether the item satisfies a predicate
     */
    boolean check(Function<Item, Boolean> itemPredicate);

    /**
     * evaluate a function on the item an return the result value
     * @param functionToComputeField the function to compute the value
     * @param <TValue> the type of the value
     * @return the value
     */
    <TValue> TValue compute(Function<Item, TValue> functionToComputeField);

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     */
    Map<String, Object> getMap();

    /**
     * Print the item
     */
    void print();

    /**
     * Debug print the item
     */
    void debug();

}
