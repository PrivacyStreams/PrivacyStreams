package com.github.privacystreams.core.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A mock object for testing.
 */

public class TestObject {

    private int id;
    private final int x;
    private final String y;
    private final double z;

    public TestObject(int id, int x, String y, double z) {
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

    public static TestObject getRandomInstance() {
        return TestObject.getRandomInstance(DEFAULT_MAX_INT, DEFAULT_MAX_DOUBLE);
    }

    public static TestObject getRandomInstance(int maxInt, double maxDouble) {
        Random random = new Random();
        Integer randInt = random.nextInt(maxInt);
        String randString = "str" + Integer.toHexString(randInt);
        Double randDouble = random.nextDouble() * maxDouble;
        return new TestObject(-1, randInt, randString, randDouble);
    }

    public static List<TestObject> getRandomList(int count) {
        return TestObject.getRandomList(DEFAULT_MAX_INT, DEFAULT_MAX_DOUBLE, count);
    }

    public static List<TestObject> getRandomList(int maxInt, double maxDouble, int count) {
        List<TestObject> dataList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            TestObject testObject = TestObject.getRandomInstance(maxInt, maxDouble);
            testObject.setId(i);
            dataList.add(testObject);
        }

        return dataList;
    }

    public static List<TestObject> deepCopy(List<TestObject> originalData) {
        List<TestObject> result = new ArrayList<>();
        for (TestObject item : originalData) {
            result.add(new TestObject(item.getId(), item.getX(), item.getY(), item.getZ()));
        }
        return result;
    }

    public String toString() {
        return String.format(Locale.getDefault(),
                "TestObject: id=%d, x=%d, y=%s, z=%f",
                this.id, this.x, this.y, this.z);
    }
}
