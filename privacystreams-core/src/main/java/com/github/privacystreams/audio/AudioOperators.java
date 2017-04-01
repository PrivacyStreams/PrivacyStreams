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
     * Calculate the average (RMS) loudness of the audio specified by an AudioData field.
     * The loudness is an double number indicating the sound pressure level in dB.
     *
     * @param audioField the name of the audio data field.
     * @return the function.
     */
    public static Function<Item, Double> calcLoudness(String audioField) {
        return new AudioLoudnessCalculator(audioField);
    }

    /**
     * Calculate the peak (max) loudness of the audio specified by an AudioData field.
     * The loudness is an double number indicating the sound pressure level in dB.
     *
     * @param audioField the name of the audio data field.
     * @return the function.
     */
    public static Function<Item, Double> calcPeakLoudness(String audioField) {
        return new AudioPeakLoudnessCalculator(audioField);
    }

    /**
     * Get the max amplitude of the audio specified by an AudioData field.
     * The amplitude is an Integer from 0 to 32767.
     *
     * @param audioField the name of the audio data field.
     * @return the function.
     */
    public static Function<Item, Integer> getMaxAmplitude(String audioField) {
        return new AudioMaxAmplitudeGetter(audioField);
    }

    /**
     * Get the file path of the audio specified by an AudioData field.
     * The path might point to a temporary audio file if it is not from storage.
     * To permanently save the file, you need to copy the file to another file path.
     *
     * @param audioField the name of photo field
     * @return the function
     */
    public static Function<Item, String> getFilepath(String audioField) {
        return new AudioFilepathGetter(audioField);
    }
}
