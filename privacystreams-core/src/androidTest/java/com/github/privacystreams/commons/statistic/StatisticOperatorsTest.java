package com.github.privacystreams.commons.statistic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.items.TestItem;
import com.github.privacystreams.core.items.TestObject;
import com.github.privacystreams.core.purposes.Purpose;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test statistic operators
 */
public class StatisticOperatorsTest {
    private UQI uqi;
    private Purpose testPurpose;
    private List<Item> testItems;
    private List<Integer> testIntegers;

    private final int NORMAL_COUNT = 1000;
    private final int IDLE_COUNT = 10;
    private final double DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        this.uqi = new UQI(appContext);
        this.testPurpose = Purpose.TEST("Testing statistic operators.");
        this.testItems = this.uqi
                .getData(TestItem.getAllRandom(100, 100, NORMAL_COUNT), this.testPurpose)
                .asList();

        this.testIntegers = new ArrayList<>();
        for (Item item : this.testItems) {
            this.testIntegers.add((Integer) item.getValueByField(TestItem.X));
        }
        for (int i = 0; i < IDLE_COUNT; i++) {
            this.testItems.add(new Item());
        }
    }

    @After
    public void tearDown() throws Exception {
        this.uqi.stopAll();
    }

    @Test
    public void count() throws Exception {
        Integer count = StatisticOperators.count().apply(this.uqi, this.testItems);
        assertEquals(NORMAL_COUNT + IDLE_COUNT, count.intValue());
    }

    @Test
    public void count1() throws Exception {
        Integer count = StatisticOperators.count(TestItem.X).apply(this.uqi, this.testItems);
        assertEquals(NORMAL_COUNT, count.intValue());
    }

    @Test
    public void range() throws Exception {
        Double range = StatisticOperators.range(TestItem.X).apply(this.uqi, this.testItems);
        Integer max = Collections.max(this.testIntegers);
        Integer min = Collections.min(this.testIntegers);
        Integer correctRange = max - min;
        assertEquals(correctRange.intValue(), range.intValue());
    }

    @Test
    public void sum() throws Exception {
        Double sum = StatisticOperators.sum(TestItem.X).apply(this.uqi, this.testItems);
        Integer correctSum = 0;
        for (Integer num : this.testIntegers) {
            correctSum += num;
        }
        assertEquals(correctSum.intValue(), sum.intValue());
    }

    @Test
    public void mean() throws Exception {
        Double mean = StatisticOperators.mean(TestItem.X).apply(this.uqi, this.testItems);
        Integer correctSum = 0;
        for (Integer num : this.testIntegers) {
            correctSum += num;
        }
        Double correctMean = correctSum.doubleValue() / NORMAL_COUNT;
        assertNotNull(correctMean);
        assertNotNull(mean);
        assertEquals(correctMean, mean, DELTA);
    }

    @Test
    public void rms() throws Exception {
        Double rms = StatisticOperators.rms(TestItem.X).apply(this.uqi, this.testItems);
        Double correctRMS = 0.0;
        for (Integer num : this.testIntegers) {
            correctRMS += num * num.doubleValue() / NORMAL_COUNT;
        }
        correctRMS = Math.sqrt(correctRMS);
        assertNotNull(correctRMS);
        assertNotNull(rms);
        assertEquals(correctRMS, rms, DELTA);
    }

    @Test
    public void variance() throws Exception {
        Double variance = StatisticOperators.variance(TestItem.X).apply(this.uqi, this.testItems);
        Integer correctSum = 0;
        for (Integer num : this.testIntegers) {
            correctSum += num;
        }
        Double correctMean = correctSum.doubleValue() / NORMAL_COUNT;
        Double correctVariance = 0.0;
        for (Integer num : this.testIntegers) {
            correctVariance += (num.doubleValue() - correctMean) * (num.doubleValue() - correctMean) / NORMAL_COUNT;
        }
        assertNotNull(correctVariance);
        assertNotNull(variance);
        assertEquals(correctVariance, variance, DELTA);
    }

    @Test
    public void max() throws Exception {
        Integer max = (Integer) StatisticOperators.max(TestItem.X).apply(this.uqi, this.testItems);
        Integer correctMax = Collections.max(this.testIntegers);
        assertEquals(correctMax.intValue(), max.intValue());
    }

    @Test
    public void min() throws Exception {
        Integer min = (Integer) StatisticOperators.min(TestItem.X).apply(this.uqi, this.testItems);
        Integer correctMin = Collections.min(this.testIntegers);
        assertEquals(correctMin.intValue(), min.intValue());
    }

    @Test
    public void median() throws Exception {
        Integer median = (Integer) StatisticOperators.median(TestItem.X).apply(this.uqi, this.testItems);
        List<Integer> sortedNumbers = new ArrayList<>(this.testIntegers);
        Collections.sort(sortedNumbers);
        Integer correctMedian = sortedNumbers.get((NORMAL_COUNT + 1) / 2);
        assertEquals(correctMedian.intValue(), median.intValue());
    }

    @Test
    public void mode() throws Exception {
        Integer mode = (Integer) StatisticOperators.mode(TestItem.X).apply(this.uqi, this.testItems);
        Map<Integer, Integer> numCount = new HashMap<>();
        for (Integer num : this.testIntegers) {
            numCount.put(num, 0);
        }
        for (Integer num : this.testIntegers) {
            int count = numCount.get(num);
            numCount.put(num, count + 1);
        }
        Integer correctMode = null;
        int maxCount = 0;
        for (Integer num : this.testIntegers) {
            int count = numCount.get(num);
            if (count > maxCount) {
                maxCount = count;
                correctMode = num;
            }
        }
        assertNotNull(correctMode);
        assertEquals(correctMode.intValue(), mode.intValue());
    }

}