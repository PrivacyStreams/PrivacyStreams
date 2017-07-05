package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.HashMap;
import java.util.List;


/**
 * The information of a contact.
 */
@PSItem
public class Contact extends Item {

    /**
     * The contact's unique ID in Android database.
     */
    @PSItemField(type = Long.class)
    public static final String ID = "id";

    /**
     * The contact name.
     */
    @PSItemField(type = String.class)
    public static final String NAME = "name";

    /**
     * The mobile number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String MOBILE_PHONE = "mobile_phone";

    /**
     * The home number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String HOME_PHONE = "home_phone";

    /**
     * The work number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String WORK_PHONE = "work_phone";

    /**
     * The other number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String OTHER_PHONE = "other_phone";

    /**
     * The work fax number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String WORK_FAX = "work_fax";

    /**
     * The home fax number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String HOME_FAX = "home_fax";

    /**
     * The assistant number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String  ASSISTANT= "assistant";

    /**
     * The call back number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String CALLBACK = "call_back";

    /**
     * The car number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String CAR = "car";

    /**
     * The company main number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String COMPANY_MAIN = "company_main";

    /**
     * The ISDN of the contact.
     */
    @PSItemField(type = List.class)
    public static final String ISDN = "ISDN";

    /**
     * The main number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String MAIN = "main";

    /**
     * The MMS number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String MMS = "MMS";


    /**
     * The other fax number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String OTHER_FAX = "other_fax";

    /**
     * The pager number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String PAGER = "pager";

    /**
     * The radio number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String RADIO = "radio";

    /**
     * The telex number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String TELEX = "telex";

    /**
     * The TTY_TDD of the contact.
     */
    @PSItemField(type = List.class)
    public static final String TTY_TDD = "TTY_TDD";

    /**
     * The work mobile number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String WORK_MOBILE = "work_mobile";

    /**
     * The work pager number of the contact.
     */
    @PSItemField(type = List.class)
    public static final String WORK_PAGER = "work_pager";

    /**
     * The home email of the contact.
     */
    @PSItemField(type = List.class)
    public static final String HOME_EMAIL = "home_email";

    /**
     * The work email of the contact.
     */
    @PSItemField(type = List.class)
    public static final String WORK_EMAIL = "work_email";

    /**
     * The other email of the contact.
     */
    @PSItemField(type = List.class)
    public static final String OTHER_EMAIL = "other_email";

    /**
     * The mobile email of the contact.
     */
    @PSItemField(type = List.class)
    public static final String MOBILE_EMAIL = "mobile_email";

    /**
     *
     */
    @PSItemField(type = String.class)
    public static final String STATUS = "status";

    public static final String STATUS_ADDED = "added";
    public static final String STATUS_DELETED = "deleted";
    public static final String STATUS_EDITED = "edited";

