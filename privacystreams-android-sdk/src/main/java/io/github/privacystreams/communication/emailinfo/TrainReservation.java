
package io.github.privacystreams.communication.emailinfo;

import com.github.privacystreams.communication.email.Domain;
import com.github.privacystreams.communication.email.EmailInfoEntity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TrainReservation
 * <p>
 * 
 * 
 */
public class TrainReservation extends EmailInfoEntity {

    /**
     * Organization
     * <p>
     * 
     * 
     */
    private Organization provider;
    private List<TrainTrip> reservationFor = new ArrayList<TrainTrip>();
    private String reservationId;
    /**
     * ReservationStatus
     * <p>
     * 
     * 
     */
    private ReservationStatus reservationStatus;
    private List<Ticket> reservedTicket = new ArrayList<Ticket>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.TRAIN;
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
     * 
     * @return
     *     The reservationFor
     */
    public List<TrainTrip> getReservationFor() {
        return reservationFor;
    }

    /**
     * 
     * @param reservationFor
     *     The reservationFor
     */
    public void setReservationFor(List<TrainTrip> reservationFor) {
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
     *     The reservedTicket
     */
    public List<Ticket> getReservedTicket() {
        return reservedTicket;
    }

    /**
     * 
     * @param reservedTicket
     *     The reservedTicket
     */
    public void setReservedTicket(List<Ticket> reservedTicket) {
        this.reservedTicket = reservedTicket;
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
        return new HashCodeBuilder().append(provider).append(reservationFor).append(reservationId).append(reservationStatus).append(reservedTicket).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TrainReservation) == false) {
            return false;
        }
        TrainReservation rhs = ((TrainReservation) other);
        return new EqualsBuilder().append(provider, rhs.provider).append(reservationFor, rhs.reservationFor).append(reservationId, rhs.reservationId).append(reservationStatus, rhs.reservationStatus).append(reservedTicket, rhs.reservedTicket).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
