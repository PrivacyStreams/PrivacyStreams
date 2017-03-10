package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A BrowserVisit item represents a browser visit event.
 */
@PSItem
public class BrowserVisit extends Item {
    @PSItemField(name="title", type = String.class, description = "The title of current web view.")
    public static final String TITLE = "title";

    @PSItemField(name="package_name", type = String.class, description = "The package name of used browser.")
    public static final String PACKAGE_NAME = "package_name";

    @PSItemField(name="url", type = String.class, description = "The URL of visited webpage.")
    public static final String URL = "url";

    @PSItemField(name="timestamp", type = Long.class, description = "The timestamp of when the web page is visited.")
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