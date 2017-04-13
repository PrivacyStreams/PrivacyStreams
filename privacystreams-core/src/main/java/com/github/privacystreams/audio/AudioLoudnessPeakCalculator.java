package com.github.privacystreams.audio;

import com.github.privacystreams.core.UQI;

/**
 * Calculate the peak loudness of an audio field.
 * The loudness is an double number indicating the sound pressure level in dB.
 */

class AudioLoudnessPeakCalculator extends AudioProcessor<Double> {

    AudioLoudnessPeakCalculator(String audioField) {
        super(audioField);
    }

    @Override
    protected Double processAudio(UQI uqi, AudioData audioData) {
        return audioData.getPeakLoudness(uqi);
    }

}
