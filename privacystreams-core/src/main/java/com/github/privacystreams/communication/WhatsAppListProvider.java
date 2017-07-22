package com.github.privacystreams.communication;

import android.Manifest;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangyusen on 7/22/17.
 */

public class WhatsAppListProvider extends MStreamProvider{
    @Override
    protected void provide() {
        this.getWhatsAppContactList();

    }
    WhatsAppListProvider(){this.addRequiredPermissions(Manifest.permission.READ_CONTACTS);};

    private void getWhatsAppContactList(){
        Cursor c = this.getContext().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY },
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[] { "com.whatsapp" },
                null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                HashMap<String,List> phones = new HashMap<>();
//                long _id = c.getLong(c.getColumnIndex(ContactsContract.Data._ID));
//                Cursor phoneCur = this.getContext().getContentResolver().query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _id,
//                        null,
//                        null);

                 String displayNameKey = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;
                 String name = c.getString(c.getColumnIndex(displayNameKey));
                 Contact contact = new Contact(null, name, null, null, null);
                this.output(contact);
                c.moveToNext();
                    }
                }
                if (c != null) {
                    c.close();
                }
                this.finish();
    }
}
