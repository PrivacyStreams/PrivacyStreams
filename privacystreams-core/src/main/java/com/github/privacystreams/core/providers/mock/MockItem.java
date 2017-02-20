package com.github.privacystreams.core.providers.mock;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;

import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * an item representing MockObject
 */

public class MockItem extends Item {
    public static final String ID = "id";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
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
