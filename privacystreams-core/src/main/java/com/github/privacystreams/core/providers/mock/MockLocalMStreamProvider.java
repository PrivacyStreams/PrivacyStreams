package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.ArrayList;
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
            if (this.isCancelled() || output.isClosed()) break;
            MockItem item = new MockItem(mockObject);
            this.output(item);
        }
        this.output(Item.EOS);
    }

}
