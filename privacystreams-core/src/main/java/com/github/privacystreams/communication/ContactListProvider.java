package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Switch;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;
import com.github.privacystreams.utils.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide a stream of contact list.
 */

class ContactListProvider extends MStreamProvider {

    ContactListProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void provide() {
        this.getContactList();
    }

    private void getContactList() {
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
                String _id = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Data._ID));
                // The primary display name
                String displayNameKey = ContactsContract.Data.DISPLAY_NAME_PRIMARY;
                String name = contactCur.getString(contactCur.getColumnIndex(displayNameKey));

                HashMap<String,String> phones = new HashMap<>();

                Cursor phoneCur = contentResolver.query(
                        Phone.CONTENT_URI,
                        null,
                        Phone.CONTACT_ID + " = " + _id,
                        null,
                        null);
                if (phoneCur != null) {
                    while (phoneCur.moveToNext()) {

                        int phoneType = phoneCur.getInt(phoneCur.getColumnIndex(Phone.TYPE));
                        String number = phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER));
//                      provide phone numbers as different types
                        switch(phoneType){
                            case Phone.TYPE_MOBILE:
                                phones.put("mobilePhone", CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_HOME:
                                phones.put("homePhone",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_WORK:
                                phones.put("workPhone",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_FAX_HOME:
                                phones.put("homeFax",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_PAGER:
                                phones.put("pager",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_CALLBACK:
                                phones.put("callback",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_CAR:
                                phones.put("car",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_ASSISTANT:
                                phones.put("assistant",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_COMPANY_MAIN:
                                phones.put("companyMain",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_FAX_WORK:
                                phones.put("workFax",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_OTHER:
                                phones.put("otherPhone",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_OTHER_FAX:
                                phones.put("otherFax",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_ISDN:
                                phones.put("ISDN",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_MAIN:
                                phones.put("main",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_MMS:
                                phones.put("MMS",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_RADIO:
                                phones.put("radio",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_TELEX:
                                phones.put("telex",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_TTY_TDD:
                                phones.put("TTY_TDD",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_WORK_MOBILE:
                                phones.put("workMobile",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                            case Phone.TYPE_WORK_PAGER:
                                phones.put("workPager",CommunicationUtils.normalizePhoneNumber(number));
                                break;
                        }
                    }
                    phoneCur.close();
                }

                HashMap<String,String> emails = new HashMap<>();
                Cursor emailCur = contentResolver.query(
                        Email.CONTENT_URI,
                        null,
                        Email.CONTACT_ID + " = " + _id,
                        null,
                        null);
                if (emailCur != null) {
                    while (emailCur.moveToNext()) {
                        int emailType = emailCur.getInt(emailCur.getColumnIndex(Email.TYPE));
                        String email = emailCur.getString(emailCur.getColumnIndex(Email.ADDRESS));

                        //provide emails as different types
                        switch(emailType){
                            case Email.TYPE_HOME:
                                Logging.debug("home email: "+email);
                                emails.put("homeEmail",email);
                                break;
                            case Email.TYPE_MOBILE:
                                emails.put("mobileEmail",email);
                                break;
                            case Email.TYPE_OTHER:
                                emails.put("otherEmail",email);
                                break;
                            case Email.TYPE_WORK:
                                emails.put("workEmail",email);
                                break;
                        }
                    }
                    emailCur.close();
                }

                Contact contact = new Contact(_id, name, phones, emails);
                this.output(contact);
                contactCur.moveToNext();
            }
        }

        if (contactCur != null) {
            contactCur.close();
        }

        this.finish();
    }
}
