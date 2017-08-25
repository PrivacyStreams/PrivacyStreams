package io.github.privacystreams.communication.emailinfo;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;


/**
 * Contact
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "contacts"
})
public class Contact extends Sift {

    @JsonProperty("contacts")
    private List<Person> mContacts = new ArrayList<Person>();
    @JsonIgnore
    private Map<String, Object> mAdditionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.CONTACT;
    }

    /**
     *
     * @return
     *     The contacts
     */
    @JsonProperty("contacts")
    public List<Person> getContacts() {
        return mContacts;
    }

    /**
     *
     * @param contacts
     *     The contacts
     */
    @JsonProperty("contacts")
    public void setContacts(List<Person> contacts) {
        this.mContacts = contacts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.mAdditionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.mAdditionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mContacts).append(mAdditionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Contact) == false) {
            return false;
        }
        Contact rhs = ((Contact) other);
        return new EqualsBuilder().append(mContacts, rhs.mContacts).append(mAdditionalProperties, rhs.mAdditionalProperties).isEquals();
    }

    public static PStreamProvider getContact(){return new EmailContactProvider("","");}


    /**fields*/

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

    Contact(){}
}
