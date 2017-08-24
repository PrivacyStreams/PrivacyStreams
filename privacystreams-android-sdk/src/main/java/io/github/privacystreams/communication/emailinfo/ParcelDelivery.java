
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
 * ParcelDelivery
 * <p>
 * 
 * 
 */
public class ParcelDelivery extends EmailInfoEntity {

    private List<Product> itemShipped = new ArrayList<Product>();
    /**
     * Order
     * <p>
     * 
     * 
     */
    private Order partOfOrder;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    private Organization provider;
    private String trackingNumber;
    private String trackingUrl;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.SHIPMENT;
    }

    /**
     * 
     * @return
     *     The itemShipped
     */
    public List<Product> getItemShipped() {
        return itemShipped;
    }

    /**
     * 
     * @param itemShipped
     *     The itemShipped
     */
    public void setItemShipped(List<Product> itemShipped) {
        this.itemShipped = itemShipped;
    }

    /**
     * Order
     * <p>
     * 
     * 
     * @return
     *     The partOfOrder
     */
    public Order getPartOfOrder() {
        return partOfOrder;
    }

    /**
     * Order
     * <p>
     * 
     * 
     * @param partOfOrder
     *     The partOfOrder
     */
    public void setPartOfOrder(Order partOfOrder) {
        this.partOfOrder = partOfOrder;
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
     *     The trackingNumber
     */
    public String getTrackingNumber() {
        return trackingNumber;
    }

    /**
     * 
     * @param trackingNumber
     *     The trackingNumber
     */
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    /**
     * 
     * @return
     *     The trackingUrl
     */
    public String getTrackingUrl() {
        return trackingUrl;
    }

    /**
     * 
     * @param trackingUrl
     *     The trackingUrl
     */
    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
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
        return new HashCodeBuilder().append(itemShipped).append(partOfOrder).append(provider).append(trackingNumber).append(trackingUrl).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ParcelDelivery) == false) {
            return false;
        }
        ParcelDelivery rhs = ((ParcelDelivery) other);
        return new EqualsBuilder().append(itemShipped, rhs.itemShipped).append(partOfOrder, rhs.partOfOrder).append(provider, rhs.provider).append(trackingNumber, rhs.trackingNumber).append(trackingUrl, rhs.trackingUrl).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
