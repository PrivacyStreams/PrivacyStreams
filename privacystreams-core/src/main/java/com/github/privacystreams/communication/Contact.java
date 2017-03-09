package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

import java.util.List;


/**
 * A Contact item represents a contact.
 */

public class Contact extends Item {
    @ItemField(name="id", type = String.class, description = "The contact ID in Android database.")
    public static final String ID = "id";

    @ItemField(name="name", type = String.class, description = "The contact name.")
    public static final String NAME = "name";

    @ItemField(name="phone_numbers", type = List.class, description = "The phone numbers of the contact.")
    public static final String PHONES = "phone_numbers";

    @ItemField(name="emails", type = List.class, description = "The emails of the contact.")
    public static final String EMAILS = "emails";

    Contact(String id, String name, List<String> phones, List<String> emails) {
        this.setFieldValue(ID, id);
        this.setFieldValue(NAME, name);
        this.setFieldValue(PHONES, phones);
        this.setFieldValue(EMAILS, emails);
    }

    /**
     * Get a provider that provides a stream of contact list
     * @return the stream provider
     */
    public static MultiItemStreamProvider asList() {
        return new ContactListProvider();
    }
}
