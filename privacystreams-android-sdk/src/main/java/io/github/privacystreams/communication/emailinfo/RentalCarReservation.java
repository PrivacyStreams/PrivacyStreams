
package io.github.privacystreams.communication.emailinfo;

import com.github.privacystreams.communication.email.Domain;
import com.github.privacystreams.communication.email.EmailInfoEntity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * RentalCarReservation
 * <p>
 * 
 * 
 */
public class RentalCarReservation extends EmailInfoEntity {

    /**
     * Place
     * <p>
     * 
     * 
     */
    private Place dropoffLocation;
    private Date dropoffTime;
    /**
     * Place
     * <p>
     * 
     * 
     */
    private Place pickupLocation;
    private Date pickupTime;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    private Organization provider;
    /**
     * Car
     * <p>
     * 
     * 
     */
    private Car reservationFor;
    private String reservationId;
    /**
     * ReservationStatus
     * <p>
     * 
     * 
     */
    private ReservationStatus reservationStatus;
    /**
     * Person
     * <p>
     * 
     * 
     */
    private Person underName;
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
    public void setDropoffLocation(Place dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    /**
     * 
     * @return
     *     The dropoffTime
     */
    public Date getDropoffTime() {
        return dropoffTime;
    }

    /**
     * 
     * @param dropoffTime
     *     The dropoffTime
     */
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
    public void setPickupLocation(Place pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * 
     * @return
     *     The pickupTime
     */
    public Date getPickupTime() {
        return pickupTime;
    }

    /**
     * 
     * @param pickupTime
     *     The pickupTime
     */
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
    public void setReservationFor(Car reservationFor) {
        this.reservationFor = reservationFor;
    }

    /**
     * 
     * @return
     *     The reservationId
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * 
     * @param reservationId
     *     The reservationId
     */
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
