package io.github.privacystreams.audio;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Calculate the max amplitude of an audio field.
 */

class AmplitudeToLoudnessConverter extends ItemOperator<Double> {

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
