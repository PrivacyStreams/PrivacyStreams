package com.github.privacystreams.core;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.privacystreams.utils.Assertions.cast;

/**
 * Created by yuanchun on 29/11/2016.
 * An PSItem is a basic element in a stream.
 */
public class Item {
    private final Map<String, Object> itemMap;

    public Item() {
        this(new HashMap<String, Object>());
    }

    public Item(Map<String, Object> itemMap) {
        this.itemMap = itemMap;
    }

    public static final Item EOS = new Item();

    public boolean isEndOfStream() {
        return this == EOS;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.putAll(this.itemMap);
        return outputMap;
    }

    public JSONObject toJson() {
        return new JSONObject(this.toMap());
    }

    public String toString() {
        return this.toMap().toString();
    }

    /**
     * Get the value of a field in the item
     *
     * @param fieldName the name of the field
     * @param <TValue>  the type of field value
     * @return the field value
     */
    public <TValue> TValue getValueByField(String fieldName) {
        if (itemMap.containsKey(fieldName))
            return cast(fieldName, itemMap.get(fieldName));
        else return null;
    }

    /**
     * Set a value to a field in the item
     *
     * @param fieldName the name of field
     * @param value     the value of field
     * @param <TValue>  the type of field value
     */
    public <TValue> void setFieldValue(String fieldName, TValue value) {
        this.itemMap.put(fieldName, value);
    }

    /**
     * Test if current item contains a field
     *
     * @param fieldName the field name to test
     * @return true if the item contains the field, otherwise false
     */
    public boolean containsField(String fieldName) {
        return this.itemMap.containsKey(fieldName);
    }

    /**
     * Include certain fields for output
     * the included fields will be available for output, while other fields will not
     *
     * @param fieldKeys the names of fields to include
     */
    public void includeFields(String... fieldKeys) {
        HashSet<String> fieldKeysToRemove = new HashSet<>();
        fieldKeysToRemove.addAll(this.itemMap.keySet());
        fieldKeysToRemove.removeAll(Arrays.asList(fieldKeys));
        this.itemMap.keySet().removeAll(fieldKeysToRemove);
    }

    /**
     * Exclude some fields from current item
     * the excluded fields will not be available for output
     *
     * @param fieldKeys the names of fields to exclude
     */
    public void excludeFields(String... fieldKeys) {
        HashSet<String> fieldKeysToRemove = new HashSet<>(Arrays.asList(fieldKeys));
        this.itemMap.keySet().removeAll(fieldKeysToRemove);
    }

    public boolean equals(Item anotherItem) {
        // TODO: need testing
        return this.toMap().equals(anotherItem.toMap());
    }

}
