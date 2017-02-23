package com.github.privacystreams.audio;

import android.net.Uri;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * Process the audio field in an item.
 */
abstract class AudioProcessor<Tout> extends ItemFunction<Tout> {

    private final String audioUriField;

    AudioProcessor(String audioUriField) {
        this.audioUriField = Assertions.notNull("audioUriField", audioUriField);
        this.addParameters(audioUriField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String audioUriString = input.getValueByField(this.audioUriField);
        Uri audioUri = Uri.parse(audioUriString);
        return this.processAudio(audioUri);
    }

    protected abstract Tout processAudio(Uri audioUri);

}
