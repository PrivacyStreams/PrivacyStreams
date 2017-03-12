package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.List;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockLocalMStreamProvider extends MultiItemStreamProvider {

    private final List<MockObject> mockObjects;

    MockLocalMStreamProvider(List<MockObject> mockObjects) {
        this.mockObjects = mockObjects;
        this.addParameters(mockObjects);
    }

    @Override
    protected void provide() {
        for(MockObject mockObject : mockObjects){
            if (this.isCancelled) break;
            MockItem item = new MockItem(mockObject);
            this.output(item);
        }
        this.finish();
    }

}
