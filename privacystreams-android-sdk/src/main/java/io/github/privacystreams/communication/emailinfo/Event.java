package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

/**
 * Event
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "endDate",
        "location",
        "name",
        "startDate"
})
public class Event {

    @JsonProperty("endDate")
    private Date mEndDate;
    @JsonProperty("location")
    private Place mLocation;
    @JsonProperty("name")
    private String mName;
    @JsonProperty("startDate")
    private Date mStartDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The endDate
     */
    @JsonProperty("endDate")
    public Date getEndDate() {
        return mEndDate;
    }

    /**
     *
     * @param endDate
     *     The endDate
     */
    @JsonProperty("endDate")
    public void setEndDate(Date endDate) {
        this.mEndDate = endDate;
    }

    /**
     * Place
     * <p>
     *
     *
     * @return
     *     The location
     */
    @JsonProperty("location")
    public Place getLocation() {
        return mLocation;
    }

    /**
     * Place
     * <p>
     *
     *
     * @param location
     *     The location
     */
    @JsonProperty("location")
    public void setLocation(Place location) {
        this.mLocation = location;
    }

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return mName;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.mName = name;
    }

    /**
     *
     * @return
     *     The startDate
     */
    @JsonProperty("startDate")
    public Date getStartDate() {
        return mStartDate;
    }

    /**
     *
     * @param startDate
     *     The startDate
     */
    @JsonProperty("startDate")
    public void setStartDate(Date startDate) {
        this.mStartDate = startDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnore
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonIgnore
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mEndDate).append(mLocation).append(mName).append(mStartDate).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Event) == false) {
            return false;
        }
        Event rhs = ((Event) other);
        return new EqualsBuilder().append(mEndDate, rhs.mEndDate).append(mLocation, rhs.mLocation).append(mName, rhs.mName).append(mStartDate, rhs.mStartDate).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
