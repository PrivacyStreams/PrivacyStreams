package io.github.privacystreams.notification;

import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

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
     * The subtext of the notification.
     */
    @PSItemField(type = String.class)
    public static final String SUB_TEXT = "sub_text";

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
                    String title = "" + mNotification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE);
                    this.setFieldValue(TITLE, title);
                    String text = "" + mNotification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
                    this.setFieldValue(TEXT, text);
                    String subText = "" + mNotification.extras.getCharSequence(android.app.Notification.EXTRA_SUB_TEXT);
                    this.setFieldValue(SUB_TEXT, subText);
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
    public static PStreamProvider asUpdates() {
        return new NotificationUpdatesProvider();
    }
}
