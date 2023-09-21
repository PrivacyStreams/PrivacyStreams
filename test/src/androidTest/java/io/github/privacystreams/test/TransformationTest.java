package io.github.privacystreams.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;

@RunWith(AndroidJUnit4.class)
public class TransformationTest {
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
        uqi.stopAll();
    }


}
