package com.github.privacystreams.image;

import android.net.Uri;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * A function that processes the photo field in an item.
 */
abstract class ImageProcessor<Tout> extends ItemFunction<Tout> {

    private final String photoUriField;

    ImageProcessor(String photoUriField) {
        this.photoUriField = Assertions.notNull("photoUriField", photoUriField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String photoUriString = input.getValueByField(this.photoUriField);
        Uri photoUri = Uri.parse(photoUriString);
        return this.processPhoto(photoUri);
    }

    protected abstract Tout processPhoto(Uri photoUri);

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(this.photoUriField);
        return parameters;
    }
}
