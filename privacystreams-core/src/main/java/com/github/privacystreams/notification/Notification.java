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
                if(mNotification.extras.getString(android.app.Notification.EXTRA_TITLE)!=null){
                    String title = mNotification.extras.getString(android.app.Notification.EXTRA_TITLE);
                    this.setFieldValue(TITLE, title);
                }
                if( mNotification.extras.getString(android.app.Notification.EXTRA_TEXT)!=null){
                    String text = mNotification.extras.getString(android.app.Notification.EXTRA_TEXT);
                    this.setFieldValue(TEXT, text);
                }

            }
            // Get whatsapp detailed information
            if(sbn.getPackageName().equals(AppUtils.APP_PACKAGE_WHATSAPP)){

                String title = mNotification.extras.getString(android.app.Notification.EXTRA_TITLE);
                String category = mNotification.extras.getString(android.app.Notification.EXTRA_TEXT);

                if(title!=null&&category!=null){
                    if(category.equals("msg")&&title.equals("WhatsApp")){
                        dumpStatusBarNotification(sbn);
                        this.setFieldValue(TITLE,contactName);
                    }
                }

            }
        }
    }

    /**
     * Provide a list of WifiAp items from WIFI scan result.
     * @return the provider function.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    // @RequiresPermission(value = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
    public static MStreamProvider asUpdates() {
        return new NotificationUpdatesProvider();
    }
    public void dumpStatusBarNotification(StatusBarNotification sbn) {
        dumpUserHandle(sbn);
        android.app.Notification notification = sbn.getNotification();
        if (notification != null) {
            dumpExtras(notification);
            dumpNotificationActions(notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void dumpUserHandle(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UserHandle userHandle = sbn.getUser();
            if (userHandle != null) {
                //Log.d("Test","User handle:"+ userHandle.toString());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void dumpNotificationActions(android.app.Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            android.app.Notification.Action[] actions = notification.actions;
            if (actions != null) {
                for (android.app.Notification.Action action : actions) {
                    //Log.d("TEST", "Action title: "+action.title);
                    dumpExtras(action);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void dumpExtras(android.app.Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dumpExtras(notification.extras);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void dumpExtras(android.app.Notification.Action action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            dumpExtras(action.getExtras());
        }
    }

    private void dumpExtras(Bundle extras) {
        if (extras != null) {
            for (String k : extras.keySet()) {
                Object o = extras.get(k);
                if (o instanceof CharSequence[]) {
                    // case for "textLines" and such
                    CharSequence n = "";
                    CharSequence[] data = (CharSequence[]) o;
                    for (CharSequence d : data) {
//                        Log.d("Test", k +" => "+ d);
                        n = d;
                    }
                    contactName = n.toString();
                    int in = contactName.indexOf(": ");
                    contactName = contactName.substring(0,in);
//                    Log.e("Test for last one","Contact name "+ contactName);
//                    Log.e("Test for Index","Index "+ in);
                } else {
                    //Log.d("Test2",k +" => "+ o);
                }
            }
        }
    }


}
