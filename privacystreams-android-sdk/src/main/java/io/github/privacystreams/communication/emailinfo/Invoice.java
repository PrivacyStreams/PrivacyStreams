
package io.github.privacystreams.communication.emailinfo;

import com.github.privacystreams.communication.email.Domain;
import com.github.privacystreams.communication.email.EmailInfoEntity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Invoice
 * <p>
 * 
 * 
 */
public class Invoice extends EmailInfoEntity {

    private String confirmationNumber;
    private String paymentStatus;
    /**
     * PriceSpecification
     * <p>
     * 
     * 
     */
    private PriceSpecification totalPaymentDue;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.BILL;
    }

    /**
     * 
     * @return
     *     The confirmationNumber
     */
    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    /**
     * 
     * @param confirmationNumber
     *     The confirmationNumber
     */
    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    /**
     * 
     * @return
     *     The paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * 
     * @param paymentStatus
     *     The paymentStatus
     */
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
    public void setTotalPaymentDue(PriceSpecification totalPaymentDue) {
        this.totalPaymentDue = totalPaymentDue;
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

}
