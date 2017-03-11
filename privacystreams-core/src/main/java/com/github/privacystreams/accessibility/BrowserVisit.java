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

    /**
     * The title of current web view.
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";

    /**
     * The package name of used browser.
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The URL of visited webpage.
     */
    @PSItemField(type = String.class)
    public static final String URL = "url";

    /**
     * The timestamp of when the web page is visited.
     */
    @PSItemField(type = Long.class)
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