
package io.github.privacystreams.communication.emailinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * LodgingBusiness
 * <p>
 * 
 * 
 */
public class LodgingBusiness {

    private String address;
    private String name;
    private String telephone;
    private Double xDays;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 
     * @param telephone
     *     The telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 
     * @return
     *     The xDays
     */
    public Double getXDays() {
        return xDays;
    }

    /**
     * 
     * @param xDays
     *     The x-days
     */
    public void setXDays(Double xDays) {
        this.xDays = xDays;
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
        return new HashCodeBuilder().append(address).append(name).append(telephone).append(xDays).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LodgingBusiness) == false) {
            return false;
        }
        LodgingBusiness rhs = ((LodgingBusiness) other);
        return new EqualsBuilder().append(address, rhs.address).append(name, rhs.name).append(telephone, rhs.telephone).append(xDays, rhs.xDays).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
