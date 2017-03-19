package com.github.privacystreams.notification;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Created by fanglinchen on 3/16/17.
 */

/**
 * An Notification item represents a received notification.
 */
@PSItem
public class Notification extends Item {
    /**
     * The timestamp of the notification.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The action associated with the notification.
     * It could be removed or posted.
     */
    @PSItemField(type = String.class)
    public static final String ACTION = "action";

    public static final String ACTION_REMOVED = "removed";
    public static final String ACTION_POSTED = "posted";

    /**
     * The category of the notification.
     * It could be game, app, etc.
     */
    @PSItemField(type = String.class)
    public static final String CATEGORY = "category";

    /**
     * The package name of the notification.
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";


    /**
     * The title of the notification.
     */
    @PSItemField(type = String.class)
    public static final String NOTIFICATION_TITLE = "notification_title";

    /**
     * The text of the notification.
     */
    @PSItemField(type = String.class)
    public static final String NOTIFICATION_TEXT = "notification_text";


    public Notification(String category,
                        String packageName,
                        String notificationTitle,
                        String notificationText,
                        String action) {
        this.setFieldValue(TIMESTAMP, System.currentTimeMillis());

        this.setFieldValue(CATEGORY, category);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(NOTIFICATION_TITLE, notificationTitle);
        this.setFieldValue(NOTIFICATION_TEXT, notificationText);
        this.setFieldValue(ACTION,action);
    }

    /**
     * Provide a list of WifiAp items from WIFI scan result.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates() {
        return new BaseNotificationEventProvider();
    }
}
