package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;


/**
 * A Contact item represents a contact.
 */
@PSItem
public class Contact extends Item {
    @PSItemField(name="id", type = String.class, description = "The contact ID in Android database.")
    public static final String ID = "id";

    @PSItemField(name="name", type = String.class, description = "The contact name.")
    public static final String NAME = "name";

    @PSItemField(name="phone_numbers", type = List.class, description = "The phone numbers of the contact.")
    public static final String PHONES = "phone_numbers";

    @PSItemField(name="emails", type = List.class, description = "The emails of the contact.")
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
