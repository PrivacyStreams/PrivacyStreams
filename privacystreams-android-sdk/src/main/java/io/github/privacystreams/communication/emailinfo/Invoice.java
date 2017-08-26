
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;


/**
 * Invoice
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "confirmationNumber",
        "paymentStatus",
        "totalPaymentDue"
})
public class Invoice extends Sift {

    @JsonProperty("confirmationNumber")
    private String confirmationNumber;
    @JsonProperty("paymentStatus")
    private String paymentStatus;
    /**
     * PriceSpecification
     * <p>
     *
     *
     */
    @JsonProperty("totalPaymentDue")
    private PriceSpecification totalPaymentDue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.BILL;
    }

    /**
     *
     * @return
     *     The confirmationNumber
     */
    @JsonProperty("confirmationNumber")
    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    /**
     *
     * @param confirmationNumber
     *     The confirmationNumber
     */
    @JsonProperty("confirmationNumber")
    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    /**
     *
     * @return
     *     The paymentStatus
     */
    @JsonProperty("paymentStatus")
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     *     The paymentStatus
     */
    @JsonProperty("paymentStatus")
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * PriceSpecification
     * <p>
     *
     *
     * @return
     *     The totalPaymentDue
     */
    @JsonProperty("totalPaymentDue")
    public PriceSpecification getTotalPaymentDue() {
        return totalPaymentDue;
    }

    /**
     * PriceSpecification
     * <p>
     *
     *
     * @param totalPaymentDue
     *     The totalPaymentDue
     */
    @JsonProperty("totalPaymentDue")
    public void setTotalPaymentDue(PriceSpecification totalPaymentDue) {
        this.totalPaymentDue = totalPaymentDue;
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
        return new HashCodeBuilder().append(confirmationNumber).append(paymentStatus).append(totalPaymentDue).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Invoice) == false) {
            return false;
        }
        Invoice rhs = ((Invoice) other);
        return new EqualsBuilder().append(confirmationNumber, rhs.confirmationNumber).append(paymentStatus, rhs.paymentStatus).append(totalPaymentDue, rhs.totalPaymentDue).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public PStreamProvider getInvoices(String api_key, String api_secret) {
        return new InvoiceProvider(api_key, api_secret);
    }

    /*fields*/
    @PSItemField(type = String.class)
    public static final String TYPE = "@type";

    @PSItemField(type = JsonNode.class)
    public static final String TOTAL_PAYMENT_DUE = "total_payment_due";

    @PSItemField(type = String.class)
    public static final String URL = "url";

    @PSItemField(type = String.class)
    public static final String PAYMENT_DUE_DATE = "payment_due_date";

    @PSItemField(type = String.class)
    public static final String DESCRIPTION = "description";

    @PSItemField(type = String.class)
    public static final String ACCOUNT_ID = "account_id";

    @PSItemField(type = String.class)
    public static final String ACCOUNT_BALANCE = "account_balance";

}
