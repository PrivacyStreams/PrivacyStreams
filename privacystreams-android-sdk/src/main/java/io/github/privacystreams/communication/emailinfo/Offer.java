
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
 * Offer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "eligibleQuantity",
        "itemOffered",
        "price"
})
public class Offer {

    /**
     * QuantitativeValue
     * <p>
     * 
     * 
     */
    @JsonProperty("eligibleQuantity")
    private QuantitativeValue eligibleQuantity;
    /**
     * Product
     * <p>
     * 
     * 
     */
    @JsonProperty("itemOffered")
    private Product itemOffered;
    @JsonProperty("price")
    private String price;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * QuantitativeValue
     * <p>
     * 
     * 
     * @return
     *     The eligibleQuantity
     */
    public QuantitativeValue getEligibleQuantity() {
        return eligibleQuantity;
    }

    /**
     * QuantitativeValue
     * <p>
     * 
     * 
     * @param eligibleQuantity
     *     The eligibleQuantity
     */
    public void setEligibleQuantity(QuantitativeValue eligibleQuantity) {
        this.eligibleQuantity = eligibleQuantity;
    }

    /**
     * Product
     * <p>
     * 
     * 
     * @return
     *     The itemOffered
     */
    @JsonProperty("itemOffered")
    public Product getItemOffered() {
        return itemOffered;
    }

    /**
     * Product
     * <p>
     * 
     * 
     * @param itemOffered
     *     The itemOffered
     */
    @JsonProperty("itemOffered")
    public void setItemOffered(Product itemOffered) {
        this.itemOffered = itemOffered;
    }

    /**
     * 
     * @return
     *     The price
     */
    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
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
        return new HashCodeBuilder().append(eligibleQuantity).append(itemOffered).append(price).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Offer) == false) {
            return false;
        }
        Offer rhs = ((Offer) other);
        return new EqualsBuilder().append(eligibleQuantity, rhs.eligibleQuantity).append(itemOffered, rhs.itemOffered).append(price, rhs.price).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
