package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * an item representing MockObject
 */
@PSItem
public class MockItem extends Item {

    @PSItemField(name = "id", type = Long.class, description = "The index of current item.")
    public static final String ID = "id";

    @PSItemField(name = "x", type = Integer.class, description = "A random integer.")
    public static final String X = "x";

    @PSItemField(name = "y", type = String.class, description = "A random String.")
    public static final String Y = "y";

    @PSItemField(name = "z", type = Double.class, description = "A random float number.")
    public static final String Z = "z";

    @PSItemField(name = "time_created", type = Long.class, description = "The timestamp of when current item is created.")
    public static final String TIME_CREATED = "time_created";

    public MockItem(long id, Integer x, String y, Double z) {
        this.setFieldValue(ID, id);
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
        this.setFieldValue(TIME_CREATED, System.currentTimeMillis());
    }

    public MockItem(MockObject mockObject) {
        this(mockObject.getId(), mockObject.getX(), mockObject.getY(), mockObject.getZ());
    }

    public static MultiItemStreamProvider asUpdates(List<MockObject> mockObjects, int delay) {
        return new MockLiveMStreamProvider(mockObjects, delay);
    }

    public static MultiItemStreamProvider asRandomUpdates(int maxInt, double maxDouble, long interval) {
        return new RandomLiveMStreamProvider(maxInt, maxDouble, interval);
    }

    public static MultiItemStreamProvider asHistory(List<MockObject> mockObjects) {
        return new MockLocalMStreamProvider(mockObjects);
    }

    public static MultiItemStreamProvider asRandomHistory(int maxInt, double maxDouble, int count) {
        return new RandomLocalMStreamProvider(maxInt, maxDouble, count);
    }

    public static SingleItemStreamProvider asItem(MockObject mockObject) {
        return new MockSStreamProvider(mockObject);
    }

    public static SingleItemStreamProvider asRandomItem() {
        return new MockSStreamProvider(MockObject.getRandomInstance());
    }
}
