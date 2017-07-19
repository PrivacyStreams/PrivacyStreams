package io.github.privacystreams.accessibility;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Website visiting event.
 */
@PSItem
public class BrowserVisit extends Item {

    /**
     * The title of current webpage.
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";

    /**
     * The package name of the browser used to visit the webpage.
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The URL of the visited website.
     */
    @PSItemField(type = String.class)
    public static final String URL = "url";

    BrowserVisit(String title, String packageName, String url) {
        this.setFieldValue(TITLE, title);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(URL, url);
    }

    /**
     * Provide a live stream of BrowserVisit items.
     * An item will be generated once the user visit a website in the browser.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
    public static PStreamProvider asUpdates(){
        return new BrowserVisitEventsProvider();
    }

}
