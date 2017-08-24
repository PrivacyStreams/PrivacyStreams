
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
 * Airport
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "iataCode",
        "name",
        "xRawName"
})
public class Airport{

    @JsonProperty("iataCode")
    private String iataCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("xRawName")
    private String xRawName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The iataCode
     */
    @JsonProperty("iataCode")
    public String getIataCode() {
        return iataCode;
    }

    /**
     *
     * @param iataCode
     *     The iataCode
     */
    @JsonProperty("iataCode")
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The xRawName
     */
    @JsonProperty("xRawName")
    public String getXRawName() {
        return xRawName;
    }

    /**
     *
     * @param xRawName
     *     The x-rawName
     */
    @JsonProperty("xRawName")
    public void setXRawName(String xRawName) {
        this.xRawName = xRawName;
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
        return new HashCodeBuilder().append(iataCode).append(name).append(xRawName).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Airport) == false) {
            return false;
        }
        Airport rhs = ((Airport) other);
        return new EqualsBuilder().append(iataCode, rhs.iataCode).append(name, rhs.name).append(xRawName, rhs.xRawName).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
