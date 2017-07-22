package com.github.privacystreams.communication;

import android.Manifest;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

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
        String[] projection    = new String[] {
                ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,  ContactsContract.CommonDataKinds.Phone.CONTACT_ID,      ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor people = this.getContext().getContentResolver().query(  ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,   ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[] { "com.whatsapp" }, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int indexUid=people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

        if(people != null   && people.moveToFirst()){
            people.moveToFirst();
            while(!people.isAfterLast()){
            HashMap<String,List> phones = new HashMap<>();
            List mobileList = new ArrayList();
            String name   = people.getString(indexName);
            String number2 = people.getString(indexNumber);
            mobileList.add(0,number2);
            String uid=people.getString(indexUid);
            phones.put(Contact.MOBILE_PHONE, mobileList);
            Contact contact = new Contact(null, name, phones, new HashMap<String,List>(), null,uid);
            this.output(contact);
            people.moveToNext();
        }
        }
        if (people != null) {
            people.close();
        }
        this.finish();
    }
}
