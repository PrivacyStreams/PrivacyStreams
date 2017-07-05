package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                long _id = contactCur.getLong(contactCur.getColumnIndex(ContactsContract.Data._ID));
                // The primary display name
                String displayNameKey = ContactsContract.Data.DISPLAY_NAME_PRIMARY;
                String name = contactCur.getString(contactCur.getColumnIndex(displayNameKey));

                HashMap<String,List> phones = new HashMap<>();
                List mobileList = new ArrayList();
                List homeList = new ArrayList();
                List workList = new ArrayList();
                List homeFaxList = new ArrayList();
                List pagerList = new ArrayList();
                List callbackList = new ArrayList();
                List carList = new ArrayList();
                List assistantList = new ArrayList();
                List companyMainList = new ArrayList();
                List workFaxList = new ArrayList();
                List otherList = new ArrayList();
                List otherFaxList = new ArrayList();
                List ISDNList = new ArrayList();
                List mainList = new ArrayList();
                List MMSList = new ArrayList();
                List radioList = new ArrayList();
                List telexList = new ArrayList();
                List TTY_TDDList = new ArrayList();
                List workMobileList = new ArrayList();
                List workPagerList = new ArrayList();

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
                        // provide phone numbers as different types
                        switch(phoneType){
                            case Phone.TYPE_MOBILE:
                                mobileList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.MOBILE_PHONE))
                                    phones.put(Contact.MOBILE_PHONE, mobileList);

                                break;

                            case Phone.TYPE_HOME:
                                homeList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.HOME_PHONE))
                                    phones.put(Contact.HOME_PHONE, homeList);

                                break;

                            case Phone.TYPE_WORK:
                                workList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.WORK_PHONE))
                                    phones.put(Contact.WORK_PHONE, workList);

                                break;

                            case Phone.TYPE_FAX_HOME:
                                homeFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.HOME_FAX))
                                    phones.put(Contact.HOME_FAX, homeFaxList);

                                break;

                            case Phone.TYPE_PAGER:
                                pagerList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.PAGER))
                                    phones.put(Contact.PAGER, pagerList);

                                break;

                            case Phone.TYPE_CALLBACK:
                                callbackList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.MOBILE_PHONE))
                                    phones.put(Contact.MOBILE_PHONE, callbackList);

                                break;

                            case Phone.TYPE_CAR:
                                carList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.CAR))
                                    phones.put(Contact.CAR, carList);

                                break;

                            case Phone.TYPE_ASSISTANT:
                                assistantList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.ASSISTANT))
                                    phones.put(Contact.ASSISTANT, assistantList);

                                break;

                            case Phone.TYPE_COMPANY_MAIN:
                                companyMainList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.COMPANY_MAIN))
                                    phones.put(Contact.COMPANY_MAIN, companyMainList);

                                break;

                            case Phone.TYPE_FAX_WORK:
                                workFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.WORK_FAX))
                                    phones.put(Contact.WORK_FAX, workFaxList);

                                break;

                            case Phone.TYPE_OTHER:
                                otherList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.OTHER_PHONE))
                                    phones.put(Contact.OTHER_PHONE, otherList);

                                break;

                            case Phone.TYPE_OTHER_FAX:
                                otherFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.OTHER_FAX))
                                    phones.put(Contact.OTHER_FAX, otherFaxList);

                                break;

                            case Phone.TYPE_ISDN:
                                ISDNList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.ISDN))
                                    phones.put(Contact.ISDN, ISDNList);

                                break;

                            case Phone.TYPE_MAIN:
                                mainList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.MAIN))
                                    phones.put(Contact.MAIN, mainList);

                                break;

                            case Phone.TYPE_MMS:
                                MMSList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.MMS))
                                    phones.put(Contact.MMS, MMSList);
                                break;

                            case Phone.TYPE_RADIO:
                                radioList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.RADIO))
                                    phones.put(Contact.RADIO, radioList);
                                break;

                            case Phone.TYPE_TELEX:
                                telexList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.TELEX))
                                    phones.put(Contact.TELEX, telexList);

                                break;

                            case Phone.TYPE_TTY_TDD:
                                TTY_TDDList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.TTY_TDD))
                                    phones.put(Contact.TTY_TDD, TTY_TDDList);
                                break;

                            case Phone.TYPE_WORK_MOBILE:
                                workMobileList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.WORK_MOBILE))
                                    phones.put(Contact.WORK_MOBILE, workMobileList);

                                break;

                            case Phone.TYPE_WORK_PAGER:
                                workPagerList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey(Contact.WORK_PAGER))
                                    phones.put(Contact.WORK_PAGER, workPagerList);
                                break;
                        }
                    }
                    phoneCur.close();
                }

                HashMap<String,List> emails = new HashMap<>();
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
                        List homeEmailList = new ArrayList();
                        List mobileEmailList = new ArrayList();
                        List otherEmailList = new ArrayList();
                        List workEmailList = new ArrayList();

                        //provide emails as different types
                        switch(emailType){
                            case Email.TYPE_HOME:
                                homeEmailList.add(email);
                                if(!emails.containsKey(Contact.HOME_EMAIL))
                                    emails.put(Contact.HOME_EMAIL,homeEmailList);

                                break;

                            case Email.TYPE_MOBILE:
                                mobileEmailList.add(email);
                                if(!emails.containsKey(Contact.MOBILE_EMAIL))
                                    emails.put(Contact.MOBILE_EMAIL,mobileEmailList);
                                break;

                            case Email.TYPE_OTHER:
                                otherEmailList.add(email);
                                if(!emails.containsKey(Contact.OTHER_EMAIL))
                                    emails.put(Contact.OTHER_EMAIL,otherEmailList);
                                break;


                            case Email.TYPE_WORK:
                                workEmailList.add(email);
                                if(!emails.containsKey(Contact.WORK_EMAIL))
                                    emails.put(Contact.WORK_EMAIL,workEmailList);
                                break;

                        }
                    }
                    emailCur.close();
                }

                Contact contact = new Contact(_id, name, phones, emails, Contact.STATUS_ADDED);
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
