package com.github.privacystreams.core.items;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * A random item for testing.
 */
@PSItem
public class TestItem extends Item {

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

    private TestItem(long id, Integer x, String y, Double z) {
        this.setFieldValue(ID, id);
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    public TestItem(TestObject testObject) {
        this(testObject.getId(), testObject.getX(), testObject.getY(), testObject.getZ());
    }

    /**
     * Provide a live stream of TestItem items, which are from a given list.
     *
     * @param testObjects the list of mock data
     * @param interval the interval between each two items, in milliseconds
     * @return the provider function
     */
    public static MStreamProvider asUpdatesFrom(List<TestObject> testObjects, long interval) {
        return new MockLiveMStreamProvider(testObjects, interval);
    }

    /**
     * Provide a live stream of TestItem items, which are randomly generated.
     *
     * @param maxInt the max value of the int field of the random mock items
     * @param maxDouble the max value of the double field of the random mock items
     * @param interval the interval between each two items, in milliseconds
     * @return the provider function
     */
    public static MStreamProvider asUpdates(int maxInt, double maxDouble, long interval) {
        return new RandomLiveMStreamProvider(maxInt, maxDouble, interval);
    }

    /**
     * Provide a stream of existing TestItem items, which are from a given list.
     *
     * @param testObjects the list of mock data
     * @return the provider function
     */
    public static MStreamProvider getAllFrom(List<TestObject> testObjects) {
        return new MockLocalMStreamProvider(testObjects);
    }

    /**
     * Provide a list of TestItem items, which are randomly generated.
     *
     * @param maxInt the max value of the int field of the random mock items
     * @param maxDouble the max value of the double field of the random mock items
     * @param count the number of random items
     * @return the provider function
     */
    public static MStreamProvider getAllRandom(int maxInt, double maxDouble, int count) {
        return new RandomLocalMStreamProvider(maxInt, maxDouble, count);
    }

    /**
     * Provide one TestItem item, which is based on an given TestObject.
     *
     * @param testObject the mock data
     * @return the provider function
     */
    public static SStreamProvider getOneFrom(TestObject testObject) {
        return new MockSStreamProvider(testObject);
    }

    /**
     * Provide one TestItem item, which is randomly generated.
     *
     * @return the provider function
     */
    public static SStreamProvider getOne() {
        return new MockSStreamProvider(TestObject.getRandomInstance());
    }
}
