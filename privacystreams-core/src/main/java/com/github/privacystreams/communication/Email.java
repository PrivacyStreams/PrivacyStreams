package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * It includes all kinds of email that could be received by phones, currently it only contains gmail.
 * Created by lenovo on 2017/6/2.
 */
@PSItem
public class Email extends Item {
    /**
     * The email body.
     */

    @PSItemField(type = String.class)
    public static final String BODY = "body";

    /**
     * The package name of the email where it was captured.
     * For example gmail is Gmail
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The sender of the email
     */
    @PSItemField(type = String.class)
    public static final String FROM = "from";

    /**
     * The receiver of the email
     */
    @PSItemField(type = String.class)
    public static final String TO = "to";

    /**
     * The subject of the email
     */
    @PSItemField(type = String.class)
    public static final String SUBJECT = "subject";


    /**
     * The timestamp of when the message is sent or received.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    Email(String body, String packageName, String from, String to, String subject, long timeStamp){
        this.setFieldValue(BODY,body);
        this.setFieldValue(PACKAGE_NAME,packageName);
        this.setFieldValue(FROM,from);
        this.setFieldValue(TO,to);
        this.setFieldValue(SUBJECT,subject);
        this.setFieldValue(TIMESTAMP,timeStamp);
    }

    public static MStreamProvider asGmailList(long afterTime, long beforeTime, int maxNumberOfResults){
        return new GmailListProvider(afterTime,beforeTime,maxNumberOfResults);
    }

    public static MStreamProvider asGmailUpdates(){
        return new GmailUpdatesProvider();
    }
}
