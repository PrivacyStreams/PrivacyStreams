package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;


/**
 * Person
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "email",
        "familyName",
        "faxNumber",
        "givenName",
        "homeLocation",
        "jobTitle",
        "name",
        "telephone",
        "workLocation",
        "worksFor"
})
public class Person {

    @JsonProperty("email")
    private String email;
    @JsonProperty("familyName")
    private String familyName;
    @JsonProperty("faxNumber")
    private String faxNumber;
    @JsonProperty("givenName")
    private String givenName;
    /**
     * ContactPoint
     * <p>
     *
     *
     */
    @JsonProperty("homeLocation")
    private ContactPoint homeLocation;
    @JsonProperty("jobTitle")
    private String jobTitle;
    @JsonProperty("name")
    private String name;
    @JsonProperty("telephone")
    private String telephone;
    /**
     * ContactPoint
     * <p>
     *
     *
     */
    @JsonProperty("workLocation")
    private ContactPoint workLocation;
    /**
     * Organization
     * <p>
     *
     *
     */
    @JsonProperty("worksFor")
    private Organization worksFor;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The familyName
     */
    @JsonProperty("familyName")
    public String getFamilyName() {
        return familyName;
    }

    /**
     *
     * @param familyName
     *     The familyName
     */
    @JsonProperty("familyName")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     *
     * @return
     *     The faxNumber
     */
    @JsonProperty("faxNumber")
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     *
     * @param faxNumber
     *     The faxNumber
     */
    @JsonProperty("faxNumber")
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     *
     * @return
     *     The givenName
     */
    @JsonProperty("givenName")
    public String getGivenName() {
        return givenName;
    }

    /**
     *
     * @param givenName
     *     The givenName
     */
    @JsonProperty("givenName")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * ContactPoint
     * <p>
     *
     *
     * @return
     *     The homeLocation
     */
    @JsonProperty("homeLocation")
    public ContactPoint getHomeLocation() {
        return homeLocation;
    }

    /**
     * ContactPoint
     * <p>
     *
     *
     * @param homeLocation
     *     The homeLocation
     */
    @JsonProperty("homeLocation")
    public void setHomeLocation(ContactPoint homeLocation) {
        this.homeLocation = homeLocation;
    }

    /**
     *
     * @return
     *     The jobTitle
     */
    @JsonProperty("jobTitle")
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     *
     * @param jobTitle
     *     The jobTitle
     */
    @JsonProperty("jobTitle")
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The telephone
     */
    @JsonProperty("telephone")
    public String getTelephone() {
        return telephone;
    }

    /**
     *
     * @param telephone
     *     The telephone
     */
    @JsonProperty("telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * ContactPoint
     * <p>
     *
     *
     * @return
     *     The workLocation
     */
    @JsonProperty("workLocation")
    public ContactPoint getWorkLocation() {
        return workLocation;
    }

    /**
     * ContactPoint
     * <p>
     *
     *
     * @param workLocation
     *     The workLocation
     */
    @JsonProperty("workLocation")
    public void setWorkLocation(ContactPoint workLocation) {
        this.workLocation = workLocation;
    }

    /**
     * Organization
     * <p>
     *
     *
     * @return
     *     The worksFor
     */
    @JsonProperty("worksFor")
    public Organization getWorksFor() {
        return worksFor;
    }

    /**
     * Organization
     * <p>
     *
     *
     * @param worksFor
     *     The worksFor
     */
    @JsonProperty("worksFor")
    public void setWorksFor(Organization worksFor) {
        this.worksFor = worksFor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(email).append(familyName).append(faxNumber).append(givenName).append(homeLocation).append(jobTitle).append(name).append(telephone).append(workLocation).append(worksFor).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Person) == false) {
            return false;
        }
        Person rhs = ((Person) other);
        return new EqualsBuilder()
                .append(email, rhs.email)
                .append(familyName, rhs.familyName)
                .append(faxNumber, rhs.faxNumber)
                .append(givenName, rhs.givenName)
                .append(homeLocation, rhs.homeLocation)
                .append(jobTitle, rhs.jobTitle)
                .append(name, rhs.name)
                .append(telephone, rhs.telephone)
                .append(workLocation, rhs.workLocation)
                .append(worksFor, rhs.worksFor)
                .append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
