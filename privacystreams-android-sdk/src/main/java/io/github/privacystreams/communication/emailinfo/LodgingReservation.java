
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
 * LodgingReservation
 * <p>
 * 
 * 
 */
public class LodgingReservation extends EmailInfoEntity {

    /**
     * Organization
     * <p>
     * 
     * 
     */
    private Organization broker;
    private Date checkinTime;
    private Date checkoutTime;
    /**
     * LodgingBusiness
     * <p>
     * 
     * 
     */
    private LodgingBusiness reservationFor;
    private String reservationId;
    /**
     * ReservationStatus
     * <p>
     * 
     * 
     */
    private ReservationStatus reservationStatus;
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
    public void setBroker(Organization broker) {
        this.broker = broker;
    }

    /**
     * 
     * @return
     *     The checkinTime
     */
    public Date getCheckinTime() {
        return checkinTime;
    }

    /**
     * 
     * @param checkinTime
     *     The checkinTime
     */
    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    /**
     * 
     * @return
     *     The checkoutTime
     */
    public Date getCheckoutTime() {
        return checkoutTime;
    }

    /**
     * 
     * @param checkoutTime
     *     The checkoutTime
     */
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
    public void setReservationFor(LodgingBusiness reservationFor) {
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
