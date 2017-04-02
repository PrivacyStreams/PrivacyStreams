package com.github.privacystreams.lab_study;

import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The evaluator of the programming tasks in lab study.
 */

public class Evaluator {
    private static final String TAG = "Evaluator";

    public static void submitTask0(Double loudness) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "Current loudness is %.1f dB", loudness);
        Log.d(TAG, message);
    }

    public static void submitTask1(List<String> emails) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "Contact emails: %s", emails.toString());
        Log.d(TAG, message);
    }

    public static void submitTask2(Double latitude, Double longitude) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "Last known location is (%.4f, %.4f)", latitude, longitude);
        Log.d(TAG, message);
    }

    public static void submitTask3(String authCode) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "Authentication code is %s", authCode);
        Log.d(TAG, message);
    }

    public static void submitTask4(List<String> imageFilepaths) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "Local images: %s", imageFilepaths.toString());
        Log.d(TAG, message);
    }

    public static void submitTask5(Boolean enterRegion) {
        // Grade the result.
        String message = String.format(Locale.getDefault(), "%s the region", enterRegion? "entered" : "left");
        Log.d(TAG, message);
    }
}
