
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
 * FlightReservation
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "broker",
        "programMembershipUsed",
        "reservationFor",
        "reservationId",
        "reservationStatus",
        "reservedTicket"
})
public class FlightReservation extends Sift {

    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("broker")
    private Organization broker;
    @JsonProperty("programMembershipUsed")
    private List<ProgramMembership> programMembershipUsed = new ArrayList<ProgramMembership>();
    @JsonProperty("reservationFor")
    private List<Flight> reservationFor = new ArrayList<Flight>();
    @JsonProperty("reservationId")
    private String reservationId;
    /**
     * ReservationStatus
     * <p>
     * 
     * 
     */
    @JsonProperty("reservationStatus")
    private ReservationStatus reservationStatus;
    @JsonProperty("reservationTicket")
    private List<Ticket> reservedTicket = new ArrayList<Ticket>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.FLIGHT;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @return
     *     The broker
     */
    @JsonProperty("broker")
    public Organization getBroker() {
        return broker;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @param broker
     *     The broker
     */
    @JsonProperty("broker")
    public void setBroker(Organization broker) {
        this.broker = broker;
    }

    /**
     * 
     * @return
     *     The programMembershipUsed
     */
    @JsonProperty("programMembershipUsed")
    public List<ProgramMembership> getProgramMembershipUsed() {
        return programMembershipUsed;
    }

    /**
     * 
     * @param programMembershipUsed
     *     The programMembershipUsed
     */
    @JsonProperty("programMembershipUsed")
    public void setProgramMembershipUsed(List<ProgramMembership> programMembershipUsed) {
        this.programMembershipUsed = programMembershipUsed;
    }

    /**
     * 
     * @return
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public List<Flight> getReservationFor() {
        return reservationFor;
    }

    /**
     * 
     * @param reservationFor
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public void setReservationFor(List<Flight> reservationFor) {
        this.reservationFor = reservationFor;
    }

    /**
     * 
     * @return
     *     The reservationId
     */
    @JsonProperty("reservationId")
    public String getReservationId() {
        return reservationId;
    }

    /**
     * 
     * @param reservationId
     *     The reservationId
     */
    @JsonProperty("reservationId")
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * ReservationStatus
     * <p>
     * 
     * 
     * @return
     *     The reservationStatus
     */
    @JsonProperty("reservationStatus")
    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    /**
     * ReservationStatus
     * <p>
     * 
     * 
     * @param reservationStatus
     *     The reservationStatus
     */
    @JsonProperty("reservationStatus")
    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
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
        return new HashCodeBuilder().append(broker).append(programMembershipUsed).append(reservationFor).append(reservationId).append(reservationStatus).append(reservedTicket).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FlightReservation) == false) {
            return false;
        }
        FlightReservation rhs = ((FlightReservation) other);
        return new EqualsBuilder().append(broker, rhs.broker).append(programMembershipUsed, rhs.programMembershipUsed).append(reservationFor, rhs.reservationFor).append(reservationId, rhs.reservationId).append(reservationStatus, rhs.reservationStatus).append(reservedTicket, rhs.reservedTicket).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
