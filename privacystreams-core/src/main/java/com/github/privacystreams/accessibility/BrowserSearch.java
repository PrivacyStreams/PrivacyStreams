package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A browser search activity.
 */
@PSItem
public class BrowserSearch extends Item {
    /**
     * The searched text.
     */
    @PSItemField(type = String.class)
    public static final String TEXT = "text";

    /**
     * The timestamp of when the search event is happened.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    BrowserSearch(String title, long timestamp) {
        this.setFieldValue(TEXT, title);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    /**
     * Provide a live stream of BrowserSearch items.
     * @return the provider function
     */
    public static MStreamProvider asUpdates(){
        return new BrowserSearchUpdatesProvider();
    }

}