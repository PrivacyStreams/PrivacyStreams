
package io.github.privacystreams.communication.emailinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Ticket
 * <p>
 * 
 * 
 */
public class Ticket {

    private String ticketUrl;
    private String url;
    private String ticketNumber;
    /**
     * Seat
     * <p>
     * 
     * 
     */
    private Seat ticketedSeat;
    /**
     * Person
     * <p>
     * 
     * 
     */
    private Person underName;
    private String description;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ticketUrl
     */
    public String getTicketUrl() {
        return ticketUrl;
    }

    /**
     * 
     * @param ticketUrl
     *     The ticketUrl
     */
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The ticketNumber
     */
    public String getTicketNumber() {
        return ticketNumber;
    }

    /**
     * 
     * @param ticketNumber
     *     The ticketNumber
     */
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
    public void setUnderName(Person underName) {
        this.underName = underName;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

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
