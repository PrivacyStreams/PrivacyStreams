package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Assertions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockLiveMStreamProvider extends MultiItemStreamProvider {

    private final List<MockObject> mockObjects;
    private final long delay;

    MockLiveMStreamProvider(List<MockObject> mockObjects, long delay) {
        this.mockObjects = Assertions.notNull("mockObjects", mockObjects);
        this.delay = delay;
        this.addParameters(mockObjects, delay);
    }

    @Override
    protected void provide() {
        for (MockObject mockObject : this.mockObjects) {
            if (!this.isCancelled() && !output.isClosed()) {
                this.output(new MockItem(mockObject));
                try {
                    Thread.sleep(this.delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }
        this.output(null);
    }
}
