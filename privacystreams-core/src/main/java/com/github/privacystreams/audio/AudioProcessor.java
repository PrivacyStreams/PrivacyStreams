package com.github.privacystreams.audio;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the audio field in an item.
 */
abstract class AudioProcessor<Tout> extends ItemFunction<Tout> {

    private final String audioDataField;

    AudioProcessor(String audioDataField) {
        this.audioDataField = Assertions.notNull("audioDataField", audioDataField);
        this.addParameters(audioDataField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        AudioData audioData = input.getValueByField(this.audioDataField);
        return this.processAudio(uqi, audioData);
    }

    protected abstract Tout processAudio(UQI uqi, AudioData audioData);

}
