package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.providers.SStreamProvider;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockSStreamProvider extends SStreamProvider {

    private final MockObject mockObject;

    public MockSStreamProvider(MockObject mockObject) {
        this.mockObject = mockObject;
        this.addParameters(mockObject);
    }

    @Override
    protected void provide() {
        if (this.isCancelled) return;
        this.output(new MockItem(mockObject));
        this.finish();
    }

}
