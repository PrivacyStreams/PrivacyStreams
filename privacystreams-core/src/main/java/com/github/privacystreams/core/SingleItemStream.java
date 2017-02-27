package com.github.privacystreams.core;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.privacystreams.core.actions.SingleItemStreamAction;
import com.github.privacystreams.core.exceptions.PipelineInterruptedException;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.commons.common.ItemCommons;
import com.github.privacystreams.core.commons.print.Printers;
import com.github.privacystreams.core.transformations.map.Mappers;

/**
 * Created by yuanchun on 29/11/2016.
 * An SingleItemStream is the basic element in a stream.
 * An SingleItemStream can be directly produced by a provider (e.g. CurrentLocationProvider)
 * or from a stream (e.g. stream.first()).
 * Similar to a MultiItemStream, an SingleItemStream could be transformed and collected with multiple functions.
 */
public class SingleItemStream extends Stream implements ISingleItemStream {
    private LazyFunction<Void, SingleItemStream> streamProvider;

    @Override
    public LazyFunction<Void, SingleItemStream> getStreamProvider() {
        return this.streamProvider;
    }

    @Override
    public void write(Item item) {
        super.write(item);
        super.write(null);
    }

    public SingleItemStream(LazyFunction<Void, SingleItemStream> streamProvider, UQI uqi) {
        super(uqi);
        this.streamProvider = streamProvider;
    }

    /**
     * Transform an item using a transformation function
     *
     * @param sStreamTransformation the function used to transform the current item
     * @return the transformed item
     */
    public ISingleItemStream transform(LazyFunction<SingleItemStream, SingleItemStream> sStreamTransformation) {
        return sStreamTransformation.apply(this.getUQI(), this);
    }

    /**
     * Collect the item for output
     *
     * @param sStreamAction the function used to output the current item
     */
    public void output(Function<SingleItemStream, Void> sStreamAction) {
        this.getUQI().setQuery(this.getStreamProvider().compound(sStreamAction));
        this.getUQI().evaluate(true);
    }

    /**
     * Convert the item with a function.
     * eg. map(Images.blur("image")) will blur the "image" field of the item
     *
     * @param function      the function to convert the item
     * @return The item after mapping
     */
    public ISingleItemStream map(Function<Item, Item> function) {
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
    public ISingleItemStream project(String... fieldsToInclude) {
        return this.map(ItemCommons.includeFields(fieldsToInclude));
    }

    /**
     * set the value of a field with a function
     * @param newField the new field name
     * @param functionToComputeField the function to compute the new field value
     * @param <TValue> the type of the new field value
     * @return the item with the new field set
     */
    public <TValue> ISingleItemStream setField(String newField, Function<Item, TValue> functionToComputeField) {
        return this.map(ItemCommons.setField(newField, functionToComputeField));
    }

    @Override
    public <Tout> void outputItem(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler) {
        this.output(new SingleItemStreamAction<>(itemOutputFunction, resultHandler));
    }

    @Override
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
            throw new PipelineInterruptedException();
        }

    }

    /**
     * get the value of a field
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    public <TValue> TValue getField(String field) throws PrivacyStreamsException {
        return this.outputItem(ItemCommons.<TValue>getField(field));
    }

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     */
    public Map<String, Object> asMap() throws PrivacyStreamsException {
        return this.outputItem(ItemCommons.asMap());
    }

    /**
     * Print the item
     */
    public void print() {
        this.outputItem(Printers.print(), null);
    }

    /**
     * Debug print the item
     */
    public void debug() {
        this.outputItem(Printers.debug(), null);
    }
}
