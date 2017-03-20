package com.github.privacystreams.core.items;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * A mock item. The content of a MockItem is mocked from another item.
 */
@PSItem
public class MockItem extends Item {

    /**
     * Provide a TestItem item, which is randomly generated.
     *
     * @return the provider function
     */
    public static SStreamProvider asRandomItem() {
        return new MockSStreamProvider(TestObject.getRandomInstance());
    }
}
