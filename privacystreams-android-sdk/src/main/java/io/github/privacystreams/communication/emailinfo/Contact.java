package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class Contact extends Item {

    public static PStreamProvider getContact(String api_key, String api_secret){
        return new EmailContactProvider(api_key,api_secret);
    }

    //TODO delete this function when debug ends
    public static PStreamProvider getContact(String api_key, String api_secret, String userName){
        return new EmailContactProvider(api_key,api_secret,userName);
    }
    /*Fields*/

    @PSItemField(type = String.class)
    public static final String NAME = "name";

    @PSItemField(type = String.class)
    public static final String FAMILY_NAME = "family_name";

    @PSItemField(type = String.class)
    public static final String GIVEN_NAME = "given_name";

    @PSItemField(type = String.class)
    public static final String JOB_TITLE = "job_title";

    @PSItemField(type = String.class)
    public static final String EMAIL = "email";

    @PSItemField(type = JsonNode.class)
    public static final String WORKS_FOR = "works_for";

    @PSItemField(type = String.class)
    public static final String FAX_NUMBER = "fax_number";

    @PSItemField(type = JsonNode.class)
    public static final String HOME_LOCATION = "home_location";

    @PSItemField(type = JsonNode.class)
    public static final String WORK_LOCATION = "work_location";

    @PSItemField(type = String.class)
    public static final String TELEPHONE = "telephone";

    @PSItemField(type = String.class)
    public static final String TYPE = "type";




}
