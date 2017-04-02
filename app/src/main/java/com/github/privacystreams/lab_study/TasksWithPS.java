package com.github.privacystreams.lab_study;

import android.content.Context;

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

    private Context context;
    public TasksWithPS(Context context) {
        this.context = context;
    }

    /**
     * Task 0 (tutorial task): Getting audio loudness periodically.
     *
     * Suppose you are developing an sleep monitor app based on microphone loudness.
     * In this task, you need to get microphone loudness periodically (every 30 minutes).
     * Each loudness value should be a double number indicating the SPL (sound pressure level in decibels) during 10 seconds.
     * To calculate loudness, use formula: `loudness = 20 * log(RMS_amplitude)`
     * Each time you get the loudness, please call `submitTask0(Double)` method to submit the result.
     * Note that emulators can't simulate microphone input, but it should be fine as this is a tutorial task.
     */
    void task0() {
        UQI uqi = new UQI(context);
        uqi
                .getData(Audio.recordPeriodic(10*1000, 30*60*1000), Purpose.TEST("Getting loudness."))
                .setField("loudness", AudioOperators.calcLoudness(Audio.AUDIO_DATA))
                .forEach("loudness", new Callback<Integer>() {
                    @Override
                    protected void onSuccess(Integer loudness) {
                        Evaluator.submitTask0(loudness);
                    }

                    @Override
                    protected void onFail(PSException exception) {
                        super.onFail(exception);
                    }
                });
    }


    /**
     * Task 1: Getting the total length of SMS messages for each contact.
     *
     * Suppose you are building an app to understand users' relationships with their friends.
     * In this task, you are trying to calculate the total length of SMS messages for each phone number.
     * The result should be a Map, where each key is a phone number in String format,
     * and the value should be an Integer indicating the total length of SMS messages with the phone number.
     * Once you get the result Map, please call `submitTask1(Map<String, Integer>)` method to submit the result.
     */
    void task1() {
        // Your code here.
    }

    /**
     * Task 2: Getting current location coordinates.
     *
     * Suppose you are building a weather app to help users check weather based on their location.
     * In this task, you are trying to get location coordinates of the user.
     * The result should be two double values that are the latitude and longitude of the user's current location.
     * After you get the result String, please call `submitTask2(Double, Double)` method to submit the task.
     */
    void task2() {
        // Your code here.
    }

    /**
     * Task 3: Getting next SMS message for two-factor authentication.
     *
     * In this task, you are trying to get the next SMS message for two-factor authentication.
     * The authentication message will be from phone number "123456789" and the text content will be:
     * "Dear participant, your authentication code is xxxxxx", where "xxxxxx" is the result you need to get.
     * After you get the authentication code, please call `submitTask3(String)` to submit the result.
     */
    void task3() {
        // Your code here
    }

    /**
     * Task 4: Getting a list of image files
     *
     * In this task, you are trying to get the file paths of all images in the sdcard.
     * The result should be a list of Strings, and each String in the list is an image file path.
     * After you get the result, please call `submitTask4(List<String>)` method to submit the result.
     */
    void task4() {
        // Your code here.
    }

    /**
     * Task 5: Getting notified when in an area.
     *
     * In this task, you are trying to continuously get device location updates and be notified
     * when entering or leaving a coordinate box [(10,10), (20,20)].
     * You need to call `submitTask5(1)` when entering the box and `submitTask5(0)` when leaving the box.
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
