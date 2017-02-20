package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

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
        this.mockObjects = mockObjects;
        this.delay = delay;
    }

    @Override
    protected void provide(MultiItemStream output) {
        for (MockObject mockObject : this.mockObjects) {
            if (!this.isCancelled() && !output.isClosed()) {
                output.write(new MockItem(mockObject));
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
        output.write(null);
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add("mockObjects: " + this.mockObjects.size());
        parameters.add(delay);
        return parameters;
    }
}
