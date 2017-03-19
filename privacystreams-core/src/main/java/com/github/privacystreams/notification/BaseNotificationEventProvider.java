package com.github.privacystreams.notification;


class BaseNotificationEventProvider extends NotificationEventProvider {

    public void handleNotificationEvent(String categoryName,
                                        String packageName,
                                        String titleName,
                                        String textName,
                                        String action){
            this.output(new Notification(categoryName, packageName, titleName,textName, action));
    }

}
