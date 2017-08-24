
package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * RentalCarReservation
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "dropoffLocation",
        "dropoffTime",
        "pickupLocation",
        "pickupTime",
        "provider",
        "reservationFor",
        "reservationId",
        "reservationStatus",
        "undername"
})
public class RentalCarReservation extends Sift {

    /**
     * Place
     * <p>
     * 
     * 
     */
    @JsonProperty("dropoffLocation")
    private Place dropoffLocation;
    @JsonProperty("dropoffTime")
    private Date dropoffTime;
    /**
     * Place
     * <p>
     * 
     * 
     */
    @JsonProperty("pickupLocation")
    private Place pickupLocation;
    @JsonProperty("pickupTime")
    private Date pickupTime;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("provider")
    private Organization provider;
    /**
     * Car
     * <p>
     * 
     * 
     */
    @JsonProperty("reservationFor")
    private Car reservationFor;
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
    /**
     * Person
     * <p>
     * 
     * 
     */
    @JsonProperty("underName")
    private Person underName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.CAR_RENTAL;
    }

    /**
     * Place
     * <p>
     * 
     * 
     * @return
     *     The dropoffLocation
     */
    @JsonProperty("dropoffLocation")
    public Place getDropoffLocation() {
        return dropoffLocation;
    }

    /**
     * Place
     * <p>
     * 
     * 
     * @param dropoffLocation
     *     The dropoffLocation
     */
    @JsonProperty("dropoffLocation")
    public void setDropoffLocation(Place dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    /**
     * 
     * @return
     *     The dropoffTime
     */
    @JsonProperty("dropoffTime")
    public Date getDropoffTime() {
        return dropoffTime;
    }

    /**
     * 
     * @param dropoffTime
     *     The dropoffTime
     */
    @JsonProperty("dropoffTime")
    public void setDropoffTime(Date dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    /**
     * Place
     * <p>
     * 
     * 
     * @return
     *     The pickupLocation
     */
    @JsonProperty("pickupLocation")
    public Place getPickupLocation() {
        return pickupLocation;
    }

    /**
     * Place
     * <p>
     * 
     * 
     * @param pickupLocation
     *     The pickupLocation
     */
    @JsonProperty("pickupLocation")
    public void setPickupLocation(Place pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * 
     * @return
     *     The pickupTime
     */
    @JsonProperty("pickupTime")
    public Date getPickupTime() {
        return pickupTime;
    }

    /**
     * 
     * @param pickupTime
     *     The pickupTime
     */
    @JsonProperty("pickupTime")
    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
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
     * Car
     * <p>
     * 
     * 
     * @return
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public Car getReservationFor() {
        return reservationFor;
    }

    /**
     * Car
     * <p>
     * 
     * 
     * @param reservationFor
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
    public void setReservationFor(Car reservationFor) {
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
        return new HashCodeBuilder().append(dropoffLocation).append(dropoffTime).append(pickupLocation).append(pickupTime).append(provider).append(reservationFor).append(reservationId).append(reservationStatus).append(underName).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RentalCarReservation) == false) {
            return false;
        }
        RentalCarReservation rhs = ((RentalCarReservation) other);
        return new EqualsBuilder().append(dropoffLocation, rhs.dropoffLocation).append(dropoffTime, rhs.dropoffTime).append(pickupLocation, rhs.pickupLocation).append(pickupTime, rhs.pickupTime).append(provider, rhs.provider).append(reservationFor, rhs.reservationFor).append(reservationId, rhs.reservationId).append(reservationStatus, rhs.reservationStatus).append(underName, rhs.underName).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
