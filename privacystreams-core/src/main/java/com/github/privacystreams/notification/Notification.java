package com.github.privacystreams.notification;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.AppUtils;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An Notification item represents a received notification.
 */
@PSItem
public class Notification extends Item {


    /**
     * The timestamp of when the notification was posted.
     */
    @PSItemField(type = Long.class)
    public static final String POST_TIME = "post_time";

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

    /**
     * The extra bundle of the notification.
     */
    @PSItemField(type = Bundle.class)
    public static final String EXTRA = "extra";

    private String contactName = null;
    Notification(long postTime, String packageName, String category, String title, String text, String action) {
        this.setFieldValue(POST_TIME, postTime);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CATEGORY, category);
        this.setFieldValue(TITLE, title);
        this.setFieldValue(TEXT, text);
        this.setFieldValue(ACTION, action);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    Notification(StatusBarNotification sbn, String action) {
        this.setFieldValue(POST_TIME, sbn.getPostTime());
        this.setFieldValue(PACKAGE_NAME, sbn.getPackageName());
        this.setFieldValue(ACTION, action);

        android.app.Notification mNotification = sbn.getNotification();
        if (mNotification != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                String category = mNotification.category;
                this.setFieldValue(CATEGORY, category);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    String title = mNotification.extras.getString(android.app.Notification.EXTRA_TITLE);
                    this.setFieldValue(TITLE, title);
                    String text = mNotification.extras.getString(android.app.Notification.EXTRA_TEXT);
                    this.setFieldValue(TEXT, text);
                    this.setFieldValue(EXTRA,mNotification.extras);
            }
        }
    }

    /**
     * Provide a list of Notification items from Notification catch result.
     * @return the provider function.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    // @RequiresPermission(value = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
    public static MStreamProvider asUpdates() {
        return new NotificationUpdatesProvider();
    }
}
