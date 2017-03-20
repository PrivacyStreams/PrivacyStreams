package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.SStreamProvider;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockSStreamProvider extends SStreamProvider {

    private final TestObject testObject;

    public MockSStreamProvider(TestObject testObject) {
        this.testObject = testObject;
        this.addParameters(testObject);
    }

    @Override
    protected void provide() {
        if (this.isCancelled) return;
        this.output(new TestItem(testObject));
        this.finish();
    }

}
