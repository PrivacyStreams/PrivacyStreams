package com.github.privacystreams.commons.item;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.Arrays;
import java.util.List;

/**
 * A helper class to access common item functions
 */
@PSOperatorWrapper
public class ItemOperators {
    /**
     * Check whether the value of a field is in a given list.
     *
     * @param field the field name
     * @param listToCompare the list to check whether the field is in
     * @param <TValue> the type of list elements
     * @return the predicate
     */
    public static <TValue> Function<Item, Boolean> isFieldIn(final String field, final TValue[] listToCompare) {
        return new FieldInCollectionPredicate<>(field, Arrays.asList(listToCompare));
    }

    /**
     * Check whether the item contains a given field.
     *
     * @param fieldToCheck the name of field to check
     * @return the predicate
     */
    public static Function<Item, Boolean> containsField(final String fieldToCheck) {
        return new ContainsFieldPredicate(fieldToCheck);
    }

    /**
     * Get the value of a given field in the item.
     *
     * @param field the name of the field to get.
     * @param <TValue> the type of field value
     * @return the function
     */
    public static <TValue> Function<Item, TValue> getField(final String field) {
        return new FieldValueGetter<>(field);
    }

    /**
     * Output the grouped items in the item with a function.
     * This function must be applied to a group item, i.e. must be used after `groupBy` or `localGroupBy`.
     *
     * @param subStreamFunction the function to output sub stream.
     * @param <Tout> the type of sub stream collection result.
     * @return the function
     */
    public static <Tout> Function<Item, Tout> wrapSubStreamFunction(Function<List<Item>, Tout> subStreamFunction) {
        return new ItemSubStreamFunction<>(subStreamFunction);
    }

    /**
     * Wrap a valueGenerator that takes Void as input type to a function that takes Item as input type.
     *
     * @param valueGenerator the function that takes Void as input.
     * @param <Tout> the type of value generator result.
     * @return the function
     */
    public static <Tout> Function<Item, Tout> wrapValueGenerator(Function<Void, Tout> valueGenerator) {
        return new IndependentItemFunction<>(valueGenerator);
    }

    /**
     * Set a field to a new value for each item in the stream.
     * This transformation can only be used after invoking group methods (`groupBy`, `localGroupBy`, etc.).
     * The value is computed with a function that takes the grouped items as input.
     * Eg. `setGroupField("count", StatisticOperators.count())` will set a new field "count" to each item,
     * which represents the number of items in the grouped sub stream.
     *
     * @param fieldToSet the new field name
     * @param fieldValueComputer the function to compute the new field value, which takes the list of grouped items as input.
     * @param <TValue> the type of the new field value
     * @return the stream of items with the new field set
     */
    public static <TValue> Function<Item, Item> setGroupField(String fieldToSet, Function<List<Item>, TValue> fieldValueComputer) {
        return new FieldSetter<>(fieldToSet, new ItemSubStreamFunction<>(fieldValueComputer));
    }

    /**
     * Set the value of a new field with a value generator function.
     * The value generator function is independent from current item, which does not need a input (Void).
     * The value generator will be evaluated on demand at runtime.
     *
     * For example, `setIndependentField("time", TimeOperators.getCurrentTime())` will set the field "time" to a timestamp in each item;
     * `setIndependentField("wifiStatus", DeviceOperators.isWifiConnected())` will set the field "wifiStatus" to a boolean indicating whether wifi is connected in each item.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param valueGenerator the function to compute the field value.
     * @param <TValue> the type of the new field value.
     * @return the item mapper function.
     */
    public static <TValue> Function<Item, Item> setIndependentField(String fieldToSet, Function<Void, TValue> valueGenerator) {
        return new FieldSetter<>(fieldToSet, new IndependentItemFunction<>(valueGenerator));
    }

    /**
     * Output the item as a key-value map.
     *
     * @return the function.
     */
    public static Function<Item, Item> asItem() {
        return new ItemIdFunction();
    }

    /**
     * Set the value of a new field with a function.
     *
     * @param fieldToSet the name of the field to set, it can be a new name.
     * @param fieldValueComputer the function to compute the value of the field based on the item.
     * @param <TValue> the type of the new field value.
     * @return the item mapper function.
     */
    public static <TValue> Function<Item, Item> setField(String fieldToSet, Function<Item, TValue> fieldValueComputer) {
        return new FieldSetter<>(fieldToSet, fieldValueComputer);
    }

    /**
     * Set the value of a new field to a given value.
     *
     * @param fieldToSet the name of the field to set, it can be a new name.
     * @param fieldValue the value of the field.
     * @param <TValue> the type of the new field value.
     * @return the item mapper function.
     */
    public static <TValue> Function<Item, Item> setField(String fieldToSet, TValue fieldValue) {
        return new FieldValueSetter<>(fieldToSet, fieldValue);
    }

    /**
     * Project an item by including some fields.
     * The fields that are not included will be removed from the item.
     *
     * @param fieldsToInclude the names of the fields to include.
     * @return the item mapper function.
     */
    public static Function<Item, Item> includeFields(String... fieldsToInclude) {
        return new ItemProjector(ItemProjector.OPERATOR_INCLUDE, fieldsToInclude);
    }

    /**
     * Project an item by excluding some fields.
     * The excluded fields will be removed from the item.
     *
     * @param fieldsToExclude the names of the fields to exclude.
     * @return the item mapper function.
     */
    public static Function<Item, Item> excludeFields(String... fieldsToExclude) {
        return new ItemProjector(ItemProjector.OPERATOR_EXCLUDE, fieldsToExclude);
    }

    /**
     * Get the sub item value of a given field.
     *
     * @param subItemField the name of sub item field.
     * @return the function.
     */
    public static Function<Item, Item> getSubItem(String subItemField) {
        return new SubItemGetter(subItemField);
    }

}
