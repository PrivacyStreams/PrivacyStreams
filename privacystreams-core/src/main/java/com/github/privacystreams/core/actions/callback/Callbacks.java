package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access callback-related functions
 */
@PSOperatorWrapper
public class Callbacks {

    /**
     * Callback with each item in the stream.
     * The callback will be invoked with each item as a parameter.
     *
     * @param itemCallback the callback to be invoked for each item
     * @return the function
     */
    public static Function<MStream, Void> forEach(Function<Item, Void> itemCallback) {
        return new ForEachCallback(itemCallback);
    }

    /**
     * Callback with the field value for each item in the stream.
     * The callback will be invoked with each field value.
     *
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked for each field value
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MStream, Void> forEachField(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new ForEachFieldCallback<>(fieldToSelect, fieldValueCallback);
    }

    /**
     * Callback with the first item in the stream.
     * If there is no present item, the callback will not be invoked.
     * The callback will be invoked with the present item as a parameter.
     *
     * @param itemCallback the callback to be invoked with the first item.
     * @return the function
     */
    public static Function<MStream, Void> ifPresent(Function<Item, Void> itemCallback) {
        return new IfPresentCallback(itemCallback);
    }

    /**
     * Callback with the first present field value in the stream.
     * If there is no item with a present field value, the callback will not be invoked.
     * The callback will be invoked with the present field value.
     *
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked with the first field value
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MStream, Void> ifFieldPresent(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new IfFieldPresentCallback<>(fieldToSelect, fieldValueCallback);
    }

    /**
     * Callback with the changed items in the stream.
     * A changed item is the item that is different from the former item.
     * The callback will be invoked with the item as a parameter.
     *
     * @param itemCallback the callback to be invoked with the changed item.
     * @return the function
     */
    public static Function<MStream, Void> onChange(Function<Item, Void> itemCallback) {
        return new OnChangeCallback(itemCallback);
    }

    /**
     * Callback with the changed field value of a given field in the stream.
     * A changed field value is the field value that is different from the former field value.
     * The callback will be invoked with the field value as a parameter.
     *
     * @param fieldToSelect the field to select
     * @param fieldValueCallback the callback function to be invoked with the changed field value.
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<MStream, Void> onFieldChange(String fieldToSelect, Function<TValue, Void> fieldValueCallback) {
        return new OnFieldChangeCallback<>(fieldToSelect, fieldValueCallback);
    }
}
