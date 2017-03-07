package com.github.privacystreams.core;

import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.Map;

/**
 * The interface of single-item stream.
 * An ISingleItemStream is a stream containing only one item, which is an instance of {@link Item}.
 *
 * An ISingleItemStream is produced by <code>uqi.getDataItem</code> method.
 * @see UQI#getDataItem(SingleItemStreamProvider, Purpose)
 *
 * It can be transformed to another ISingleItemProvider with transformation functions,
 * such as {@link #setField(String, Function)}, {{@link #project(String...)}}, {{@link #map(Function)}}, etc.
 *
 * Finally, it can be outputted using {{@link #asMap()}}, {{@link #getField(String)}}, etc.
 */
public interface ISingleItemStream {
    /**
     * Transform current single-item stream to another single-item stream a transformation function.
     *
     * @param s2sStreamTransformation the function used to transform the stream
     * @return the transformed item
     */
    ISingleItemStream transform(Function<SingleItemStream, SingleItemStream> s2sStreamTransformation);

    /**
     * Output the item in the current stream.
     *
     * @param sStreamAction the function used to output the current item
     */
    void output(Function<SingleItemStream, Void> sStreamAction);

    /**
     * Convert the item in the stream with a function.
     * Eg. map(Images.blur("image")) will blur the "image" field of the item.
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
     * Set a field with a function that takes the item as input.
     *
     * @param newField the new field name
     * @param functionToComputeField the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the item with the new field set
     */
    <TValue> ISingleItemStream setField(String newField, Function<Item, TValue> functionToComputeField);

    /**
     * Output the item in the stream with a function, and the result is delivered to a callback function.
     * This method will NOT block.
     *
     * @param itemOutputFunction the function used to output the current item
     * @param resultHandler the function to handle the result
     * @param <Tout>           the type of result
     */
    <Tout> void outputItem(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler);

    /**
     * Output the item in the stream with a function.
     * This method will block until the result returns.
     *
     * @param itemOutputFunction the function used to output the current item
     * @param <Tout>           the type of result
     * @return the result of itemOutputFunction
     * @throws PrivacyStreamsException if something goes wrong during getting results.
     */
    <Tout> Tout outputItem(Function<Item, Tout> itemOutputFunction) throws PrivacyStreamsException;

    /**
     * Get the value of a field.
     *
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    <TValue> TValue getField(String field) throws PrivacyStreamsException;

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     *
     * @return the key-value map of the item
     */
    Map<String, Object> asMap() throws PrivacyStreamsException;

    /**
     * Print the item.
     */
    void print();

    /**
     * Debug print the item.
     */
    void debug();

}
