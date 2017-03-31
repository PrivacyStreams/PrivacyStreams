package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.List;


/**
 * Provide a bunch of local TestItems based on a list of TestObjects.
 */

class MockLocalMStreamProvider extends MStreamProvider {

    private final List<TestObject> testObjects;

    MockLocalMStreamProvider(List<TestObject> testObjects) {
        this.testObjects = testObjects;
        this.addParameters(testObjects);
    }

    @Override
    protected void provide() {
        for(TestObject testObject : testObjects){
            if (this.isCancelled) break;
            TestItem item = new TestItem(testObject);
            this.output(item);
        }
        this.finish();
    }

}
