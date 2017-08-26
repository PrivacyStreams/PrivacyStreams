package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.utils.Logging;


public class EmailContactProvider extends EmailInfoProvider {

    private static final String REQUEST_DOMAIN = "contact";
    private static final String GET_DOMAIN = "contacts";

    public EmailContactProvider(String api_key, String api_secret){
        super(api_key,api_secret,REQUEST_DOMAIN);
    }

    private void getContactInfo(JsonNode jsonNode){
        Contact contact = new Contact();
        contact.setFieldValue(io.github.privacystreams.communication.emailinfo.Contact.NAME,
                jsonNode.get(0).get("name").toString());
        contact.setFieldValue(Contact.FAMILY_NAME, jsonNode.get(0).get("familyName").toString());
        contact.setFieldValue(Contact.GIVEN_NAME, jsonNode.get(0).get("givenName").toString());
        contact.setFieldValue(Contact.JOB_TITLE, jsonNode.get(0).get("jobTitle").toString());
        contact.setFieldValue(Contact.EMAIL, jsonNode.get(0).get("email").toString());
        contact.setFieldValue(Contact.WORKS_FOR, jsonNode.get(0).get("worksFor"));
        contact.setFieldValue(Contact.FAX_NUMBER, jsonNode.get(0).get("faxNumber").toString());
        contact.setFieldValue(Contact.HOME_LOCATION, jsonNode.get(0).get("homeLocation"));
        contact.setFieldValue(Contact.WORK_LOCATION, jsonNode.get(0).get("workLocation"));
        contact.setFieldValue(Contact.TELEPHONE, jsonNode.get(0).get("telephone").toString());
        contact.setFieldValue(Contact.TYPE, jsonNode.get(0).get("@type").toString());

        output(contact);
    }

    @Override
    protected void provide() {
        super.provide();
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode){
        JsonNode contacts = jsonNode.get(GET_DOMAIN);
        for(JsonNode contact : contacts){
            getContactInfo(contact);
        }
    }
}
