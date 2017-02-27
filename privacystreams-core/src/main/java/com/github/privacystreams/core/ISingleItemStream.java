package com.github.privacystreams.core;

import com.github.privacystreams.core.exceptions.PrivacyStreamsException;

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
     * @param sStreamTransformation the function used to transform the current item
     * @return the transformed item
     */
    ISingleItemStream transform(Function<SingleItemStream, SingleItemStream> sStreamTransformation);

    /**
     * Collect the item for output
     *
     * @param sStreamAction the function used to output the current item
     */
    void output(Function<SingleItemStream, Void> sStreamAction);

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
     * @param itemOutputFunction the function used to output the current item
     * @param resultHandler the function to handle the result
     * @param <Tout>           the type of result
     */
    <Tout> void outputItem(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler);

    /**
     * Collect the item for output
     *
     * @param itemOutputFunction the function used to output the current item
     * @param <Tout>           the type of result
     * @return the result of itemOutputFunction
     */
    <Tout> Tout outputItem(Function<Item, Tout> itemOutputFunction) throws PrivacyStreamsException;

    /**
     * get the value of a field
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    <TValue> TValue getField(String field) throws PrivacyStreamsException;

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     */
    Map<String, Object> asMap() throws PrivacyStreamsException;

    /**
     * Print the item
     */
    void print();

    /**
     * Debug print the item
     */
    void debug();

}
