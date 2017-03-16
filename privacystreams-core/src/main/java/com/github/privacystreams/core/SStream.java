package com.github.privacystreams.core;

import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.actions.SStreamAction;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.transformations.map.Mappers;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An SStream is the basic element in a stream.
 * An SStream can be directly produced by a provider (e.g. CurrentLocationProvider)
 * or from a stream (e.g. stream.first()).
 * Similar to a MStream, an SStream could be transformed and collected with multiple functions.
 */
public class SStream extends Stream implements SStreamInterface {
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
     * Transform an item using a transformation function
     *
     * @param s2sStreamTransformation the function used to transform the current item
     * @return the transformed item
     */
    public SStream transform(Function<SStream, SStream> s2sStreamTransformation) {
        return new SStream(this.getUQI(), this.streamProvider.compound(s2sStreamTransformation));
    }

    /**
     * Collect the item for output
     *
     * @param sStreamAction the function used to output the current item
     */
    public void output(Function<SStream, Void> sStreamAction) {
        this.getUQI().setQuery(this.streamProvider.compound(sStreamAction));
        this.getUQI().evaluate(true);
    }

    /**
     * Convert the item with a function.
     * eg. map(Images.blur("image")) will blur the "image" field of the item
     *
     * @param function      the function to convert the item
     * @return The item after mapping
     */
    public SStream map(Function<Item, Item> function) {
        return this.transform(Mappers.mapItem(function));
    }

    /**
     * Project the item by including some fields.
     * Other fields will not appear in collectors, such as toMap().
     * eg. project("name", "email") will only keep the "name" and "email" field in the item
     *
     * @param fieldsToInclude the fields to include
     * @return The item after projection
     */
    public SStream project(String... fieldsToInclude) {
        return this.map(ItemOperators.includeFields(fieldsToInclude));
    }

    /**
     * set the value of a field with a function
     * @param newField the new field name
     * @param functionToComputeField the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the item with the new field set
     */
    public <TValue> SStream setField(String newField, Function<Item, TValue> functionToComputeField) {
        return this.map(ItemOperators.setField(newField, functionToComputeField));
    }

    public <Tout> void outputItem(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler) {
        this.output(new SStreamAction<>(itemOutputFunction, resultHandler));
    }

    public <Tout> Tout outputItem(Function<Item, Tout> itemOutputFunction) throws PrivacyStreamsException {
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        Function<Tout, Void> resultHandler = new Callback<Tout>() {
            @Override
            protected void onSuccess(Tout input) {
                resultQueue.add(input);
            }

            @Override
            protected void onFail(PrivacyStreamsException exception) {
                resultQueue.add(exception);
            }
        };
        this.outputItem(itemOutputFunction, resultHandler);
        try {
            Object resultOrException = resultQueue.take();
            if (resultOrException instanceof PrivacyStreamsException) {
                throw (PrivacyStreamsException) resultOrException;
            }
            return (Tout) resultOrException;
        } catch (InterruptedException e) {
            throw PrivacyStreamsException.INTERRUPTED(e.getMessage());
        }

    }

    /**
     * get the value of a field
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    public <TValue> TValue getField(String field) throws PrivacyStreamsException {
        return this.outputItem(ItemOperators.<TValue>getField(field));
    }

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     */
    public Map<String, Object> asMap() throws PrivacyStreamsException {
        return this.outputItem(ItemOperators.asMap());
    }

    /**
     * Debug print the item
     */
    public void debug() {
        this.outputItem(ItemOperators.debug(), null);
    }
}
