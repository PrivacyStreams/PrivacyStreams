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

public class BackupTasks {

    private final static String TAG = "Tasks";

    private Context context;
    public BackupTasks(Context context) {
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
     * Task 6: Getting the total length of text messages sent to each contact.
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
     * Once you get the result Map, please call `Evaluator.submitTask6(Map<String, Integer>)` method to submit.
     */
    void task6() {
        // Your code here.
    }
}
