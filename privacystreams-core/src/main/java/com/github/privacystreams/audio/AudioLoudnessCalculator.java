package com.github.privacystreams.audio;

import android.net.Uri;

/**
 * Calculate the loudness of an audio field.
 * The loudness is an double number indicating the sound pressure level in dB.
 */

class AudioLoudnessCalculator extends AudioProcessor<Double> {

    AudioLoudnessCalculator(String audioField) {
        super(audioField);
    }

    @Override
    protected Double processAudio(AudioData audioData) {
        return audioData.getLoudness();
    }

}
