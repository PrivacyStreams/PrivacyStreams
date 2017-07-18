package io.github.privacystreams.audio;

import io.github.privacystreams.core.UQI;

/**
 * Calculate the max amplitude of an audio field.
 */

class AudioMaxAmplitudeGetter extends AudioProcessor<Integer> {

    AudioMaxAmplitudeGetter(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected Integer processAudio(UQI uqi, AudioData audioData) {
        return audioData.getMaxAmplitude(uqi);
    }

}
