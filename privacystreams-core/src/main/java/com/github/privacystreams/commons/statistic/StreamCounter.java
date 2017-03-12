package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.core.Item;

import java.util.List;

/**
 * Created by yuanchun on 23/12/2016.
 * A function that counts the items in the stream
 */
final class StreamCounter extends StreamStatistic<Integer> {
    @Override
    public Integer calculate(List<Item> items) {
        return items.size();
    }
}
