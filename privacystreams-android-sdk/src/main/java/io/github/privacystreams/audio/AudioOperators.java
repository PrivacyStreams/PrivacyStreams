package io.github.privacystreams.audio;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access audio-related operators
 */
@PSOperatorWrapper
public class AudioOperators {
    /**
     * Calculate the average (RMS) loudness of the audio specified by an AudioData field.
     * The loudness is an double number indicating the sound pressure level in dB.
     *
     * @param audioDataField the name of the AudioData field.
     * @return the function.
     */
    public static Function<Item, Double> calcLoudness(String audioDataField) {
        return new AudioLoudnessCalculator(audioDataField);
    }

    /**
     * Calculate the mel-frequence cepstral coefficients of the audio specified by an AudioData
     * field with default frame size 1024, 30 mel filters and 30 cepstral coefficients.
     * The MFCC is a list of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @return the function
     */
    public static Function<Item, List<Double[]>> calcMFCC(String audioDataField) {
        return new MFCC(audioDataField);
    }

    /**
     * Calculate the mel-frequence cepstral coefficients of the audio specified by an AudioData
     * field with 30 mel filters and 30 cepstral coefficients. The MFCC is a list of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @param frameSize the size of audio frames for analysis
     * @return the function
     */
    public static Function<Item, List<Double[]>> calcMFCC(String audioDataField, int frameSize) {
        return new MFCCWithFrameSize(audioDataField, frameSize);
    }

    /**
     * Calculate the mel-frequence cepstral coefficients of the audio specified by an AudioData
     * field. The MFCC is a list of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @param frameSize the size of audio frames for analysis
     * @param MelFilters the number of mel filters for MFCC analysis
     * @param CepstrumCoe the number of cepstral coefficients for analysis
     * @return the function
     */
    public static Function<Item, List<Double[]>> calcMFCC(String audioDataField, int frameSize,
                                                          int MelFilters, int CepstrumCoe) {
        return new MFCCWithParameters(audioDataField, frameSize, MelFilters, CepstrumCoe);
    }

    /**
     * calculate the frequency of the audio specified by an AudioData field.
     * The frequency is a list of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @return the function
     */

    public static Function<Item, List<Double>> calcFrequency(String audioDataField){
        return new Frequency(audioDataField);
    }

    /**
     * calculate the frequency of the audio specified by an AudioData field.
     * The frequency is a list of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @param frameSize the size of audio frames for analysis
     * @return the function
     */
    public static Function<Item, List<Double>> calcFrequency(String audioDataField, int frameSize){
        return new FrequencyWithFrameSize(audioDataField, frameSize);
    }
    /**
     * calculate the zero-crossing-rate of the audio specifies by an AudioData field.
     * The frequency is an array of doubles
     *
     * @param audioDataField the name of the AudioData field
     * @return the function
     */

    public static Function<Item, List<Double>> calcZeroCrossingRate(String audioDataField){
        return new ZeroCrossingRate(audioDataField);
    }

    /**
     * calculate the zero-crossing-rate of the audio specifies by an AudioData field.
     * The frequency is an array of doubles
     *
     * @param audioDataField the name of the AudioData field.
     * @param frameSize the size of audio frames for analysis
     * @return the function
     */

    public static Function<Item, List<Double>> calcZeroCrossingRate(String audioDataField,
                                                                    int frameSize){
        return new ZeroCrossingRateWithFrameSize(audioDataField, frameSize);
    }

    /**
     * Get the max amplitude of the audio specified by an AudioData field.
     * The amplitude is an Integer from 0 to 32767.
     *
     * @param audioDataField the name of the AudioData field.
     * @return the function.
     */
    public static Function<Item, Integer> getMaxAmplitude(String audioDataField) {
        return new AudioMaxAmplitudeGetter(audioDataField);
    }

    /**
     * Get the amplitude samples of the audio specified by an AudioData field.
     * Each amplitude sample is an Integer from 0 to 32767.
     * The amplitude sampling rate can be configured at `Globals.AudioConfig.amplitudeSamplingRate`;
     *
     * @param audioDataField the name of the AudioData field.
     * @return the function.
     */
    public static Function<Item, List<Integer>> getAmplitudeSamples(String audioDataField) {
        return new AudioAmplitudeSamplesGetter(audioDataField);
    }

    /**
     * Calculate loudness (in decibels) based on an amplitude value.
     * The amplitude should be a number from 0 to 32767.
     *
     * @param amplitudeField the name of the amplitude field.
     * @return the function.
     */
    public static Function<Item, Double> convertAmplitudeToLoudness(String amplitudeField) {
        return new AmplitudeToLoudnessConverter(amplitudeField);
    }

    /**
     * Get the file path of the audio specified by an AudioData field.
     * The path might point to a temporary audio file if it is not from storage.
     * To permanently save the file, you need to copy the file to another file path.
     *
     * @param audioDataField the name of AudioData field
     * @return the function
     */
    public static Function<Item, String> getFilepath(String audioDataField) {
        return new AudioFilepathGetter(audioDataField);
    }

    /**
     * Detect human voice from the audio.
     * Return true if there is human voice.
     *
     * @param audioDataField the name of AudioData field
     * @return the function
     */
    public static Function<Item, Boolean> hasHumanVoice(String audioDataField) {
        return new AudioVoiceDetector(audioDataField);
    }
}
