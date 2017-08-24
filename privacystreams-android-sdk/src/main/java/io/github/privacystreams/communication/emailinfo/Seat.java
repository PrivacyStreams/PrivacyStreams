
package io.github.privacystreams.communication.emailinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Seat
 * <p>
 * 
 * 
 */
public class Seat {

    private String seatNumber;
    private String seatRow;
    private String seatingType;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The seatNumber
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * 
     * @param seatNumber
     *     The seatNumber
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * 
     * @return
     *     The seatRow
     */
    public String getSeatRow() {
        return seatRow;
    }

    /**
     * 
     * @param seatRow
     *     The seatRow
     */
    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    /**
     * 
     * @return
     *     The seatingType
     */
    public String getSeatingType() {
        return seatingType;
    }

    /**
     * 
     * @param seatingType
     *     The seatingType
     */
    public void setSeatingType(String seatingType) {
        this.seatingType = seatingType;
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
