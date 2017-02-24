package com.github.privacystreams.core;

import java.util.Map;

import com.github.privacystreams.core.actions.SingleItemStreamAction;
import com.github.privacystreams.core.utilities.common.ItemCommons;
import com.github.privacystreams.core.utilities.print.Printers;
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
     * @param <Tout>           the type of output
     * @return the output
     */
    public <Tout> Tout output(Function<SingleItemStream, Tout> sStreamAction) {
        return this.getUQI().evaluate(this.getStreamProvider(), sStreamAction);
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
    public <T> T outputItem(Function<Item, T> itemCollector) {
        return new SingleItemStreamAction<>(itemCollector).apply(this.getUQI(), this);
    }

    /**
     * get the value of a field
     * @param field the name of the field to get
     * @param <TValue> the type of the new field value
     * @return the field value
     */
    public <TValue> TValue getField(String field) {
        return this.outputItem(ItemCommons.<TValue>getField(field));
    }

    /**
     * Check whether the item satisfies a predicate
     * @param itemPredicate the predicate to check
     * @return true if the predicate is satisfied.
     */
    public boolean check(Function<Item, Boolean> itemPredicate) {
        return this.outputItem(itemPredicate);
    }

    /**
     * evaluate a function on the item an return the result value
     * @param functionToComputeValue the function to compute the value
     * @param <TValue> the type of the value
     * @return the value
     */
    public <TValue> TValue compute(Function<Item, TValue> functionToComputeValue) {
        return this.outputItem(functionToComputeValue);
    }

    /**
     * Output the item by returning the key-value map.
     * The keys in the map can be selected using project(String... fieldsToInclude) method.
     */
    public Map<String, Object> getMap() {
        return this.outputItem(ItemCommons.asMap());
    }

    /**
     * Print the item
     */
    public void print() {
        this.outputItem(Printers.print());
    }

    /**
     * Debug print the item
     */
    public void debug() {
        this.outputItem(Printers.debug());
    }
}
