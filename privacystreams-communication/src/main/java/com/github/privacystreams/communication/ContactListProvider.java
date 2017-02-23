package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class ContactListProvider extends MultiItemStreamProvider {
    private static final String DESCRIPTION = "dummy data source for testing";

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_CONTACTS
    };

    ContactListProvider() {
    }

    @Override
    protected void provide(MultiItemStream output) {
        boolean permissionGranted = PermissionActivity.requestPermissions(this.getContext(), REQUIRED_PERMISSIONS);
        if (permissionGranted)
            this.getContactList(output);
        else
            Logging.warn("permission not granted: " + Arrays.asList(REQUIRED_PERMISSIONS));
    }

    private void getContactList(MultiItemStream output) {
        ContentResolver contentResolver = this.getContext().getContentResolver();

        Cursor contactCur = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (contactCur != null && contactCur.getCount() > 0) {
            contactCur.moveToFirst();
            while (!contactCur.isAfterLast()) {
                String _ID = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Data._ID));
                // The primary display name
                String displayNameKey = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Data.DISPLAY_NAME;
                String name = contactCur.getString(contactCur.getColumnIndex(displayNameKey));
//                String contactID = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
//                String lookup = c.getString(c.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));

                List<String> phones = new ArrayList<>();
                Cursor phoneCur = contentResolver.query(
                        Phone.CONTENT_URI,
                        null,
                        Phone.CONTACT_ID + " = " + _ID,
                        null,
                        null);
                while (phoneCur.moveToNext()) {
                    String number = phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER));
                    phones.add(number);
//                    int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
                }
                phoneCur.close();

                List<String> emails = new ArrayList<>();
                Cursor emailCur = contentResolver.query(
                        Email.CONTENT_URI,
                        null,
                        Email.CONTACT_ID + " = " + _ID,
                        null,
                        null);
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(emailCur.getColumnIndex(Email.ADDRESS));
                    emails.add(email);
//                    int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
                }
                emailCur.close();

                Contact contact = new Contact(name, phones, emails);
                output.write(contact);
//                Contact contact = new Contact(_ID, contactID, lookup, displayName);
//                contactQuery.write(contact);

                contactCur.moveToNext();
            }
        }

        if (contactCur != null) {
            contactCur.close();
        }

        if (!output.isClosed()) output.write(null);
    }
}
