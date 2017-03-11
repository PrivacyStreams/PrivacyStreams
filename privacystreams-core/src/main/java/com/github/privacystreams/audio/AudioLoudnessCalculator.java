package com.github.privacystreams.audio;

import android.net.Uri;

/**
 * Created by yuanchun on 28/12/2016.
 * calculate the loudness of an audio field.
 * the loudness is an integer in dB.
 */

class AudioLoudnessCalculator extends AudioProcessor<Integer> {

    AudioLoudnessCalculator(String audioField) {
        super(audioField);
    }

    @Override
    protected Integer processAudio(Uri audioUri) {
        // TODO calculate loudness here
        return null;
    }

}
