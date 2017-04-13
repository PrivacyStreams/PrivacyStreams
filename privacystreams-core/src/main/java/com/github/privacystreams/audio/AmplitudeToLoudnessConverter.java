package com.github.privacystreams.audio;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Calculate the max amplitude of an audio field.
 */

class AmplitudeToLoudnessConverter extends ItemFunction<Double> {

    private final String amplitudeField;

    AmplitudeToLoudnessConverter(String amplitudeField) {
        this.amplitudeField = Assertions.notNull("amplitudeField", amplitudeField);
        this.addParameters(amplitudeField);
    }

    @Override
    public Double apply(UQI uqi, Item item) {
        Number amplitude = item.getValueByField(this.amplitudeField);
        return AudioData.convertAmplitudeToLoudness(uqi, amplitude);
    }

}
