
package io.github.privacystreams.communication.emailinfo;

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
 * Ticket
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "ticketUrl",
        "url",
        "ticketNumber",
        "ticketSear",
        "underName",
        "description"
})
public class Ticket {

    @JsonProperty("ticketUrl")
    private String ticketUrl;
    @JsonProperty("url")
    private String url;
    @JsonProperty("ticketNumber")
    private String ticketNumber;
    /**
     * Seat
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketedSeat")
    private Seat ticketedSeat;
    /**
     * Person
     * <p>
     * 
     * 
     */
    @JsonProperty("underName")
    private Person underName;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ticketUrl
     */
    @JsonProperty("ticketUrl")
    public String getTicketUrl() {
        return ticketUrl;
    }

    /**
     * 
     * @param ticketUrl
     *     The ticketUrl
     */
    @JsonProperty("ticketUrl")
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The ticketNumber
     */
    @JsonProperty("ticketNumber")
    public String getTicketNumber() {
        return ticketNumber;
    }

    /**
     * 
     * @param ticketNumber
     *     The ticketNumber
     */
    @JsonProperty("ticketNumber")
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    /**
     * Seat
     * <p>
     * 
     * 
     * @return
     *     The ticketedSeat
     */
    @JsonProperty("ticketedSeat")
    public Seat getTicketedSeat() {
        return ticketedSeat;
    }

    /**
     * Seat
     * <p>
     * 
     * 
     * @param ticketedSeat
     *     The ticketedSeat
     */
    @JsonProperty("ticketedSeat")
    public void setTicketedSeat(Seat ticketedSeat) {
        this.ticketedSeat = ticketedSeat;
    }

    /**
     * Person
     * <p>
     * 
     * 
     * @return
     *     The underName
     */
    @JsonProperty("underName")
    public Person getUnderName() {
        return underName;
    }

    /**
     * Person
     * <p>
     * 
     * 
     * @param underName
     *     The underName
     */
    @JsonProperty("underName")
    public void setUnderName(Person underName) {
        this.underName = underName;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
        return new HashCodeBuilder().append(ticketUrl).append(url).append(ticketNumber).append(ticketedSeat).append(underName).append(description).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Ticket) == false) {
            return false;
        }
        Ticket rhs = ((Ticket) other);
        return new EqualsBuilder().append(ticketUrl, rhs.ticketUrl).append(url, rhs.url).append(ticketNumber, rhs.ticketNumber).append(ticketedSeat, rhs.ticketedSeat).append(underName, rhs.underName).append(description, rhs.description).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
