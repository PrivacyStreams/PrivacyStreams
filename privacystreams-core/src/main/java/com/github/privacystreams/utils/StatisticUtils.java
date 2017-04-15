package com.github.privacystreams.utils;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A helper class to access statistic-related functions.
 */

public class StatisticUtils {
    private static final String LOG_TAG = "StatisticUtils - ";

    /**
     * Get the mean of all numbers in the list.
     * @param numList a list of numbers
     * @return the mean, in Double
     */
    public static Double mean(List<? extends Number> numList) {
        int validLength = validLength(numList);
        if (validLength == 0) return null;
        Double sum = sum(numList);
        if (sum == null) return null;
        return sum / validLength;
    }

    /**
     * Get the median of all numbers in the list.
     * @param numList a list of numbers
     * @return the median
     */
    public static <T extends Number> T median(List<T> numList) {
        int validLength = validLength(numList);
        if (validLength == 0) return null;
        List<T> copyNumList = new ArrayList<>(numList);
        Collections.sort(copyNumList, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1 == null) return -1;
                else if (o2 == null) return 1;
                double comp = o1.doubleValue() - o2.doubleValue();
                return comp > 0 ? 1 : -1;
            }
        });
        int medianId = (validLength + 1) / 2;
        int id = 0;
        for (T num : copyNumList) {
            if (num == null) continue;
            id++;
            if (id == medianId) return num;
        }
        return null;
    }

    /**
     * Get the mode of all numbers in the list.
     * @param numList a list of numbers
     * @return the mode
     */
    public static <T extends Number> T mode(List<T> numList) {
        int validLength = validLength(numList);
        if (validLength == 0) return null;
        SparseIntArray hashcode2Count = new SparseIntArray();
        SparseArray<T> hashcode2Num = new SparseArray<>();
        for (T num : numList) {
            if (num == null) continue;
            int hashcode = HashUtils.valueHash(num);
            if (hashcode2Count.indexOfKey(hashcode) < 0) {
                hashcode2Count.put(hashcode, 1);
                hashcode2Num.put(hashcode, num);
            }
            else {
                int count = hashcode2Count.get(hashcode);
                hashcode2Count.put(hashcode, count + 1);
            }
        }
        T mode = null;
        int maxCount = 0;
        for (int i = 0; i < hashcode2Count.size(); i++) {
            int count = hashcode2Count.valueAt(i);
            if (count > maxCount) {
                int hashcode = hashcode2Count.keyAt(i);
                mode = hashcode2Num.get(hashcode);
                maxCount = count;
            }
        }
        return mode;
    }

    /**
     * Get the sum of all numbers in the list.
     * @param numList a list of numbers
     * @return the sum, in Double.
     */
    public static Double sum(List<? extends Number> numList) {
        if (validLength(numList) == 0) return null;
        Double result = 0.0;
        for (Number num : numList) {
            if (num == null) continue;
            result += num.doubleValue();
        }
        return result;
    }

    /**
     * Get the max of all numbers in the list.
     * @param numList a list of numbers
     * @return the max number, in Number.
     */
    public static <T extends Number> T max(List<T> numList) {
        if (validLength(numList) == 0) return null;
        T result = null;
        for (T num : numList) {
            if (num == null) continue;
            if (result == null) result = num;
            else if (num.doubleValue() > result.doubleValue()) result = num;
        }
        return result;
    }

    /**
     * Get the min of all numbers in the list.
     * @param numList a list of numbers
     * @return the min number, in Number.
     */
    public static <T extends Number> T min(List<T> numList) {
        if (validLength(numList) == 0) return null;
        T result = null;
        for (T num : numList) {
            if (num == null) continue;
            if (result == null) result = num;
            else if (num.doubleValue() < result.doubleValue()) result = num;
        }
        return result;
    }

    /**
     * Get the range of all numbers in the list.
     * @param numList a list of numbers
     * @return the range of numbers
     */
    public static <T extends Number> Double range(List<T> numList) {
        if (validLength(numList) == 0) return null;
        T max = max(numList);
        T min = min(numList);
        if (max == null || min == null) return null;
        return max.doubleValue() - min.doubleValue();
    }

    /**
     * Get the RMS (root mean square) of all numbers in the list.
     * @param numList a list of numbers
     * @return the RMS of numbers
     */
    public static <T extends Number> Double rms(List<T> numList) {
        int len = validLength(numList);
        if (len == 0) return null;
        double rms = 0;
        for (T num : numList) {
            if (num == null) continue;
            rms += num.doubleValue() / len * num.doubleValue();
        }
        return Math.sqrt(rms);
    }

    /**
     * Get the variance of all numbers in the list.
     * @param numList a list of numbers
     * @return the variance of numbers
     */
    public static <T extends Number> Double variance(List<T> numList) {
        int len = validLength(numList);
        if (len == 0) return null;
        Double mean = mean(numList);
        if (mean == null) return null;
        double var = 0;
        for (T num : numList) {
            if (num == null) continue;
            double temp = num.doubleValue() - mean;
            var += temp * temp / len;
        }
        return var;
    }

    /**
     * Get the length of valid numbers.
     * @param numList a list of numbers.
     * @return the length, in int.
     */
    public static <T extends Number> int validLength(List<T> numList) {
        int length = 0;
        for (T num : numList) {
            if (num == null) continue;
            length++;
        }
        return length;
    }

}
