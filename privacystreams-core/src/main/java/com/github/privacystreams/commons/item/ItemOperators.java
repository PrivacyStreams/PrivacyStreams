package com.github.privacystreams.commons.item;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * This function must be applied to a group item, i.e. must be used after <code>groupBy</code> or <code>localGroupBy</code>.
     *
     * @param subStreamFunction the function to output sub stream.
     * @param <Tout> the type of sub stream collection result.
     * @return the function
     */
    public static <Tout> Function<Item, Tout> collectGroupedItems(Function<List<Item>, Tout> subStreamFunction) {
        return new ItemSubStreamFunction<>(subStreamFunction);
    }

    /**
     * Set the value of a new field with a function.
     * This function must be applied to a group item, i.e. must be used after <code>groupBy</code> or <code>localGroupBy</code>.
     * For example, `.groupBy("name").setGroupField("count", count())` will group the items with same "name" together,
     * and set "count" field to the number of items in each group.
     *
     * @param fieldToSet the name of the field to set, it can be a new name or an existing name.
     * @param itemsFunction the function to compute the field value based on the grouped items.
     * @param <TValue> the type of the new field value.
     * @return the item mapper function.
     */
    public static <TValue> Function<Item, Item> setGroupField(String fieldToSet, Function<List<Item>, TValue> itemsFunction) {
        return new FieldSetter<>(fieldToSet, new ItemSubStreamFunction<>(itemsFunction));
    }

    /**
     * Output the item as a key-value map.
     *
     * @return the function.
     */
    public static Function<Item, Map<String, Object>> asMap() {
        return new ItemToMapFunction();
    }

    /**
     * Set the value of a new field with a function.
     *
     * @param fieldToSet the name of the field to set, it can be a new name.
     * @param itemFunction the function to compute the value of the field based on the item.
     * @param <TValue> the type of the new field value.
     * @return the item mapper function.
     */
    public static <TValue> Function<Item, Item> setField(String fieldToSet, Function<Item, TValue> itemFunction) {
        return new FieldSetter<>(fieldToSet, itemFunction);
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

    /**
     * Print the item for debugging.
     *
     * @return the function
     */
    public static Function<Item, Void> debug() {
        return new ItemDebugPrinter();
    }
}
