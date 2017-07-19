package io.github.privacystreams.core.transformations.group;

import io.github.privacystreams.core.PStreamTransformation;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access grouping functions
 */
@PSOperatorWrapper
public class Groupers {

    /**
     * Group the items according to a field.
     * After grouping, the items in the new stream will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg. `groupBy("x")` will group the items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param groupField the field used to group the items in current stream.
     * @return the grouper function.
     */
    public static PStreamTransformation groupBy(String groupField) {
        return new ByFieldGrouper(groupField);
    }

    /**
     * Group the **contiguous** items according to a field.
     * After grouping, the items in the new stream will only have two fields.
     * One is the field used for grouping by. Another is "grouped_items" which is a list of grouped Items.
     * Eg.  `localGroupBy("x")` will group the contiguous items with same "x" field,
     * and the item in the stream after groupping will contain two fields: "x" and "grouped_items".
     *
     * @param groupField the field used to group the items in current stream.
     * @return the grouper function.
     */
    public static PStreamTransformation localGroupBy(String groupField) {
        return new ByFieldLocalGrouper(groupField);
    }

    /**
     * Un-group a list field in each item to multiple items.
     * Each element in the list will be a new field in each item of the new stream.
     * After un-grouping, the items in the new streams will have the same amount of fields
     * as the original stream.
     * However, the list field (`unGroupField`) will be replaced by a new field (`newField`).
     * Eg.  `unGroup("emails", "email")` will un-group the "emails" field (which is a list)
     * in an item to several new items with a "email" field.
     *
     * @param unGroupField the field to un-group, whose value should be a list
     * @param newField the new field name in the new stream
     * @return The un-group function
     */
    public static PStreamTransformation unGroup(String unGroupField, String newField) {
        return new UnGrouper(unGroupField, newField);
    }

}
