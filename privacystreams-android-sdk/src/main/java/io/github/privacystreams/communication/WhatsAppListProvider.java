package io.github.privacystreams.communication;

import android.Manifest;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.privacystreams.core.PStreamProvider;


public class WhatsAppListProvider extends PStreamProvider{
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

        if(people.moveToFirst()){
            people.moveToFirst();

            while(!people.isAfterLast()){
                HashMap<String,List> phones = new HashMap<>();
                List mobileList = new ArrayList();
                String name   = people.getString(indexName);
                String phone_number = people.getString(indexNumber);
                mobileList.add(0,phone_number);
                String uid=people.getString(indexUid);
                phones.put(Contact.MOBILE_PHONE, mobileList);
                Contact contact = new Contact(null, name, phones, new HashMap<String,List>(), null,uid);
                this.output(contact);
                people.moveToNext();
                 }
             }
        people.close();
        this.finish();
    }
}