package com.github.privacystreams.core;

import com.github.privacystreams.commons.debug.DebugOperators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.actions.SStreamAction;
import com.github.privacystreams.core.actions.callback.Callbacks;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.transformations.S2MTransformation;
import com.github.privacystreams.core.transformations.S2STransformation;
import com.github.privacystreams.core.transformations.map.Mappers;
import com.github.privacystreams.utils.annotations.PSAction;
import com.github.privacystreams.utils.annotations.PSTransformation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The interface of SStream (single-item stream).
 * An SStream is a stream containing only one item, which is an instance of `Item`.
 * An SStream is produced by `uqi.getData` method.
 *
 * It can be transformed to another SStream with transformation functions,
 * such as `setField`, `project`, `map`, etc.
 *
 * Finally, it can be outputted using `asItem`, `getField`, etc.
 */
public class SStream extends Stream {
    private Function<Void, SStream> streamProvider;

    @Override
    public Function<Void, SStream> getStreamProvider() {
        return this.streamProvider;
    }

    public SStream(UQI uqi, Function<Void, SStream> streamProvider) {
        super(uqi);
        this.streamProvider = streamProvider;
    }

    /**
     * Transform current SStream to another SStream.
     *
     * @param s2sStreamTransformation the function used to transform the stream
     * @return the transformed stream
     */
    public SStream transform(S2STransformation s2sStreamTransformation) {
        return new SStream(this.getUQI(), this.streamProvider.compound(s2sStreamTransformation));
    }

    /**
     * Transform current SStream to a MStream.
     *
     * @param s2mStreamTransformation the function used to transform the stream
     * @return the transformed stream
     */
    public MStream transform(S2MTransformation s2mStreamTransformation) {
        return new MStream(this.getUQI(), this.streamProvider.compound(s2mStreamTransformation));
    }

    /**
     * Output the item in the current stream.
     *
     * @param sStreamAction the function used to output the current item
     */
    public void output(SStreamAction sStreamAction) {
        this.getUQI().evaluate(this.streamProvider.compound(sStreamAction), true);
    }

    /**
     * Convert the item in the stream with a function.
     * Eg. `map(ImageOperators.blur("image"))` will blur the image specified by "image" field in the item.
     *
     * @param function      the function to convert the item
     * @return The item after mapping
     */
    @PSTransformation
    public SStream map(Function<Item, Item> function) {
        return this.transform(Mappers.mapItem(function));
    }

    /**
     * Project the item by including some fields.
     * Other fields will not appear in collectors, such as toMap().
     * eg. `project("name", "email")` will only keep the "name" and "email" field in the item
     *
     * @param fieldsToInclude the fields to include
     * @return The item after projection
     */
    @PSTransformation
    public SStream project(String... fieldsToInclude) {
        return this.map(ItemOperators.includeFields(fieldsToInclude));
    }

    /**
     * Set a field with a function that takes the item as input.
     *
     * @param newField the new field name
     * @param functionToComputeField the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the item with the new field set
     */
    @PSTransformation
    public <TValue> SStream setField(String newField, Function<Item, TValue> functionToComputeField) {
        return this.map(ItemOperators.setField(newField, functionToComputeField));
    }

    /**
     * Set the value of a new field with a value generator function.
     * The value generator function is independent from current item, which does not need a input (input type is Void).
     * The value generator will be evaluated on demand at runtime.
     * Eg. `setIndependentField("time", TimeOperators.getCurrentTime())` will set the field "time" to a timestamp in each item;
     * `setIndependentField("wifiStatus", DeviceOperators.isWifiConnected())` will set the field "wifiStatus" to a boolean indicating whether wifi is connected in each item.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param valueGenerator the function to compute the field value.
     * @param <TValue> the type of the new field value.
     * @return the stream of items with the new field set
     */
    @PSTransformation
    public <TValue> SStream setIndependentField(String fieldToSet, Function<Void, TValue> valueGenerator) {
        return this.map(ItemOperators.setIndependentField(fieldToSet, valueGenerator));
    }

    /**
     * Output the item in the stream with a function, and the result is delivered to a callback function.
     * This method will NOT block.
     *
     * @param itemCollector the function used to output the current item
     * @param resultHandler the function to handle the result
     * @param <Tout>           the type of result
     */
    @PSAction(blocking = false)
    public <Tout> void output(Function<Item, Tout> itemCollector, Callback<Tout> resultHandler) {
        this.output(Collectors.collectItem(itemCollector, resultHandler));
    }

    /**
     * Output the item in the stream with a function.
     * This method will block until the result returns.
     *
     * @param itemCollector the function used to output the current item
     * @param <Tout>           the type of result
     * @return the result of itemCollector
     * @throws PSException if something goes wrong during getting results.
     */
    @PSAction(blocking = true)
    private <Tout> Tout output(Function<Item, Tout> itemCollector) throws PSException {
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
        this.output(itemCollector, resultHandler);
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
     * Get the value of a field.
     *
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    @PSAction(blocking = true)
    public <TValue> TValue getField(String field) throws PSException {
        return this.output(ItemOperators.<TValue>getField(field));
    }

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     *
     * @return the key-value map of the item
     */
    @PSAction(blocking = true)
    public Item asItem() throws PSException {
        return this.output(ItemOperators.asItem());
    }

    /**
     * Print this stream for debugging.
     */
    @PSAction(blocking = false)
    public void debug() {
        this.ifPresent(DebugOperators.<Item>debug());
    }

    /**
     * Print the items in current stream.
     *
     * @param logTag the log tag to use in printing current stream
     */
    @PSTransformation
    public SStream logAs(String logTag) {
        return this.map(DebugOperators.<Item>logAs(logTag));
    }

    /**
     * Reuse current stream.
     *
     * @param numOfReuses number of reuses
     * @return the stream ready for reuse
     */
    @PSTransformation
    public SStream reuse(int numOfReuses) {
        this.getUQI().reuse(this, numOfReuses);
        return this;
    }

    /**
     * Get a value generator that can be evaluated on demand.
     * The function will not be evaluated immediately, instead, it will be evaluated once `apply()` is called.
     *
     * @return the function
     */
    public <Tout> Function<Void, Tout> getValueGenerator(Function<SStream, Tout> streamOutputFunction) {
        return this.streamProvider.compound(streamOutputFunction);
    }

    /**
     * Once an item is present, callback with the item.
     *
     * @param callback the callback to invoke with the item.
     */
    @PSAction(blocking = false)
    public void ifPresent(Function<Item, Void> callback) {
        this.output(Callbacks.ifPresent2(callback));
    }

    /**
     * Once an item is present and the field is valid, Callback with the field value.
     *
     * @param fieldToSelect the name of the field.
     * @param callback the callback to invoke with the field valu.
     * @param <TValue> the type of the field.
     */
    @PSAction(blocking = false)
    public <TValue> void ifPresent(String fieldToSelect, Callback<TValue> callback) {
        this.output(Callbacks.ifFieldPresent2(fieldToSelect, callback));
    }
}
