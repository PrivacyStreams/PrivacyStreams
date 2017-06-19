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
    @PSItemField(type = String.class)
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
    public static final String MOBILE_PHONE = "mobile phone";

    /**
     * The home number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String HOME_PHONE = "home phone";

    /**
     * The work number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String WORK_PHONE = "work phone";

    /**
     * The work fax number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String WORKFAX = "work fax";

    /**
     * The home fax number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String HOMEFAX = "home fax";

    /**
     * The assistant number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String  ASSISTANT= "assistant";

    /**
     * The call back number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String CALLBACK = "call back";

    /**
     * The car number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String CAR = "car";

    /**
     * The company main number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String COMPANY_MAIN = "company main";

    /**
     * The ISDN of the contact.
     */
    @PSItemField(type = String.class)
    public static final String ISDN = "ISDN";

    /**
     * The main number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String MAIN = "main";

    /**
     * The MMS number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String MMS = "MMS";

    /**
     * The other number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String OTHER_PHONE = "other phone";

    /**
     * The other fax number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String OTHER_FAX = "other fax";

    /**
     * The pager number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String PAGER = "pager";

    /**
     * The radio number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String RADIO = "radio";

    /**
     * The telex number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String TELEX = "telex";

    /**
     * The TTY_TDD of the contact.
     */
    @PSItemField(type = String.class)
    public static final String TTY_TDD = "TTY_TDD";

    /**
     * The work mobile number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String WORK_MOBILE = "work mobile";

    /**
     * The work pager number of the contact.
     */
    @PSItemField(type = String.class)
    public static final String WORK_PAGER = "work pager";

    /**
     * The home email of the contact.
     */
    @PSItemField(type = String.class)
    public static final String HOME_EMAIL = "homeEmail";

    /**
     * The work email of the contact.
     */
    @PSItemField(type = String.class)
    public static final String WORK_EMAIL = "workEmail";

    /**
     * The other email of the contact.
     */
    @PSItemField(type = String.class)
    public static final String OTHER_EMAIL = "otherEmail";

    /**
     * The mobile email of the contact.
     */
    @PSItemField(type = String.class)
    public static final String MOBILE_EMAIL = "mobileEmail";

    /**
     * construct a contact item
     * @param id
     * @param name
     * @param phones
     * @param emails
     */
    Contact(String id, String name, HashMap<String, String> phones, HashMap<String,String> emails) {
        this.setFieldValue(ID, id);
        this.setFieldValue(NAME, name);
        if(phones.containsKey("mobilePhone")) this.setFieldValue(MOBILE_PHONE, phones.get("mobilePhone"));
        if(phones.containsKey("workFax")) this.setFieldValue(WORKFAX, phones.get("workFax"));
        if(phones.containsKey("homePhone")) this.setFieldValue(HOME_PHONE, phones.get("homePhone"));
        if(phones.containsKey("workPhone")) this.setFieldValue(WORK_PHONE, phones.get("workPhone"));
        if(phones.containsKey("homeFax")) this.setFieldValue(HOMEFAX, phones.get("homeFax"));
        if(phones.containsKey("assistant")) this.setFieldValue(ASSISTANT, phones.get("assistant"));
        if(phones.containsKey("callBack")) this.setFieldValue(CALLBACK, phones.get("callBack"));
        if(phones.containsKey("car")) this.setFieldValue(CAR, phones.get("car"));
        if(phones.containsKey("companyMain")) this.setFieldValue(COMPANY_MAIN, phones.get("companyMain"));
        if(phones.containsKey("ISDN")) this.setFieldValue(ISDN, phones.get("ISDN"));
        if(phones.containsKey("main")) this.setFieldValue(MAIN, phones.get("main"));
        if(phones.containsKey("MMS")) this.setFieldValue(MMS, phones.get("MMS"));
        if(phones.containsKey("otherPhone")) this.setFieldValue(OTHER_PHONE, phones.get("otherPhone"));
        if(phones.containsKey("otherFax")) this.setFieldValue(OTHER_FAX, phones.get("otherFax"));
        if(phones.containsKey("pager")) this.setFieldValue(PAGER, phones.get("pager"));
        if(phones.containsKey("radio")) this.setFieldValue(RADIO, phones.get("radio"));
        if(phones.containsKey("telex")) this.setFieldValue(TELEX, phones.get("telex"));
        if(phones.containsKey("TTY_TDD")) this.setFieldValue(TTY_TDD, phones.get("TTY_TDD"));
        if(phones.containsKey("workMobile")) this.setFieldValue(WORK_MOBILE, phones.get("workMobile"));
        if(phones.containsKey("workPager")) this.setFieldValue(WORK_PAGER, phones.get("workPager"));
        if(emails.containsKey("homeEmail")) this.setFieldValue(HOME_EMAIL, emails.get("homeEmail"));
        if(emails.containsKey("workEmail")) this.setFieldValue(WORK_EMAIL, emails.get("workEmail"));
        if(emails.containsKey("otherEmail")) this.setFieldValue(OTHER_EMAIL, emails.get("otherEmail"));
        if(emails.containsKey("mobileEmail")) this.setFieldValue(MOBILE_EMAIL, emails.get("mobileEmail"));
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
}
