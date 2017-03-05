package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 18/02/2017.
 * A Provider that provides continuous random MockItem updates
 */
class RandomLiveMStreamProvider extends MultiItemStreamProvider {
    private final int maxInt;
    private final double maxDouble;
    private final long interval;

    RandomLiveMStreamProvider(int maxInt, double maxDouble, long interval) {
        this.maxInt = maxInt;
        this.maxDouble = maxDouble;
        this.interval = interval;
        this.addParameters(maxInt, maxDouble, interval);
    }

    @Override
    protected void provide() {
        int id = 0;
        while (!this.isCancelled) {
            MockObject mockObject = MockObject.getRandomInstance(this.maxInt, this.maxDouble);
            mockObject.setId(id);
            id++;
            this.output(new MockItem(mockObject));
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
