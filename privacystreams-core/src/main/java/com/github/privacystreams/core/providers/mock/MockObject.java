package com.github.privacystreams.core.providers.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yuanchun on 14/02/2017.
 * A mock object for testing
 */

public class MockObject {

    private int id;
    private final int x;
    private final String y;
    private final double z;

    public MockObject(int id, int x, String y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    private static final int DEFAULT_MAX_INT = 100;
    private static final int DEFAULT_MAX_DOUBLE = 10;

    public static MockObject getRandomInstance() {
        return MockObject.getRandomInstance(DEFAULT_MAX_INT, DEFAULT_MAX_DOUBLE);
    }

    public static MockObject getRandomInstance(int maxInt, double maxDouble) {
        Random random = new Random();
        Integer randInt = random.nextInt(maxInt);
        String randString = Integer.toHexString(randInt);
        Double randDouble = random.nextDouble() * maxDouble;
        return new MockObject(-1, randInt, randString, randDouble);
    }

    public static List<MockObject> getRandomList(int count) {
        return MockObject.getRandomList(DEFAULT_MAX_INT, DEFAULT_MAX_DOUBLE, count);
    }

    public static List<MockObject> getRandomList(int maxInt, double maxDouble, int count) {
        List<MockObject> dataList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            MockObject mockObject = MockObject.getRandomInstance(maxInt, maxDouble);
            mockObject.setId(i);
            dataList.add(mockObject);
        }

        return dataList;
    }

    public static List<MockObject> deepCopy(List<MockObject> originalData) {
        List<MockObject> result = new ArrayList<>();
        for (MockObject item : originalData) {
            result.add(new MockObject(item.getId(), item.getX(), item.getY(), item.getZ()));
        }
        return result;
    }

    public String toString() {
        return String.format("MockObject: id=%d, x=%d, y=%s, z=%f",
                this.id, this.x, this.y, this.z);
    }
}
