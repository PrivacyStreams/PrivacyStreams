package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A received or sent email.
 */
@PSItem
public class Email extends Item {
    /**
     * The email body.
     */

    @PSItemField(type = String.class)
    public static final String BODY = "body";

    /**
     * The package name of the email app.
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

    /**
     * Provide a list of Email items from the Gmail app.
     * List will be generated given a time window (in ms) and
     * a max number of returned results for query.
     * @return the provider function.
     */
    public static MStreamProvider asGmailList(long afterTime, long beforeTime, int maxNumberOfResults){
        return new GmailListProvider(afterTime,beforeTime,maxNumberOfResults);
    }

    /**
     * Provide a live stream of Email items from the Gmail app.
     * Updates will be generated if there are emails sent or received per hour.
     * @return the provider function.
     */
    public static MStreamProvider asGmailUpdates(){
        return new GmailUpdatesProvider();
    }
}
