package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * A BrowserHistory item represents a browser history entry.
 */

public class BrowserHistory extends Item {
    public static final String TITLE = "title";
    public static final String PACKAGE_NAME = "package_name";
    public static final String URL = "url";
    public static final String TIMESTAMP = "timestamp";

    public BrowserHistory(String title,
                          String packageName,
                          String url,
                          long timestamp) {
        this.setFieldValue(TITLE, title);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(URL, url);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new BrowserHistoryStreamProvider();
    }

}