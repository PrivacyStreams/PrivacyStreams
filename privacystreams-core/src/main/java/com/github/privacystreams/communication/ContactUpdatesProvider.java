package com.github.privacystreams.communication;


import android.Manifest;
import android.database.ContentObserver;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.Contacts;

/**
 * Provide a stream of updated contacts
 */

public class ContactUpdatesProvider extends MStreamProvider {
    private Contact updatedContact;
    private ContactStateObserver contactStateObserver;
    private List contactList = null;

    public ContactUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void provide() {
        contactStateObserver = new ContactStateObserver();
        getContext().getApplicationContext().getContentResolver().registerContentObserver(Contacts.CONTENT_URI, true, contactStateObserver);
    }

    //observer
    private class ContactStateObserver extends ContentObserver {

        public ContactStateObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            UQI uqi = new UQI(getContext());
            MStream newContactStream = uqi.getData(Contact.getAll(), Purpose.FEATURE("to get the new contact list"));
            List newContactList = null;
            try {
                newContactList = newContactStream.asList();
            } catch (PSException e) {
                e.printStackTrace();
            }
            if (contactList == null) {
                MStream contactStream = newContactStream;
                try {
                    contactList = contactStream.asList();
                } catch (PSException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    updatedContact = contactChange(contactList, newContactList);
                } catch (PSException e) {
                    e.printStackTrace();
                }
                ContactUpdatesProvider.this.output(updatedContact);
            }
        }


        public void onStop() {
            getContext().getApplicationContext().getContentResolver().unregisterContentObserver(contactStateObserver);
        }

    }

    public Contact contactChange(List oldContactList, List newContactList) throws PSException {
        Contact editedContact;
        List listOfID = new ArrayList();
        List newListOfID = new ArrayList();
        int listTotal = oldContactList.size();
        int newListTotal = newContactList.size();
        for (int i = 0; i < listTotal; i++) {
            Contact contactAti = (Contact) oldContactList.get(i);
            listOfID.add((long) contactAti.getValueByField(Contact.ID));
        }
        for (int j = 0; j < newListTotal; j++) {
            Contact newContactAtj = (Contact) newContactList.get(j);
            newListOfID.add((long) newContactAtj.getValueByField(Contact.ID));
        }

        List similar = new ArrayList();
        for (int k = 0; k < listOfID.size(); k++) {
            similar.add(listOfID.get(k));
        }
        similar.retainAll(newListOfID);

        //add
        if (similar.size() != newListOfID.size()) {
            Contact tempContact = (Contact) newContactList.get(newContactList.size() - 1);
            tempContact.setFieldValue(Contact.STATUS, "added");
            editedContact = tempContact;
            contactList = newContactList;
            return editedContact;
        }
        //delete
        else if (similar.size() != listOfID.size()) {
            List difference = new ArrayList();
            for (int j = 0; j < listTotal; j++) {
                difference.add(listOfID.get(j));
            }
            difference.removeAll(similar);
            long deleted = (long) difference.get(0);
            for (int i = 0; i < listTotal; i++) {
                if (deleted == (long) listOfID.get(i)) {
                    Contact tempContact = (Contact) oldContactList.get(i);
                    tempContact.setFieldValue(Contact.STATUS, "deleted");
                    editedContact = tempContact;
                    contactList = newContactList;
                    return editedContact;
                }
            }
        }
        //edit
        else {
            int i = 0;
            do {
                long newTimeCreated = ((Contact) newContactList.get(i)).getValueByField(Contact.TIME_CREATED);
                Contact oldContact = (Contact) oldContactList.get(i);
                oldContact.setFieldValue(Contact.TIME_CREATED, newTimeCreated);
                if (!((Contact) oldContactList.get(i)).equals((Contact) newContactList.get(i))) {
                    Contact tempContact = (Contact) newContactList.get(i);
                    tempContact.setFieldValue(Contact.STATUS, "edited");
                    editedContact = tempContact;
                    contactList = newContactList;
                    return editedContact;
                }
                i++;
            } while (i < contactList.size());
        }
        return null;
    }
}


