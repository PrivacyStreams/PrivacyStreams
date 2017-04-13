package com.github.privacystreams.commons.list;

import java.util.List;

/**
 * Count the number of valid fields in a stream.
 */
final class FieldCounter extends NumListProcessor<Integer> {

    FieldCounter(String numListField) {
        super(numListField);
    }

    @Override
    protected Integer processNumList(List<Number> numList) {
        return numList == null ? null : numList.size();
    }
}
