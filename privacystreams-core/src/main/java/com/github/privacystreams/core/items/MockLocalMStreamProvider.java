package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.List;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
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
