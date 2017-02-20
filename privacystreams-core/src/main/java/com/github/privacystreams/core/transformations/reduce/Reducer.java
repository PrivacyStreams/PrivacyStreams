package com.github.privacystreams.core.transformations.reduce;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.transformations.M2STransformation;


/**
 * Created by yuanchun on 14/11/2016.
 * pick an item from the stream
 * return null if fails to find an item
 */

public abstract class Reducer extends M2STransformation {

    @Override
    protected Item applyInBackground(MultiItemStream stream) {
        Item result = stream.read();
        while (true) {
            Item item = stream.read();
            if (item == null) break;
            result = this.reduce(result, item);
        }
        return result;
    }

    protected abstract Item reduce(Item item1, Item item2);
}
