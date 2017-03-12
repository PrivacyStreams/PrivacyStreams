package com.github.privacystreams.audio;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access audio-related operators
 */
@PSOperatorWrapper
public class AudioOperators {
    /**
     * Calculate the loudness of an audio field. The loudness is an integer in dB.
     * @param audioUriField the name of the audio uri field.
     * @return the function
     */
    public static Function<Item, Integer> calcLoudness(String audioUriField) {
        return new AudioLoudnessCalculator(audioUriField);
    }
}
