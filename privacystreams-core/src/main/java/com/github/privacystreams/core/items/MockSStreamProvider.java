package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.SStreamProvider;


/**
 * Provide a TestItem based on a give TestObject.
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
