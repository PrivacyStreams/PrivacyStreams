package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class MockSStreamProvider extends SingleItemStreamProvider {

    private final MockObject mockObject;

    public MockSStreamProvider(MockObject mockObject) {
        this.mockObject = mockObject;
    }

    @Override
    protected void provide(SingleItemStream output) {
        if (this.isCancelled() || output.isClosed()) return;
        output.write(new MockItem(mockObject));
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add("MockObject: " + this.mockObject);
        return parameters;
    }
}
