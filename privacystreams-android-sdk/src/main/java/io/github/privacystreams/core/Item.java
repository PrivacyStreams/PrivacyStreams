package io.github.privacystreams.core;

import io.github.privacystreams.utils.IOUtils;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import static io.github.privacystreams.utils.Assertions.cast;

/**
 * An Item is a basic element in a stream.
 * This class is the base class of all type of personal data items in PrivacyStream.
 */
@PSItem
public class Item {

    /**
     * The timestamp of when this item is created.
     * It is a general field for all items.
     */
    @PSItemField(type = Long.class)
    public static final String TIME_CREATED = "time_created";

    private final Map<String, Object> itemMap;

    public Item() {
        this(new HashMap<String, Object>());
    }

    public Item(Map<String, Object> itemMap) {
        this.itemMap = itemMap;
        this.setFieldValue(TIME_CREATED, System.currentTimeMillis());
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
        return new JSONObject((Map) IOUtils.serialize(this.itemMap));
    }

    public String toString() {
        return this.toJson().toString();
    }

    public String toDebugString() {
        if (this == EOS) return "ITEM_EOS";
        String itemStr = "ITEM {\n";
        for (String fieldKey : this.itemMap.keySet()) {
            Object fieldValue = this.itemMap.get(fieldKey);
            if (fieldValue == null) {
                itemStr += String.format(Locale.getDefault(),
                        "\t\"%s\": null\n",
                        fieldKey);
            } else {
                String fieldValueClass = fieldValue.getClass().getSimpleName();
                itemStr += String.format(Locale.getDefault(),
                        "\t%s \"%s\": %s\n",
                        fieldValueClass, fieldKey, fieldValue.toString());
            }
        }
        itemStr += "}";
        return itemStr;
    }

    /**
     * Get the value of a field in the item.
     *
     * @param fieldName the name of the field
     * @param <TValue>  the type of field value
     * @return the field value
     */
    public <TValue> TValue getValueByField(String fieldName) {
        if (itemMap.containsKey(fieldName)) {
            return cast(fieldName, itemMap.get(fieldName));
        }
        Logging.error("Unknown field: \"" + fieldName + "\" in " + this.toString());
        return null;
    }

    /**
     * Get the value of a String field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public String getAsString(String fieldName) {
        return getValueByField(fieldName);
    }

    /**
     * Get the value of a Boolean field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public Boolean getAsBoolean(String fieldName) {
        return getValueByField(fieldName);
    }

    /**
     * Get the value of a Integer field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public Integer getAsInteger(String fieldName) {
        return getValueByField(fieldName);
    }

    /**
     * Get the value of a Long field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public Long getAsLong(String fieldName) {
        return getValueByField(fieldName);
    }

    /**
     * Get the value of a Double field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public Double getAsDouble(String fieldName) {
        return getValueByField(fieldName);
    }

    /**
     * Get the value of a Float field in the item.
     *
     * @param fieldName the name of the field
     * @return the field value
     */
    public Float getAsFloat(String fieldName) {
        return getValueByField(fieldName);
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
        return this == anotherItem || anotherItem != null && this.toMap().equals(anotherItem.toMap());
    }

}
