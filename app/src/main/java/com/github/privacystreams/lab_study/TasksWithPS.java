package com.github.privacystreams.lab_study;

import android.content.Context;
import android.util.Log;

import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.audio.AudioOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;

/**
 * The programming tasks to use in the lab study.
 */

public class TasksWithPS {

    private final static String TAG = "TasksWithPS";

    private Context context;
    public TasksWithPS(Context context) {
        this.context = context;
    }

    /**
     * Task 0 (tutorial task): Getting audio loudness periodically.
     *
     * Suppose you are developing a sleep monitor app based on microphone loudness.
     * As a part of the app, in this task, you need to get microphone loudness periodically (every 10 minutes).
     * Each loudness value is measured in decibels (dB) with a 10-second duration.
     * Each time you get the Double-type loudness value, please call `Evaluator.submitTask0(Double)` method to submit the result.
     * Note that emulators can't simulate microphone input, but it should be fine as this is a tutorial task.
     */
    void task0() {
        UQI uqi = new UQI(context);
        uqi
                .getData(Audio.recordPeriodic(10*1000, 10*60*1000), Purpose.TEST("Monitoring sleep."))
                .setField("loudness", AudioOperators.calcLoudness(Audio.AUDIO_DATA))
                .forEach("loudness", new Callback<Double>() {
                    @Override
                    protected void onSuccess(Double loudness) {
                        Evaluator.submitTask0(loudness);
                    }

                    @Override
                    protected void onFail(PSException exception) {
                        Log.e(TAG, "Getting loudness failed.");
                    }
                });
    }


    /**
     * Task 1: Getting the total length of text messages sent to each contact.
     *
     * Suppose you are building an app to infer users' relationships with their friends.
     * As a part of the app, in this task, you are trying to calculate the total length of text messages sent to each phone number.
     * The result should be a Map, where each key is a phone number in String,
     * and the value should be the total length of messages sent to the phone number in Integer.
     *
     * For example, if the user has two messages sent to number "123456",
     * the first message has 10 characters and the second has 20 characters.
     * Then the result Map should have a key "123456" and the value should be 30 (10+20).
     *
     * Once you get the result Map, please call `Evaluator.submitTask1(Map<String, Integer>)` method to submit the result.
     */
    void task1() {
        // Your code here.
    }

    /**
     * Task 2: Getting last known location coordinates.
     *
     * Suppose you are building a weather app to provide local weather information.
     * As a part of the app, in this task, you are trying to get location coordinates in order to check weather.
     * The result should be two Double numbers.
     * The first number is the latitude of the user's last known location, and the second number is the longitude.
     *
     * For example, if the user is in New York City, a possible location can be (40.7128, -74.0059).
     *
     * Once you get the result coordinates, please call `submitTask2(Double, Double)` method to submit the task.
     */
    void task2() {
        // Your code here.
    }

    /**
     * Task 3: Getting two-factor authentication code in next incoming text messages.
     *
     * Suppose your app uses two-factor authentication to verify user identities
     * by sending a 6-digit authentication code in an text message.
     * As a part of the app, in this task, you need to wait for an incoming authentication message
     * and get the authentication code in the message.
     * The authentication message will be from number "123456", and the text content will be:
     * "Your code is xxxxxx", where "xxxxxx" is the result String you need.
     *
     * For example, if the next SMS message is not from "123456", just ignore it and keep waiting.
     * If the next message is "Your code is abcdef" from "123456", then "abcdef" should be the result.
     *
     * Once you get the authentication code, please call `submitTask3(String)` to submit the result.
     */
    void task3() {
        // Your code here
    }

    /**
     * Task 4: Getting a list of image files.
     *
     * Suppose you are developing a photo-editing app.
     * As a part of the app, in this task, you are trying to get the file paths of all images in local storage.
     * The result should be a list of String values, and each value is an file path to an image.
     *
     * For example, if there are two images in storage, the result list might be
     * ["/sdcard/Photos/image1.jpg", "/sdcard/Photos/image2.jpg"].
     *
     * Once you get the result, please call `submitTask4(List<String>)` method to submit the result.
     */
    void task4() {
        // Your code here.
    }

    /**
     * Task 5: Getting notified when in a circular geofence.
     *
     * Suppose you are developing a location-based game that notifies the users when there is a monster nearby (like Pokemon GO).
     * As a part of the app, in this task, you are trying to continuously monitor location,
     * in order to notify users when they enter or leave a circular area.
     *
     * For example,
     *
     * You need to call `submitTask5(1)` when entering the area and `submitTask5(0)` when leaving the area.
     */
    void task5() {
        // Your code here.
    }

    /**
     * Task 6: Getting all contact email addresses.
     *
     * In this task, you are trying to get a list of all contacts' email addresses on the device.
     * The result should be a list of Strings, in which each element is an email address.
     * After you get the result, please call `submitTask6(List<String>)` method to submit the result.
     */
    void task6() {
        // Your code here.
    }
}
