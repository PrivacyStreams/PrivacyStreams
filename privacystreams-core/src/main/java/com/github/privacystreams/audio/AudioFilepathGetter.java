package com.github.privacystreams.audio;

import com.github.privacystreams.core.UQI;

/**
 * Get the file path of the audio in an AudioData field.
 */
class AudioFilepathGetter extends AudioProcessor<String> {

    AudioFilepathGetter(String photoField) {
        super(photoField);
    }

    @Override
    protected String processAudio(UQI uqi, AudioData audioData) {
        return audioData.getFilepath(uqi);
    }

}
