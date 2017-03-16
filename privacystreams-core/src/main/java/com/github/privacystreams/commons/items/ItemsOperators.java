package com.github.privacystreams.commons.items;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access common stream functions
 */
@PSOperatorWrapper
public class ItemsOperators {

    /**
     * Collect the items in the stream to a list.
     * Each element in the list is an instance of Item.
     *
     * @return the function
     */
    public static Function<List<Item>, List<Item>> asList() {
        return new StreamListCollector();
    }

    /**
     * Collect the values of a field in the stream to a list.
     * Each element in the list is the value of the specified field in an item.
     *
     * @param fieldToCollect the name of the field to collect.
     * @return the function
     */
    public static <TValue> Function<List<Item>, List<TValue>> asList(String fieldToCollect) {
        return new StreamFieldListCollector<>(fieldToCollect);
    }

    /**
     * Select an item from the items that has the max value of a field.
     *
     * @param numField the name of the number field
     */
    public static Function<List<Item>, Item> getItemWithMax(String numField) {
        return new MaxFieldSelector(numField);
    }

    /**
     * Select an item from the items that has the min value of a field.
     *
     * @param numField the name of the number field
     */
    public static Function<List<Item>, Item> getItemWithMin(String numField) {
        return new MinFieldSelector(numField);
    }

    /**
     * Select an item from the items that has the most elements in a list field.
     *
     * @param listField the name of the list field
     */
    public static Function<List<Item>, Item> getItemWithMost(String listField) {
        return new MostFieldSelector(listField);
    }

    /**
     * Select an item from the items that has the least elements in a list field.
     *
     * @param listField the name of the list field
     */
    public static Function<List<Item>, Item> getItemWithLeast(String listField) {
        return new LeastFieldSelector(listField);
    }
}
