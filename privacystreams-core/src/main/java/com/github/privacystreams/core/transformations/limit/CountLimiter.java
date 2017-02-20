package com.github.privacystreams.core.transformations.limit;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/11/2016.
 * limit the count of items in the stream
 */
final class CountLimiter extends StreamLimiter {
    private final int maxCount;
    private int count;

    CountLimiter(int maxCount) {
        this.maxCount = maxCount;
        this.count = 0;
    }

    @Override
    protected boolean keep(Item item) {
        this.count++;
        return this.count <= this.maxCount;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(String.valueOf(maxCount));
        return parameters;
    }

}
