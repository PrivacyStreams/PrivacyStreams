package com.github.privacystreams.audio;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * A helper class to access audio-related operators
 */

public class AudioOperators {
    /**
     * calculate the loudness of an audio field.
     * the loudness is an integer in dB.
     * @param audioUriField the name of the audio uri field.
     * @return the function
     */
    public static Function<Item, Integer> calcLoudness(String audioUriField) {
        return new AudioLoudnessCalculator(audioUriField);
    }
}
