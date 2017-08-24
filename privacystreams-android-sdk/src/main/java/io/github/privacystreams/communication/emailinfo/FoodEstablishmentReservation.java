
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
 * FoodEstablishmentReservation
 * <p>
 * 
 * 
 */
public class FoodEstablishmentReservation extends EmailInfoEntity {

    private Double partySize;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    private Organization provider;
    /**
     * FoodEstablishment
     * <p>
     * 
     * 
     */
    private FoodEstablishment reservationFor;
    private String reservationId;
    /**
     * ReservationStatus
     * <p>
     * 
     * 
     */
    private ReservationStatus reservationStatus;
    private Date startTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.RESTAURANT;
    }

    /**
     * 
     * @return
     *     The partySize
     */
    public Double getPartySize() {
        return partySize;
    }

    /**
     * 
     * @param partySize
     *     The partySize
     */
    public void setPartySize(Double partySize) {
        this.partySize = partySize;
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
     * FoodEstablishment
     * <p>
     * 
     * 
     * @return
     *     The reservationFor
     */
    public FoodEstablishment getReservationFor() {
        return reservationFor;
    }

    /**
     * FoodEstablishment
     * <p>
     * 
     * 
     * @param reservationFor
     *     The reservationFor
     */
    public void setReservationFor(FoodEstablishment reservationFor) {
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
     * 
     * @return
     *     The startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
        return new HashCodeBuilder().append(partySize).append(provider).append(reservationFor).append(reservationId).append(reservationStatus).append(startTime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FoodEstablishmentReservation) == false) {
            return false;
        }
        FoodEstablishmentReservation rhs = ((FoodEstablishmentReservation) other);
        return new EqualsBuilder().append(partySize, rhs.partySize).append(provider, rhs.provider).append(reservationFor, rhs.reservationFor).append(reservationId, rhs.reservationId).append(reservationStatus, rhs.reservationStatus).append(startTime, rhs.startTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
