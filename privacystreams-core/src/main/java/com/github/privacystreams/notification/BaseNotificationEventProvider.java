package com.github.privacystreams.notification;

/**
 * @author toby
 * @date 2/28/17
 * @time 11:23 AM
 */
class BaseNotificationEventProvider extends NotificationEventProvider {

    public void handleNotificationEvent(String categoryName,
                                        String packageName,
                                        String titleName,
                                        String textName){
            this.output(new Notification(categoryName, packageName, titleName,textName));
    }
}
