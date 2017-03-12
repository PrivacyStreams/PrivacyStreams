package com.github.privacystreams.communication;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * A Contact item represents a contact.
 */
@PSItem
public class Contact extends Item {

    /**
     * The contact ID in Android database.
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
    public static final String PHONES = "phone_numbers";

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
     * Get a provider that provides a stream of contact list
     * @return the stream provider
     */
    public static Function<Void, MultiItemStream> asList() {
        return new ContactListProvider();
    }
}
