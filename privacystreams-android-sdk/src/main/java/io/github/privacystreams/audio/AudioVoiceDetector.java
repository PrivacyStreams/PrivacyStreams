package io.github.privacystreams.audio;

import io.github.privacystreams.core.UQI;

/**
 * Detect human voice from the audio.
 * Return true if there is human voice.
 */

class AudioVoiceDetector extends AudioProcessor<Boolean> {

    AudioVoiceDetector(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected Boolean processAudio(UQI uqi, AudioData audioData) {
        return audioData.hasHumanVoice(uqi);
    }

}
