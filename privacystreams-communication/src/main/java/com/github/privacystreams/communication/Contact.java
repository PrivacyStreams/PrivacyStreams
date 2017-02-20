package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.List;


/**
 * Created by yuanchun on 07/12/2016.
 * Contact item
 */

public class Contact extends Item {
    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PHONES = "phone_numbers";
    public static final String EMAILS = "emails";

    public Contact(String name, List<String> phones, List<String> emails) {
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
