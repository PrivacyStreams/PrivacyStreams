
package io.github.privacystreams.communication.emailinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * TrainTrip
 * <p>
 * 
 * 
 */
public class TrainTrip {

    /**
     * TrainStation
     * <p>
     * 
     * 
     */
    private TrainStation arrivalStation;
    /**
     * TrainStation
     * <p>
     * 
     * 
     */
    private TrainStation departureStation;
    private Date departureTime;
    private String description;
    private String trainNumber;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * TrainStation
     * <p>
     * 
     * 
     * @return
     *     The arrivalStation
     */
    public TrainStation getArrivalStation() {
        return arrivalStation;
    }

    /**
     * TrainStation
     * <p>
     * 
     * 
     * @param arrivalStation
     *     The arrivalStation
     */
    public void setArrivalStation(TrainStation arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    /**
     * TrainStation
     * <p>
     * 
     * 
     * @return
     *     The departureStation
     */
    public TrainStation getDepartureStation() {
        return departureStation;
    }

    /**
     * TrainStation
     * <p>
     * 
     * 
     * @param departureStation
     *     The departureStation
     */
    public void setDepartureStation(TrainStation departureStation) {
        this.departureStation = departureStation;
    }

    /**
     * 
     * @return
     *     The departureTime
     */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
     * 
     * @param departureTime
     *     The departureTime
     */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The trainNumber
     */
    public String getTrainNumber() {
        return trainNumber;
    }

    /**
     * 
     * @param trainNumber
     *     The trainNumber
     */
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
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
        return new HashCodeBuilder().append(arrivalStation).append(departureStation).append(departureTime).append(description).append(trainNumber).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TrainTrip) == false) {
            return false;
        }
        TrainTrip rhs = ((TrainTrip) other);
        return new EqualsBuilder().append(arrivalStation, rhs.arrivalStation).append(departureStation, rhs.departureStation).append(departureTime, rhs.departureTime).append(description, rhs.description).append(trainNumber, rhs.trainNumber).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
