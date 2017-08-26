package io.github.privacystreams.communication.emailinfo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;


/**
 * EventReservation
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "provider",
        "reservationFor",
        "reservedTicket"
})
public class EventReservation extends Sift {

    /**
     * Organization
     * <p>
     *
     *
     */
    @JsonProperty("provider")
    private Organization provider;
    /**
     * Event
     * <p>
     *
     *
     */
    @JsonProperty("reservationFor")
    private Event reservationFor;
    @JsonProperty("reservedTicket")
    private List<Ticket> reservedTicket = new ArrayList<Ticket>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.EVENT;
    }

    /**
     * Organization
     * <p>
     *
     *
     * @return
     *     The provider
     */
    @JsonProperty("provider")
    public Organization getProvider() {
        return provider;
    }

    /**
     * Organization
     * <p>
     *
     *
     * @param provider
     *     The provider
     */
    @JsonProperty("provider")
    public void setProvider(Organization provider) {
        this.provider = provider;
    }

    /**
     * Event
     * <p>
     *
     *
     * @return
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public Event getReservationFor() {
        return reservationFor;
    }

    /**
     * Event
     * <p>
     *
     *
     * @param reservationFor
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public void setReservationFor(Event reservationFor) {
        this.reservationFor = reservationFor;
    }

    /**
     *
     * @return
     *     The reservedTicket
     */
    @JsonProperty("reservedTicket")
    public List<Ticket> getReservedTicket() {
        return reservedTicket;
    }

    /**
     *
     * @param reservedTicket
     *     The reservedTicket
     */
    @JsonProperty("reservedTicket")
    public void setReservedTicket(List<Ticket> reservedTicket) {
        this.reservedTicket = reservedTicket;
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
        return new HashCodeBuilder().append(provider).append(reservationFor).append(reservedTicket).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EventReservation) == false) {
            return false;
        }
        EventReservation rhs = ((EventReservation) other);
        return new EqualsBuilder().append(provider, rhs.provider).append(reservationFor, rhs.reservationFor).append(reservedTicket, rhs.reservedTicket).append(additionalProperties, rhs.additionalProperties).isEquals();
    }


}
