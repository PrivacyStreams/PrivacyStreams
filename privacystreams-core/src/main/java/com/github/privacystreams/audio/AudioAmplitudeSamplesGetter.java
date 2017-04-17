package com.github.privacystreams.audio;

import com.github.privacystreams.core.UQI;

import java.util.List;

/**
 * Get the amplitude samples of the audio in an audio field.
 */

class AudioAmplitudeSamplesGetter extends AudioProcessor<List<Integer>> {

    AudioAmplitudeSamplesGetter(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected List<Integer> processAudio(UQI uqi, AudioData audioData) {
        return audioData.getAmplitudeSamples();
    }

}
