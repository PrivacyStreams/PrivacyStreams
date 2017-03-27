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
     * Calculate the loudness of an audio field.
     * The loudness is an double number indicating the sound pressure level in dB.
     *
     * @param audioField the name of the audio data field.
     * @return the function.
     */
    public static Function<Item, Double> calcLoudness(String audioField) {
        return new AudioLoudnessCalculator(audioField);
    }
}
