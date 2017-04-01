package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;

/**
 * Provide a stream of existing SMS messages
 */

class SMSMessageListProvider extends MStreamProvider {

    public SMSMessageListProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_SMS);
    }

    @Override
    protected void provide() {
        ContentResolver contentResolver = this.getContext().getContentResolver();

        Cursor c = contentResolver.query(
                Uri.parse("content://sms"),
                null,
                null,
                null,
                null
        );

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Long date = c.getLong(c.getColumnIndex("date"));
                Long dateSent = c.getLong(c.getColumnIndex("date_sent"));
                String smsId = c.getString(c.getColumnIndex("_id"));
                String address = CommunicationUtils.normalizePhoneNumber(c.getString(c.getColumnIndex("address")));
                Integer type = c.getInt(c.getColumnIndex("type"));
                String typeStr;
                switch (type) {
                    case 1: typeStr = Message.TYPE_RECEIVED; break;
                    case 2: typeStr = Message.TYPE_SENT; break;
                    case 3: typeStr = Message.TYPE_DRAFT; break;
                    case 4:
                    case 5:
                    case 6: typeStr = Message.TYPE_PENDING; break;
                    default: typeStr = Message.TYPE_UNKNOWN;
                }
                String content = c.getString(c.getColumnIndex("body"));
                Integer seen = c.getInt(c.getColumnIndex("seen"));
                Integer read = c.getInt(c.getColumnIndex("read"));

                Message message = new Message(typeStr, content, "system", address, date);
//                message.setFieldValue("date_sent", dateSent);
                message.setFieldValue("sms_id", smsId);
                message.setFieldValue("seen", seen==1);
                message.setFieldValue("read", read==1);

                this.output(message);

                c.moveToNext();
            }
        }

        if (c != null) {
            c.close();
        }

        this.finish();
    }

}
