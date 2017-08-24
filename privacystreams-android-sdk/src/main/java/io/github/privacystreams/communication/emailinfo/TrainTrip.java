
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
 * TrainTrip
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "arrivalStation",
        "departureStation",
        "departureTime",
        "description",
        "trainNumber"
})
public class TrainTrip {

    /**
     * TrainStation
     * <p>
     * 
     * 
     */
    @JsonProperty("arrivalStation")
    private TrainStation arrivalStation;
    /**
     * TrainStation
     * <p>
     * 
     * 
     */
    @JsonProperty("departureStation")
    private TrainStation departureStation;
    @JsonProperty("departureTime")
    private Date departureTime;
    @JsonProperty("description")
    private String description;
    @JsonProperty("trainNumber")
    private String trainNumber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * TrainStation
     * <p>
     * 
     * 
     * @return
     *     The arrivalStation
     */
    @JsonProperty("arrivalStation")
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
    @JsonProperty("arrivalStation")
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
    @JsonProperty("departureStation")
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
    @JsonProperty("departureStation")
    public void setDepartureStation(TrainStation departureStation) {
        this.departureStation = departureStation;
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
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The trainNumber
     */
    @JsonProperty("trainNumber")
    public String getTrainNumber() {
        return trainNumber;
    }

    /**
     * 
     * @param trainNumber
     *     The trainNumber
     */
    @JsonProperty("trainNumber")
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
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
