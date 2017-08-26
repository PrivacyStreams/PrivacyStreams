
package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;


/**
 * ParcelDelivery
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "itemShipped",
        "partOfOrder",
        "provider",
        "trackingNumber",
        "trackingUrl"
})
public class ParcelDelivery extends Sift {

    @JsonProperty("itemShipped")
    private List<Product> itemShipped = new ArrayList<Product>();
    /**
     * Order
     * <p>
     * 
     * 
     */
    @JsonProperty("partOfOrder")
    private Order partOfOrder;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("provider")
    private Organization provider;
    @JsonProperty("trackingNumber")
    private String trackingNumber;
    @JsonProperty("trackingUrl")
    private String trackingUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.SHIPMENT;
    }

    /**
     * 
     * @return
     *     The itemShipped
     */
    @JsonProperty("itemShipped")
    public List<Product> getItemShipped() {
        return itemShipped;
    }

    /**
     * 
     * @param itemShipped
     *     The itemShipped
     */
    @JsonProperty("itemShipped")
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
    @JsonProperty("partOfOrder")
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
    @JsonProperty("partOfOrder")
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
     * 
     * @return
     *     The trackingNumber
     */
    @JsonProperty("trackingNumber")
    public String getTrackingNumber() {
        return trackingNumber;
    }

    /**
     * 
     * @param trackingNumber
     *     The trackingNumber
     */
    @JsonProperty("trackingNumber")
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    /**
     * 
     * @return
     *     The trackingUrl
     */
    @JsonProperty("trackingUrl")
    public String getTrackingUrl() {
        return trackingUrl;
    }

    /**
     * 
     * @param trackingUrl
     *     The trackingUrl
     */
    @JsonProperty("trackingUrl")
    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
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

    /*Fields*/
    @PSItemField(type = String.class)
    public static final String TRACKING_URL = "tracking_url";

    @PSItemField(type = String.class)
    public static final String TRACKING_NUMBER = "tracking_number";

    @PSItemField(type = String.class)
    public static final String EXPECTED_ARRIVAL_UNTIL = "expected_arrival_until";

    @PSItemField(type = List.class)
    public static final String ITEM_SHIPPED = "item_shipped";

    @PSItemField(type = JsonNode.class)
    public static final String PROVIDER = "provider";

    @PSItemField(type = JsonNode.class)
    public static final String PART_OF_ORDER = "part_of_order";

    public PStreamProvider getParcelDeliverys(String api_key, String api_secret){
        return new ParcelDeliveryProvider(api_key,api_secret);
    }
}
