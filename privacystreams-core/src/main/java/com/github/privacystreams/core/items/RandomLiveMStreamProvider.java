package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Created by yuanchun on 18/02/2017.
 * A Provider that provides continuous random TestItem updates
 */
class RandomLiveMStreamProvider extends MStreamProvider {
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
            TestObject testObject = TestObject.getRandomInstance(this.maxInt, this.maxDouble);
            testObject.setId(id);
            id++;
            this.output(new TestItem(testObject));
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