    /**
     * construct a contact item
     * @param id id of contact
     * @param name name of contact
     * @param phones array list of phone numbers
     * @param emails array list of emails
     */
    Contact(Long id, String name, HashMap<String, List> phones, HashMap<String,List> emails, String status) {
        this.setFieldValue(ID, id);
        this.setFieldValue(NAME, name);

        if(phones.containsKey(MOBILE_PHONE))
            this.setFieldValue(MOBILE_PHONE, phones.get(MOBILE_PHONE));
        else
            this.setFieldValue(MOBILE_PHONE, null);

        if(phones.containsKey(WORK_FAX))
            this.setFieldValue(WORK_FAX, phones.get(WORK_FAX));
        else
            this.setFieldValue(WORK_FAX, null);

        if(phones.containsKey(HOME_PHONE))
            this.setFieldValue(HOME_PHONE, phones.get(HOME_PHONE));
        else
            this.setFieldValue(HOME_PHONE, null);

        if(phones.containsKey(WORK_PHONE))
            this.setFieldValue(WORK_PHONE, phones.get(WORK_PHONE));
        else
            this.setFieldValue(WORK_PHONE, null);

        if(phones.containsKey(HOME_FAX))
            this.setFieldValue(HOME_FAX, phones.get(HOME_FAX));
        else
            this.setFieldValue(HOME_FAX, null);

        if(phones.containsKey(ASSISTANT))
            this.setFieldValue(ASSISTANT, phones.get(ASSISTANT));
        else
            this.setFieldValue(ASSISTANT, null);

        if(phones.containsKey(CALLBACK))
            this.setFieldValue(CALLBACK, phones.get(CALLBACK));
        else
            this.setFieldValue(CALLBACK, null);

        if(phones.containsKey(CAR))
            this.setFieldValue(CAR, phones.get(CAR));
        else
            this.setFieldValue(CAR, null);

        if(phones.containsKey(COMPANY_MAIN))
            this.setFieldValue(COMPANY_MAIN, phones.get(COMPANY_MAIN));
        else
            this.setFieldValue(COMPANY_MAIN, null);

        if(phones.containsKey(ISDN))
            this.setFieldValue(ISDN, phones.get(ISDN));
        else
            this.setFieldValue(ISDN, null);

        if(phones.containsKey(MAIN))
            this.setFieldValue(MAIN, phones.get(MAIN));
        else
            this.setFieldValue(MAIN, null);

        if(phones.containsKey(MMS))
            this.setFieldValue(MMS, phones.get(MMS));
        else
            this.setFieldValue(MMS, null);

        if(phones.containsKey(OTHER_PHONE))
            this.setFieldValue(OTHER_PHONE, phones.get(OTHER_PHONE));
        else
            this.setFieldValue(OTHER_PHONE, null);

        if(phones.containsKey(OTHER_FAX))
            this.setFieldValue(OTHER_FAX, phones.get(OTHER_FAX));
        else
            this.setFieldValue(OTHER_FAX, null);

        if(phones.containsKey(PAGER))
            this.setFieldValue(PAGER, phones.get(PAGER));
        else
            this.setFieldValue(PAGER, null);

        if(phones.containsKey(RADIO))
            this.setFieldValue(RADIO, phones.get(RADIO));
        else
            this.setFieldValue(RADIO, null);

        if(phones.containsKey(TELEX))
            this.setFieldValue(TELEX, phones.get(TELEX));
        else
            this.setFieldValue(TELEX, null);

        if(phones.containsKey(TTY_TDD))
            this.setFieldValue(TTY_TDD, phones.get(TTY_TDD));
        else
            this.setFieldValue(TTY_TDD, null);

        if(phones.containsKey(WORK_MOBILE))
            this.setFieldValue(WORK_MOBILE, phones.get(WORK_MOBILE));
        else
            this.setFieldValue(WORK_MOBILE, null);

        if(phones.containsKey(WORK_PAGER))
            this.setFieldValue(WORK_PAGER, phones.get(WORK_PAGER));
        else
            this.setFieldValue(WORK_PAGER, null);

        if(emails.containsKey(HOME_EMAIL))
            this.setFieldValue(HOME_EMAIL, emails.get(HOME_EMAIL));
        else
            this.setFieldValue(HOME_EMAIL, null);

        if(emails.containsKey(WORK_EMAIL))
            this.setFieldValue(WORK_EMAIL, emails.get(WORK_EMAIL));
        else
            this.setFieldValue(WORK_EMAIL, null);

        if(emails.containsKey(OTHER_EMAIL))
            this.setFieldValue(OTHER_EMAIL, emails.get(OTHER_EMAIL));
        else
            this.setFieldValue(OTHER_EMAIL, null);

        if(emails.containsKey(MOBILE_EMAIL))
            this.setFieldValue(MOBILE_EMAIL, emails.get(MOBILE_EMAIL));
        else
            this.setFieldValue(MOBILE_EMAIL, null);

        this.setFieldValue(STATUS, status);
    }

    public Contact(Contact another){
        for(String key: another.toMap().keySet()){
            this.setFieldValue(key, another.getValueByField(key));
        }
    }
    /**
     * Provide all Contact items in device's contacts database.
     * This provider requires `android.permission.READ_CONTACTS` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_CONTACTS)
    public static MStreamProvider getAll() {
        return new ContactListProvider();
    }

    public static MStreamProvider asUpdates() {return new ContactUpdatesProvider();}
}
