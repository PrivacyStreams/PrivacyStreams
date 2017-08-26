
package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;

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
 * Order
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "acceptedOffer",
        "broker",
        "orderNumber",
        "seller"
})
public class Order extends Sift {

    @JsonProperty("acceptedOffer")
    private List<Offer> acceptedOffer = new ArrayList<Offer>();
    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("broker")
    private Organization broker;
    @JsonProperty("orderNumber")
    private String orderNumber;
    /**
     * Organization
     * <p>
     * 
     * 
     */
    @JsonProperty("seller")
    private Organization seller;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.PURCHASE;
    }

    /**
     * 
     * @return
     *     The acceptedOffer
     */
    @JsonProperty("acceptedOffer")
    public List<Offer> getAcceptedOffer() {
        return acceptedOffer;
    }

    /**
     * 
     * @param acceptedOffer
     *     The acceptedOffer
     */
    @JsonProperty("acceptedOffer")
    public void setAcceptedOffer(List<Offer> acceptedOffer) {
        this.acceptedOffer = acceptedOffer;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @return
     *     The broker
     */
    @JsonProperty("broker")
    public Organization getBroker() {
        return broker;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @param broker
     *     The broker
     */
    @JsonProperty("broker")
    public void setBroker(Organization broker) {
        this.broker = broker;
    }

    /**
     * 
     * @return
     *     The orderNumber
     */
    @JsonProperty("orderNumber")
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 
     * @param orderNumber
     *     The orderNumber
     */
    @JsonProperty("orderNumber")
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @return
     *     The seller
     */
    @JsonProperty("seller")
    public Organization getSeller() {
        return seller;
    }

    /**
     * Organization
     * <p>
     * 
     * 
     * @param seller
     *     The seller
     */
    @JsonProperty("seller")
    public void setSeller(Organization seller) {
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
        return new HashCodeBuilder().append(acceptedOffer).append(broker).append(orderNumber).append(seller).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order) == false) {
            return false;
        }
        Order rhs = ((Order) other);
        return new EqualsBuilder().append(acceptedOffer, rhs.acceptedOffer).append(broker, rhs.broker).append(orderNumber, rhs.orderNumber).append(seller, rhs.seller).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    /*Fields*/
    @PSItemField(type = JsonNode.class)
    public static final String ACCEPTED_OFFER = "accepted_offer";

    @PSItemField(type = String.class)
    public static final String ORDER_NUMBER = "order_number";

    @PSItemField(type = String.class)
    public static final String DESCRIPTION = "description";

    @PSItemField(type = JsonNode.class)
    public static final String PART_OF_INVOICE = "part_of_invoice";

    @PSItemField(type = JsonNode.class)
    public static final String SELLER = "seller";

    @PSItemField(type = JsonNode.class)
    public static final String BROKER = "broker";

    @PSItemField(type = String.class)
    public static final String PAYMENT_SCHEDULED = "payment-scheduled";

    @PSItemField(type = String.class)
    public static final String PRICE = "price";

    @PSItemField(type = String.class)
    public static final String SUBTOTAL = "sub_total";

    @PSItemField(type = String.class)
    public static final String REFUND = "refund";

    public static PStreamProvider getOrder(String api_key, String api_secret){
        return new OnlineOrderProvider(api_key,api_secret);
    }

}
