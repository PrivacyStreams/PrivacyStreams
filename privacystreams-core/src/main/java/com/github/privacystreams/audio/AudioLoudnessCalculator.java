package com.github.privacystreams.audio;

import android.net.Uri;

/**
 * Created by yuanchun on 28/12/2016.
 * calculate the loudness of an audio field.
 * the loudness is an double number indicating the sound pressure level in dB.
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
