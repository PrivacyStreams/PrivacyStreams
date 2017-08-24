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
 * Flight
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "arrivalAirport",
        "arrivalTime",
        "departureAirport",
        "departureTime",
        "flightNumber",
        "seller"
})
public class Flight {

    /**
     * Airport
     * <p>
     *
     *
     */
    @JsonProperty("arrivalAirport")
    private Airport arrivalAirport;
    @JsonProperty("arrivalTime")
    private Date arrivalTime;
    /**
     * Airport
     * <p>
     *
     *
     */
    @JsonProperty("departureAirport")
    private Airport departureAirport;
    @JsonProperty("departureTime")
    private Date departureTime;
    @JsonProperty("flightNumber")
    private String flightNumber;
    /**
     * Airline
     * <p>
     *
     *
     */
    @JsonProperty("seller")
    private Airline seller;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Airport
     * <p>
     *
     *
     * @return
     *     The arrivalAirport
     */
    @JsonProperty("arrivalAirport")
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Airport
     * <p>
     *
     *
     * @param arrivalAirport
     *     The arrivalAirport
     */
    @JsonProperty("arrivalAirport")
    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    /**
     *
     * @return
     *     The arrivalTime
     */
    @JsonProperty("arrivalTime")
    public Date getArrivalTime() {
        return arrivalTime;
    }

    /**
     *
     * @param arrivalTime
     *     The arrivalTime
     */
    @JsonProperty("arrivalTime")
    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Airport
     * <p>
     *
     *
     * @return
     *     The departureAirport
     */
    @JsonProperty("departureAirport")
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    /**
     * Airport
     * <p>
     *
     *
     * @param departureAirport
     *     The departureAirport
     */
    @JsonProperty("departureAirport")
    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    /**
     *
     * @return
     *     The departureTime
     */
    @JsonProperty("departureTime")
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
     *
     * @param departureTime
     *     The departureTime
     */
    @JsonProperty("departureTime")
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /**
     *
     * @return
     *     The flightNumber
     */
    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     *
     * @param flightNumber
     *     The flightNumber
     */
    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Airline
     * <p>
     *
     *
     * @return
     *     The seller
     */
    @JsonProperty("seller")
    public Airline getSeller() {
        return seller;
    }

    /**
     * Airline
     * <p>
     *
     *
     * @param seller
     *     The seller
     */
    @JsonProperty("seller")
    public void setSeller(Airline seller) {
        this.seller = seller;
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
        return new HashCodeBuilder().append(arrivalAirport).append(arrivalTime).append(departureAirport).append(departureTime).append(flightNumber).append(seller).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Flight) == false) {
            return false;
        }
        Flight rhs = ((Flight) other);
        return new EqualsBuilder().append(arrivalAirport, rhs.arrivalAirport).append(arrivalTime, rhs.arrivalTime).append(departureAirport, rhs.departureAirport).append(departureTime, rhs.departureTime).append(flightNumber, rhs.flightNumber).append(seller, rhs.seller).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
