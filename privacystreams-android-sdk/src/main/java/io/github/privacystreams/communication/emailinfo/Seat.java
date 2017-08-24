
package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;


/**
 * Seat
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "seatNumber",
        "seatRow",
        "seatingType"
})
public class Seat {

    @JsonProperty("seatNumber")
    private String seatNumber;
    @JsonProperty("seatRow")
    private String seatRow;
    @JsonProperty("seatingType")
    private String seatingType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The seatNumber
     */
    @JsonProperty("seatNumber")
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * 
     * @param seatNumber
     *     The seatNumber
     */
    @JsonProperty("seatNumber")
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * 
     * @return
     *     The seatRow
     */
    @JsonProperty("seatRow")
    public String getSeatRow() {
        return seatRow;
    }

    /**
     * 
     * @param seatRow
     *     The seatRow
     */
    @JsonProperty("seatRow")
    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    /**
     * 
     * @return
     *     The seatingType
     */
    @JsonProperty("seatingType")
    public String getSeatingType() {
        return seatingType;
    }

    /**
     * 
     * @param seatingType
     *     The seatingType
     */
    @JsonProperty("seatingType")
    public void setSeatingType(String seatingType) {
        this.seatingType = seatingType;
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
        return new HashCodeBuilder().append(seatNumber).append(seatRow).append(seatingType).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Seat) == false) {
            return false;
        }
        Seat rhs = ((Seat) other);
        return new EqualsBuilder().append(seatNumber, rhs.seatNumber).append(seatRow, rhs.seatRow).append(seatingType, rhs.seatingType).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
