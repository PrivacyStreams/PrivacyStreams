package com.github.privacystreams.communication;


import android.Manifest;
import android.database.ContentObserver;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.Date;
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
    private long lastUpdateTime = 0;

    public ContactUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CONTACTS);
    }


    @Override
    protected void provide() {
        contactStateObserver = new ContactStateObserver();
        UQI oldUqi = new UQI(getContext());
        try {
            contactList = oldUqi.getData(Contact.getAll(), Purpose.FEATURE("get original contacts on phone")).asList();
        } catch (PSException e) {
            e.printStackTrace();
        }
        oldUqi.stopAll();
        getContext().getApplicationContext().getContentResolver().registerContentObserver(Contacts.CONTENT_URI, false, contactStateObserver);
    }

    //observer
    private class ContactStateObserver extends ContentObserver {

        ContactStateObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            //import Date to handle exceptions raised by actions like refreshing the contact
            //which will call the onChange method but will return nothing
            Date timer = new Date();
            long thisTime = timer.getTime();
            if (lastUpdateTime == 0) {
                outputEffectiveData();
            } else if (thisTime - lastUpdateTime > 1000) {
                outputEffectiveData();
            }
            lastUpdateTime = thisTime;
            super.onChange(selfChange);
        }

        //a method just used to avoid repeated codes
        private void outputEffectiveData() {
            UQI uqi = new UQI(getContext());
            MStream newContactStream = uqi.getData(Contact.getAll(), Purpose.FEATURE("to get the new contact list"));
            uqi.stopAll();
            List newContactList = null;
            try {
                newContactList = newContactStream.asList();
            } catch (PSException e) {
                e.printStackTrace();
            }
            try {
                updatedContact = contactChange(contactList, newContactList);
            } catch (PSException e) {
                e.printStackTrace();
            }

            if (updatedContact != null) {
                ContactUpdatesProvider.this.output(updatedContact);
                updatedContact = null;
            }

            uqi.stopAll();
        }
    }

    @Override
    public void onCancel(UQI uqi) {
        getContext().getApplicationContext().getContentResolver().unregisterContentObserver(contactStateObserver);
    }

    private Contact contactChange(List oldContactList, List newContactList) throws PSException {
        Contact editedContact;
        List<Long> listOfID = new ArrayList<>();
        List<Long> newListOfID = new ArrayList<>();
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

        List<Long> similar = new ArrayList<>();
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
            List<Long> difference = new ArrayList<>();
            for (int j = 0; j < listTotal; j++) {
                difference.add(listOfID.get(j));
            }
            difference.removeAll(similar);
            long deleted = difference.get(0);
            for (int i = 0; i < listTotal; i++) {
                if (deleted == listOfID.get(i)) {
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
        contactList = newContactList;
        return null;
    }
}


