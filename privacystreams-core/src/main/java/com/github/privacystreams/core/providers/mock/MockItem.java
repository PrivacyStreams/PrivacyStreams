package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * A MockItem is a randomly generated item for testing.
 */
@PSItem
public class MockItem extends Item {

    /**
     * The index of current item.
     */
    @PSItemField(type = Long.class)
    public static final String ID = "id";

    /**
     * A random integer.
     */
    @PSItemField(type = Integer.class)
    public static final String X = "x";

    /**
     * A random String.
     */
    @PSItemField(type = String.class)
    public static final String Y = "y";

    /**
     * A random float number.
     */
    @PSItemField(type = Double.class)
    public static final String Z = "z";

    /**
     * The timestamp of when current item is created.
     */
    @PSItemField(type = Long.class)
    public static final String TIME_CREATED = "time_created";

    private MockItem(long id, Integer x, String y, Double z) {
        this.setFieldValue(ID, id);
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
        this.setFieldValue(TIME_CREATED, System.currentTimeMillis());
    }

    public MockItem(MockObject mockObject) {
        this(mockObject.getId(), mockObject.getX(), mockObject.getY(), mockObject.getZ());
    }

    /**
     * A function that provides a live stream of MockItem updates, the items are from an existing list.
     * @param mockObjects the list of mock data
     * @param interval the interval between each two items, in milliseconds
     * @return the provider function
     */
    public static Function<Void, MultiItemStream> asUpdates(List<MockObject> mockObjects, long interval) {
        return new MockLiveMStreamProvider(mockObjects, interval);
    }

    /**
     * A function that provides a live stream of random MockItem updates.
     * @param maxInt the max value of the int field of the random mock items
     * @param maxDouble the max value of the double field of the random mock items
     * @param interval the interval between each two items, in milliseconds
     * @return the provider function
     */
    public static Function<Void, MultiItemStream> asRandomUpdates(int maxInt, double maxDouble, long interval) {
        return new RandomLiveMStreamProvider(maxInt, maxDouble, interval);
    }

    /**
     * A function that provides a stream of MockItem history, the items are from an existing list.
     * @param mockObjects the list of mock data
     * @return the provider function
     */
    public static Function<Void, MultiItemStream> asHistory(List<MockObject> mockObjects) {
        return new MockLocalMStreamProvider(mockObjects);
    }

    /**
     * A function that provides a stream of random MockItem history.
     * @param maxInt the max value of the int field of the random mock items
     * @param maxDouble the max value of the double field of the random mock items
     * @param count the number of random items
     * @return the provider function
     */
    public static Function<Void, MultiItemStream> asRandomHistory(int maxInt, double maxDouble, int count) {
        return new RandomLocalMStreamProvider(maxInt, maxDouble, count);
    }

    /**
     * A function that provides a single-item stream, the item is based on an existing MockObject
     * @param mockObject the mock data
     * @return the provider function
     */
    public static Function<Void, SingleItemStream> asItem(MockObject mockObject) {
        return new MockSStreamProvider(mockObject);
    }

    /**
     * A function that provides a single-item stream, the item is randomly generated.
     * @return the provider function
     */
    public static Function<Void, SingleItemStream> asRandomItem() {
        return new MockSStreamProvider(MockObject.getRandomInstance());
    }
}
