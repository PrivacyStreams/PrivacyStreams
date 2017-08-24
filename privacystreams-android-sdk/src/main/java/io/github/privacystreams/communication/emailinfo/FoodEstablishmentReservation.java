
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
 * FoodEstablishmentReservation
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "partySize",
        "provider",
        "reservationFor",
        "reservationId",
        "reservationStatus",
        "startTime"
})
public class FoodEstablishmentReservation extends Sift {

    @JsonProperty("partySize")
    private Double partySize;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("provider")
    private Organization provider;
    /**
     * FoodEstablishment
     * <p>
     * 
     * 
     */
    @JsonProperty("reservationFor")
    private FoodEstablishment reservationFor;
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
    @JsonProperty("startTime")
    private Date startTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.RESTAURANT;
    }

    /**
     * 
     * @return
     *     The partySize
     */
    @JsonProperty("partySize")
    public Double getPartySize() {
        return partySize;
    }

    /**
     * 
     * @param partySize
     *     The partySize
     */
    @JsonProperty("partySize")
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
     * FoodEstablishment
     * <p>
     * 
     * 
     * @return
     *     The reservationFor
     */
    @JsonProperty("reservationFor")
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
    @JsonProperty("reservationFor")
    public void setReservationFor(FoodEstablishment reservationFor) {
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
     *     The startTime
     */
    @JsonProperty("startTime")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The startTime
     */
    @JsonProperty("startTime")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
