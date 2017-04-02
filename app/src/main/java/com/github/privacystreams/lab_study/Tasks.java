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

public class Tasks {

    private final static String TAG = "Tasks";

    private Context context;
    public Tasks(Context context) {
        this.context = context;
    }

    /**
     * Task 0 (tutorial task): Getting audio loudness periodically.
     *
     * Suppose you are developing a sleep monitor app based on microphone loudness.
     * As a part of the app, in this task, you need to get microphone loudness periodically (every 10 minutes).
     * Each loudness value is measured in decibels (dB) with a 10-second duration.
     * Each time you get the Double-type loudness value, please call `Evaluator.submitTask0(Double)` method to submit.
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
     * Task 1: Getting all contact email addresses.
     *
     * Suppose you are developing a social network app (like Facebook).
     * As a part of the app, in this task, you are trying to get all contact email addresses on the device,
     * in order to recommend potential friends to the user.
     * The result should be a list of Strings, in which each element is an email address.
     * Note that a contact may have multiple email addresses.
     *
     * For example, if the user has two contacts, the first contact has one email address 'alice@email.com',
     * and the second contact has two email addresses 'bob@email.com' and 'bob@123.com',
     * then the result should be a list of three Strings: ['alice@email.com', 'bob@email.com', 'bob@123.com']
     *
     * After you get the result, please call `submitTask1(List<String>)` method to submit the result.
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
     * Once you get the result coordinates, please call `Evaluator.submitTask2(Double, Double)` method to submit.
     */
    void task2() {
        // Your code here.
    }

    /**
     * Task 3: Getting two-factor authentication code in next incoming text messages.
     *
     * Suppose your app uses two-factor authentication to verify user identities
     * by sending a 6-digit authentication code in an text message (like Facebook did for login).
     * As a part of the app, in this task, you want to automatically get the authentication code
     * from the incoming text messages (so that users do not have to copy & paste by themselves).
     * The authentication message will be from number "14008001234", and the text content will be:
     * "Your code is xxxxxx", where "xxxxxx" is the result String you need.
     *
     * For example, if the next SMS message is not from "14008001234", just ignore it and keep waiting.
     * If the next message is "Your code is 123456" from "14008001234", then "123456" should be the result.
     *
     * Once you get the String-type 6-digit code, please call `Evaluator.submitTask3(String)` to submit.
     */
    void task3() {
        // Your code here
    }

    /**
     * Task 4: Getting a list of image files.
     *
     * Suppose you are developing a photo-editing app (like Instagram).
     * As a part of the app, in this task, you are trying to get the file paths of all images in local storage.
     * The result should be a list of String values, and each value is an file path to an image.
     *
     * For example, if there are two images in storage, the result list might be
     * ["/sdcard/Photos/image1.jpg", "/sdcard/Photos/image2.jpg"].
     *
     * Once you get the result, please call `Evaluator.submitTask4(List<String>)` method to submit.
     */
    void task4() {
        // Your code here.
    }

    /**
     * Task 5: Getting notified when in a circular geofence.
     *
     * Suppose you are developing a location-based game that notifies the users when there is a monster nearby (like Pokemon GO).
     * As a part of the app, in this task, you are trying to continuously monitor location,
     * and notify users when they enter or leave a geofence.
     * The geofence is a circular region with center coordinate `C = (40.4435, -79.9435)` and radius `R = 20m`.
     *
     * You need to call `Evaluator.submitTask5(true)` when entering the region and `Evaluator.submitTask5(false)` when leaving the region.
     */
    void task5() {
        // Your code here.
    }
}
