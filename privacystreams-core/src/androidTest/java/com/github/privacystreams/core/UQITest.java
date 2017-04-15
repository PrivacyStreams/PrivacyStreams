package com.github.privacystreams.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.github.privacystreams.core.items.TestItem;
import com.github.privacystreams.core.items.TestObject;
import com.github.privacystreams.core.purposes.Purpose;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for UQI class
 */
@RunWith(AndroidJUnit4.class)
public class UQITest {
    private UQI uqi;
    private Purpose testPurpose;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        this.uqi = new UQI(appContext);
        this.testPurpose = Purpose.TEST("unit test.");

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getContext() throws Exception {
        Context context = this.uqi.getContext();
        assertNotNull(context);
    }

    @Test
    public void getDataItems() throws Exception {
        List<TestObject> testObjectList = TestObject.getRandomList(10);
        int itemCount = this.uqi
                .getData(TestItem.getAllFrom(testObjectList), this.testPurpose)
                .count();
        assertEquals(10, itemCount);
    }

    @Test
    public void getDataItem() throws Exception {
        TestObject testObject = TestObject.getRandomInstance();
        int mockItemX = this.uqi
                .getData(TestItem.getOneFrom(testObject), this.testPurpose)
                .getField(TestItem.X);
//        System.out.println("mockItemX: " + mockItemX);
        assertEquals(testObject.getX(), mockItemX);
    }

}