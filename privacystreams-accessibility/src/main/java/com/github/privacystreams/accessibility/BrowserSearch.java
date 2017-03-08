package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by fanglinchen on 3/5/17.
 */

public class BrowserSearch extends Item {
    public static final String TEXT = "title";
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