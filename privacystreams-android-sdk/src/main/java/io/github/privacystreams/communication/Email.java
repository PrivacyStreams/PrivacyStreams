package io.github.privacystreams.communication;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Email.
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
        this.setFieldValue(BODY, body);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(FROM, from);
        this.setFieldValue(TO, to);
        this.setFieldValue(SUBJECT, subject);
        this.setFieldValue(TIMESTAMP, timeStamp);
    }

    /**
     * Provide a list of Email items from Gmail.
     *
     * @param afterTime the minimum timestamp of emails to get
     * @param beforeTime the maximum timestamp of emails to get
     * @param maxNumberOfResults the max number of emails to get
     * @return the provider function.
     */
    public static PStreamProvider asGmailHistory(long afterTime, long beforeTime, int maxNumberOfResults){
        return new GmailHistoryProvider(afterTime, beforeTime, maxNumberOfResults);
    }

    /**
     * Provide a live stream of Emails from Gmail.
     * Updates will be generated if there are emails sent or received per hour.
     *
     * @param frequency the frequency of checking updates
     * @return the provider function.
     */
    public static PStreamProvider asGmailUpdates(long frequency) {
        return new GmailUpdatesProvider(frequency);
    }
}
