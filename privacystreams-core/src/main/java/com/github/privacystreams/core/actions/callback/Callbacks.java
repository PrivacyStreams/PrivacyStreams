package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;

/**
 * A helper class to access callback-related functions
 */
public class Callbacks {

    /**
     * A function that callbacks with each item in the stream.
     * The callback will be invoked with each item as a parameter.
     * @param itemCallback the callback to be invoked for each item
     * @return the function
     */
    public static Function<MultiItemStream, Void> forEach(Function<Item, Void> itemCallback) {
        return new ForEachCallback(itemCallback);
    }

    /**
     * A function that callbacks with the field value for each item in the stream.
     * The callback will be invoked with each field value.
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked for each field value
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MultiItemStream, Void> forEachField(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new ForEachFieldCallback<>(fieldToSelect, fieldValueCallback);
    }

    /**
     * A function that callbacks with the first item in the stream.
     * If there is no present item, the callback will not be invoked.
     * The callback will be invoked with the present item as a parameter.
     * @param itemCallback the callback to be invoked with the first item.
     * @return the function
     */
    public static Function<MultiItemStream, Void> ifPresent(Function<Item, Void> itemCallback) {
        return new IfPresentCallback(itemCallback);
    }

    /**
     * A function that callbacks with the first present field value in the stream.
     * If there is no item with a present field value, the callback will not be invoked.
     * The callback will be invoked with the present field value.
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked with the first field value
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MultiItemStream, Void> ifFieldPresent(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new IfFieldPresentCallback<>(fieldToSelect, fieldValueCallback);
    }

    /**
     * A function that callbacks with the changed items in the stream.
     * A changed item is the item that is different from the former item.
     * The callback will be invoked with the item as a parameter.
     * @param itemCallback the callback to be invoked with the changed item.
     * @return the function
     */
    public static Function<MultiItemStream, Void> onChange(Function<Item, Void> itemCallback) {
        return new OnChangeCallback(itemCallback);
    }

    /**
     * A function that callbacks with the changed field value in the stream.
     * A changed field value is the field value that is different from the former field value.
     * The callback will be invoked with the field value.
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked with the changed field value.
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MultiItemStream, Void> onFieldChange(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new OnFieldChangeCallback<>(fieldToSelect, fieldValueCallback);
    }
}
