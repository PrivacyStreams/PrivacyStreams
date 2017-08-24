
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
 * LodgingReservation
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

public class LodgingReservation extends Sift {

    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("broker")
    private Organization broker;
    @JsonProperty("checkinTime")
    private Date checkinTime;
    @JsonProperty("checkoutTime")
    private Date checkoutTime;
    /**
     * LodgingBusiness
     * <p>
     * 
     * 
     */
    @JsonProperty("reservationFor")
    private LodgingBusiness reservationFor;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.HOTEL;
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
     *     The checkinTime
     */
    @JsonProperty("checkinTime")
    public Date getCheckinTime() {
        return checkinTime;
    }

    /**
     * 
     * @param checkinTime
     *     The checkinTime
     */
    @JsonProperty("checkinTime")
    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    /**
     * 
     * @return
     *     The checkoutTime
     */
    @JsonProperty("checkoutTime")
    public Date getCheckoutTime() {
        return checkoutTime;
    }

    /**
     * 
     * @param checkoutTime
     *     The checkoutTime
     */
    @JsonProperty("checkoutTime")
    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    /**
     * LodgingBusiness
     * <p>
     * 
     * 
     * @return
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public LodgingBusiness getReservationFor() {
        return reservationFor;
    }

    /**
     * LodgingBusiness
     * <p>
     * 
     * 
     * @param reservationFor
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public void setReservationFor(LodgingBusiness reservationFor) {
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
        return new HashCodeBuilder().append(broker).append(checkinTime).append(checkoutTime).append(reservationFor).append(reservationId).append(reservationStatus).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LodgingReservation) == false) {
            return false;
        }
        LodgingReservation rhs = ((LodgingReservation) other);
        return new EqualsBuilder().append(broker, rhs.broker).append(checkinTime, rhs.checkinTime).append(checkoutTime, rhs.checkoutTime).append(reservationFor, rhs.reservationFor).append(reservationId, rhs.reservationId).append(reservationStatus, rhs.reservationStatus).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
