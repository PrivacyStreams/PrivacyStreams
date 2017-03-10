package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A BrowserSearch item represents a browser search event.
 */
@PSItem
public class BrowserSearch extends Item {
    @PSItemField(name="text", type = String.class, description = "The searched text.")
    public static final String TEXT = "text";

    @PSItemField(name="timestamp", type = Long.class, description = "The timestamp of when the search event is happened.")
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