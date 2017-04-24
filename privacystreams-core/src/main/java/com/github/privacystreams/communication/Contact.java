package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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
     * The phone numbers of the contact.
     */
    @PSItemField(type = List.class)
    public static final String PHONES = "phones";

    /**
     * The emails of the contact.
     */
    @PSItemField(type = List.class)
    public static final String EMAILS = "emails";

    Contact(String id, String name, List<String> phones, List<String> emails) {
        this.setFieldValue(ID, id);
        this.setFieldValue(NAME, name);
        this.setFieldValue(PHONES, phones);
        this.setFieldValue(EMAILS, emails);
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
