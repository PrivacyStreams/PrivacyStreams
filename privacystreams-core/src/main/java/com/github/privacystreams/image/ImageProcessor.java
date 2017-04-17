package com.github.privacystreams.image;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the photo field in an item.
 */
abstract class ImageProcessor<Tout> extends ItemFunction<Tout> {

    private final String imageDataField;

    ImageProcessor(String imageDataField) {
        this.imageDataField = Assertions.notNull("imageDataField", imageDataField);
        this.addParameters(imageDataField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        ImageData imageData = input.getValueByField(this.imageDataField);
        return this.processImage(uqi, imageData);
    }

    protected abstract Tout processImage(UQI uqi, ImageData imageData);
}
