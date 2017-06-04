package com.github.privacystreams.email;

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
     * The email content.
     */

    @PSItemField(type = String.class)
    public static final String CONTENT = "content";

    /**
     * The package name of the email where it was captured.
     * For example gmail is Gmail
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The timestamp of when the message is sent or received.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    Email(String content,String packageName,Long timestamp){
        this.setFieldValue(CONTENT,content);
        this.setFieldValue(PACKAGE_NAME,packageName);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MStreamProvider asUpdates(){
        return new GmailProvider();
    }
}
