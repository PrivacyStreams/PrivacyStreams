package com.github.privacystreams.lab_study;

import android.content.Context;

import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.audio.AudioOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
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
     * Note that emulators can't simulate microphone input, but it should be fine as this is a tutorial task.
     * In this task, you are trying to measure the loudness level during the next 10 seconds.
     * The loudness should be calculated as the max amplitude.
     * Every time you get the result, please call submitTask3(int) method to submit the result.
     */
    void task0() throws PrivacyStreamsException {
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
                    protected void onFail(PrivacyStreamsException exception) {
                        super.onFail(exception);
                    }
                });
    }


    /**
     * Task 1: Getting the number of calls for each contact.
     *
     * TODO: This example is used in PrivacyStreams documentation, can change to a similar task accessing SMS.
     *
     * In this task, you are trying to get the number of calls for each contacted phone number.
     * The result should be a Map, where each key is a phone number in String format,
     * and the value is the number of calls with the phone number in Integer.
     * After you get the result, please call submitTask1(Map<String, Integer>) method to submit the result.
     */
    void task1() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 2: Getting current city name.
     *
     * In this task, you are trying to get the name of they city where the device is currently in.
     * The result should be the city name in String format.
     * After you get the result, please call submitTask2(String) method to submit the task.
     */
    void task2() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 3: Getting the phone number with the most outgoing messages.
     *
     * In this task, you want to know who the user sent the most SMS messages to.
     */
    void task3() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 4: Getting a list of image files
     *
     * In this task, you are trying to get the file paths of all images in the sdcard.
     * The result should be a list of Strings, and each String in the list is an image file path.
     * After you get the result, please call submitTask4(List<String>) method to submit the result.
     */
    void task4() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 5: Getting notified when in an area.
     *
     * In this task, you are trying to continuously get device location updates and be notified
     * when entering or leaving a coordinate box [(10,10), (20,20)].
     * You need to call submitTask5(1) when entering the box and submitTask5(0) when leaving the box.
     */
    void task5() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 6: Getting a list of all contacts' email addresses.
     *
     * In this task, you are trying to get a list of all contacts' email addresses on the device.
     * The result should be a list of Strings, in which each element is an email address.
     * After you get the result, please call submitTask6(List<String>) method to submit the result.
     */
    void task6() throws PrivacyStreamsException {
        // Your code here.
    }

    /**
     * Task 7: Getting next SMS message for two-factor authentication.
     *
     * In this task, you are trying to get the next SMS message for two-factor authentication.
     * The authentication message will be from phone number "123456789" and the text content will be:
     * "Dear participant, your authentication code is xxxxxx", where "xxxxxx" is the result you need to get.
     * After you get the authentication code, please call submitTask7(String) to submit the result.
     */
    void task7() throws PrivacyStreamsException {
        // Your code here
    }
}
