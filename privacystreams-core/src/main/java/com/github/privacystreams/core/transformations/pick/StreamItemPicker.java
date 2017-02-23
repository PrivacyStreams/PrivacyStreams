package com.github.privacystreams.core.transformations.pick;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.transformations.M2STransformation;


/**
 * Created by yuanchun on 14/11/2016.
 * pick an item from the stream
 * return null if fails to find an item
 */

class StreamItemPicker extends M2STransformation {
    private final int itemIndex;

    StreamItemPicker(int itemIndex) {
        this.itemIndex = itemIndex;
        this.addParameters(itemIndex);
    }

    @Override
    protected Item applyInBackground(MultiItemStream stream) {
        int itemCount = 0;
        while (true) {
            Item item = stream.read();
            if (itemCount == this.itemIndex) return item;
            if (item == null) return null;
            itemCount++;
        }
    }
}
