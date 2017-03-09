package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

/**
 * A BrowserVisit item represents a browser visit event.
 */

public class BrowserVisit extends Item {
    @ItemField(name=TITLE, type = String.class, description = "The title of current web view.")
    public static final String TITLE = "title";

    @ItemField(name=PACKAGE_NAME, type = String.class, description = "The package name of used browser.")
    public static final String PACKAGE_NAME = "package_name";

    @ItemField(name=URL, type = String.class, description = "The URL of visited webpage.")
    public static final String URL = "url";

    @ItemField(name=TIMESTAMP, type = Long.class, description = "The timestamp of when the web page is visited.")
    public static final String TIMESTAMP = "timestamp";

    public BrowserVisit(String title,
                        String packageName,
                        String url,
                        long timestamp) {
        this.setFieldValue(TITLE, title);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(URL, url);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new BrowserVisitStreamProvider();
    }

}