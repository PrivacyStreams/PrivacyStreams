
package io.github.privacystreams.communication.emailinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * ProgramMembership
 * <p>
 * 
 * 
 */
public class ProgramMembership {

    private String membershipNumber;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The membershipNumber
     */
    public String getMembershipNumber() {
        return membershipNumber;
    }

    /**
     * 
     * @param membershipNumber
     *     The membershipNumber
     */
    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
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
        return new HashCodeBuilder().append(membershipNumber).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProgramMembership) == false) {
            return false;
        }
        ProgramMembership rhs = ((ProgramMembership) other);
        return new EqualsBuilder().append(membershipNumber, rhs.membershipNumber).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
