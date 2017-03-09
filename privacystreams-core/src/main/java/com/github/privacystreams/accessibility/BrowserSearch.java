package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

/**
 * A BrowserSearch item represents a browser search event.
 */

public class BrowserSearch extends Item {
    @ItemField(name="text", type = String.class, description = "The searched text.")
    public static final String TEXT = "text";

    @ItemField(name="timestamp", type = Long.class, description = "The timestamp of when the search event is happened.")
    public static final String TIMESTAMP = "timestamp";

    public BrowserSearch(String title,
                         long timestamp) {
        this.setFieldValue(TEXT, title);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new BrowserSearchUpdatesProvider();
    }

}