package com.github.privacystreams.email;

import android.util.Log;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * The received date of the email
     */
    @PSItemField(type = String.class)
    public static final String DATE = "date";

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

    Email(String content,String packageName,String from,String to,String subject,long timeStamp){
        this.setFieldValue(CONTENT,content);
        this.setFieldValue(PACKAGE_NAME,packageName);
        this.setFieldValue(FROM,from);
        this.setFieldValue(TO,to);
        this.setFieldValue(SUBJECT,subject);
        this.setFieldValue(TIMESTAMP,timeStamp);
    }



    public static MStreamProvider asEmailList(){return new GmailProvider();}

    public static MStreamProvider asEmailUpdates(){
        return new GmailUpdatesProvider();
    }
}
