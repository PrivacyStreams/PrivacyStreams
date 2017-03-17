package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Assertions;

import java.util.List;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockLiveMStreamProvider extends MStreamProvider {

    private final List<MockObject> mockObjects;
    private final long interval;

    MockLiveMStreamProvider(List<MockObject> mockObjects, long interval) {
        this.mockObjects = Assertions.notNull("mockObjects", mockObjects);
        this.interval = interval;
        this.addParameters(mockObjects, interval);
    }

    @Override
    protected void provide() {
        for (MockObject mockObject : this.mockObjects) {
            if (!this.isCancelled) {
                this.output(new MockItem(mockObject));
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
