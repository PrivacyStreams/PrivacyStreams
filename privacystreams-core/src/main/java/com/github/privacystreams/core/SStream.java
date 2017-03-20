package com.github.privacystreams.core;

import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.actions.SStreamAction;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.transformations.S2MTransformation;
import com.github.privacystreams.core.transformations.S2STransformation;
import com.github.privacystreams.core.transformations.map.Mappers;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The interface of SStream (single-item stream).
 * An SStreamInterface is a stream containing only one item, which is an instance of {@link Item}.
 *
 * An SStreamInterface is produced by <code>uqi.getData</code> method.
 * @see UQI#getData(com.github.privacystreams.core.providers.SStreamProvider, Purpose)
 *
 * It can be transformed to another ISingleItemProvider with transformation functions,
 * such as {@link #setField(String, Function)}, {{@link #project(String...)}}, {{@link #map(Function)}}, etc.
 *
 * Finally, it can be outputted using {{@link #asMap()}}, {{@link #getField(String)}}, etc.
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
        this.getUQI().setQuery(this.streamProvider.compound(sStreamAction));
        this.getUQI().evaluate(true);
    }

    /**
     * Convert the item in the stream with a function.
     * Eg. <code>map(ImageOperators.blur("image"))</code> will blur the image specified by "image" field in the item.
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
     * eg. <code>project("name", "email")</code> will only keep the "name" and "email" field in the item
     *
     * @param fieldsToInclude the fields to include
     * @return The item after projection
     */
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
    public <TValue> SStream setField(String newField, Function<Item, TValue> functionToComputeField) {
        return this.map(ItemOperators.setField(newField, functionToComputeField));
    }

    /**
     * Output the item in the stream with a function, and the result is delivered to a callback function.
     * This method will NOT block.
     *
     * @param itemCollector the function used to output the current item
     * @param resultHandler the function to handle the result
     * @param <Tout>           the type of result
     */
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
     * @throws PrivacyStreamsException if something goes wrong during getting results.
     */
    public <Tout> Tout output(Function<Item, Tout> itemCollector) throws PrivacyStreamsException {
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        Callback<Tout> resultHandler = new Callback<Tout>() {
            @Override
            protected void onSuccess(Tout input) {
                resultQueue.add(input);
            }

            @Override
            protected void onFail(PrivacyStreamsException exception) {
                resultQueue.add(exception);
            }
        };
        this.output(itemCollector, resultHandler);
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
     * Get the value of a field.
     *
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    public <TValue> TValue getField(String field) throws PrivacyStreamsException {
        return this.output(ItemOperators.<TValue>getField(field));
    }

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     *
     * @return the key-value map of the item
     */
    public Map<String, Object> asMap() throws PrivacyStreamsException {
        return this.output(ItemOperators.asMap());
    }

    /**
     * Print this stream for debugging.
     */
    public void debug() {
        this.output(ItemOperators.debug(), null);
    }

    /**
     * Fork current stream for reusing.
     *
     * @param numOfForks number of reuses
     * @return the forked stream
     */
    public SStream fork(int numOfForks) {
        return (SStream) super.fork(numOfForks);
    }
}
