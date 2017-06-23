package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
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
//                      provide phone numbers as different types
                        switch(phoneType){
                            case Phone.TYPE_MOBILE:
                                mobileList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("mobilePhone")) phones.put("mobilePhone", mobileList);
                                break;
                            case Phone.TYPE_HOME:
                                homeList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("homePhone")) phones.put("homePhone", homeList);
                                break;
                            case Phone.TYPE_WORK:
                                workList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("workPhone")) phones.put("workPhone", workList);
                                break;
                            case Phone.TYPE_FAX_HOME:
                                homeFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("homeFax")) phones.put("homeFax", homeFaxList);
                                break;
                            case Phone.TYPE_PAGER:
                                pagerList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("pager")) phones.put("pager", pagerList);
                                break;
                            case Phone.TYPE_CALLBACK:
                                callbackList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("callback")) phones.put("mobilePhone", callbackList);
                                break;
                            case Phone.TYPE_CAR:
                                carList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("car")) phones.put("car", carList);
                                break;
                            case Phone.TYPE_ASSISTANT:
                                assistantList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("assistant")) phones.put("assistant", assistantList);
                                break;
                            case Phone.TYPE_COMPANY_MAIN:
                                companyMainList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("companyMain")) phones.put("companyMain", companyMainList);
                                break;
                            case Phone.TYPE_FAX_WORK:
                                workFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("workFax")) phones.put("workFax", workFaxList);
                                break;
                            case Phone.TYPE_OTHER:
                                otherList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("otherPhone")) phones.put("otherphone", otherList);
                                break;
                            case Phone.TYPE_OTHER_FAX:
                                otherFaxList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("otherFax")) phones.put("otherFax", otherFaxList);
                                break;
                            case Phone.TYPE_ISDN:
                                ISDNList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("ISDN")) phones.put("ISDN", ISDNList);
                                break;
                            case Phone.TYPE_MAIN:
                                mainList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("main")) phones.put("main", mainList);
                                break;
                            case Phone.TYPE_MMS:
                                MMSList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("MMS")) phones.put("MMS", MMSList);
                                break;
                            case Phone.TYPE_RADIO:
                                radioList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("radio")) phones.put("radio", radioList);
                                break;
                            case Phone.TYPE_TELEX:
                                telexList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("telex")) phones.put("telex", telexList);
                                break;
                            case Phone.TYPE_TTY_TDD:
                                TTY_TDDList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("TTY_TDD")) phones.put("TTY_TDD", TTY_TDDList);
                                break;
                            case Phone.TYPE_WORK_MOBILE:
                                workMobileList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("workMobile")) phones.put("workMobile", workMobileList);
                                break;
                            case Phone.TYPE_WORK_PAGER:
                                workPagerList.add(CommunicationUtils.normalizePhoneNumber(number));
                                if(!phones.containsKey("workPager")) phones.put("workPager", workPagerList);
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
                                if(!emails.containsKey("homeEmail")) emails.put("homeEmail",homeEmailList);
                                break;
                            case Email.TYPE_MOBILE:
                                mobileEmailList.add(email);
                                if(!emails.containsKey("mobileEmail")) emails.put("mobileEmail",mobileEmailList);
                                break;
                            case Email.TYPE_OTHER:
                                otherEmailList.add(email);
                                if(!emails.containsKey("otherEmail")) emails.put("otherEmail",otherEmailList);
                                break;
                            case Email.TYPE_WORK:
                                workEmailList.add(email);
                                if(!emails.containsKey("workEmail")) emails.put("workEmail",workEmailList);
                                break;
                        }
                    }
                    emailCur.close();
                }

                Contact contact = new Contact(_id, name, phones, emails, "added");
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
