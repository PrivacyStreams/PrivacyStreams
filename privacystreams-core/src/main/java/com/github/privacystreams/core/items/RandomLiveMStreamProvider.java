package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Provide continuous random TestItem updates.
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
            TestItem item = new TestItem(testObject);
            this.output(item);
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.finish();
    }

//    @Override
//    protected void onCancel(UQI uqi) {
//        super.onCancel(uqi);
//        try {
//            throw new Exception();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
