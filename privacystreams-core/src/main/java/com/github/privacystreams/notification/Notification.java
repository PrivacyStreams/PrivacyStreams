package com.github.privacystreams.notification;

import android.Manifest;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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
     * It could be "removed" or "posted".
     */
    @PSItemField(type = String.class)
    public static final String ACTION = "action";

    /** Notification removed action. */
    public static final String ACTION_REMOVED = "removed";
    /** Notification posted action. */
    public static final String ACTION_POSTED = "posted";

    /**
     * The category of the notification.
     * One of the predefined notification categories
     * (see the `CATEGORY_*` constants in `android.app.Notification` class.)
     * that best describes this Notification.
     * Such as "sys", "social", etc.
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
    public static final String TITLE = "title";

    /**
     * The text of the notification.
     */
    @PSItemField(type = String.class)
    public static final String TEXT = "text";


    public Notification(String category,
                        String packageName,
                        String notificationTitle,
                        String notificationText,
                        String action) {
        this.setFieldValue(TIMESTAMP, System.currentTimeMillis());
        this.setFieldValue(CATEGORY, category);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(TITLE, notificationTitle);
        this.setFieldValue(TEXT, notificationText);
        this.setFieldValue(ACTION,action);
    }

    /**
     * Provide a list of WifiAp items from WIFI scan result.
     * @return the provider function.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    // @RequiresPermission(value = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
    public static MStreamProvider asUpdates() {
        return new BaseNotificationEventProvider();
    }
}
