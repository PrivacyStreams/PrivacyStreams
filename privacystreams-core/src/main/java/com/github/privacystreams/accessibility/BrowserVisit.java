package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A website visit event.
 */
@PSItem
public class BrowserVisit extends Item {

    /**
     * The title of current webpage.
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";

    /**
     * The package name of the browser used to visit webpage.
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The URL of the visited website.
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

    /**
     * Provide a live stream of BrowserVisit items.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates(){
        return new BrowserVisitStreamProvider();
    }

}