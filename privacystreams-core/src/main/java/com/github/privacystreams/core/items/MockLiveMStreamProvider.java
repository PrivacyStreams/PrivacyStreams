package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Assertions;

import java.util.List;


/**
 * Provide a live stream of TestItems based on a given list.
 */

class MockLiveMStreamProvider extends MStreamProvider {

    private final List<TestObject> testObjects;
    private final long interval;

    MockLiveMStreamProvider(List<TestObject> testObjects, long interval) {
        this.testObjects = Assertions.notNull("testObjects", testObjects);
        this.interval = interval;
        this.addParameters(testObjects, interval);
    }

    @Override
    protected void provide() {
        for (TestObject testObject : this.testObjects) {
            if (!this.isCancelled) {
                this.output(new TestItem(testObject));
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }
        this.finish();
    }
}
