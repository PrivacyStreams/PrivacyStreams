package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.List;

/**
 * Created by yuanchun on 18/02/2017.
 * A Provider that provides continuous random MockItem updates
 */
class RandomLocalMStreamProvider extends MStreamProvider {
    private final int maxInt;
    private final double maxDouble;
    private final int count;

    RandomLocalMStreamProvider(int maxInt, double maxDouble, int count) {
        this.maxInt = maxInt;
        this.maxDouble = maxDouble;
        this.count = count;
        this.addParameters(maxInt, maxDouble, count);
    }

    @Override
    protected void provide() {
        int id = 0;
        if (!this.isCancelled) {
            List<MockObject> mockObjects = MockObject.getRandomList(this.maxInt, maxDouble, count);
            for (MockObject mockObject : mockObjects) {
                mockObject.setId(id);
                id++;
                this.output(new MockItem(mockObject));
            }
            this.finish();
        }
    }
}
