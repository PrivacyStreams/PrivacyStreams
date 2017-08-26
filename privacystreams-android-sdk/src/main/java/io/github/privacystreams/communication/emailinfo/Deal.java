package io.github.privacystreams.communication.emailinfo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Deal
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "isDeal",
})
public class Deal extends Sift {

    @JsonProperty("isDeal")
    private Boolean isDeal;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Domain getDomain() {
        return Domain.DEAL;
    }

    /**
     *
     * @return
     *     The isDeal
     */
    @JsonProperty("isDeal")
    public Boolean getIsDeal() {
        return isDeal;
    }

    /**
     *
     * @param isDeal
     *     The isDeal
     */
    @JsonProperty("isDeal")
    public void setIsDeal(Boolean isDeal) {
        this.isDeal = isDeal;
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
        return new HashCodeBuilder().append(isDeal).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Deal) == false) {
            return false;
        }
        Deal rhs = ((Deal) other);
        return new EqualsBuilder().append(isDeal, rhs.isDeal).append(additionalProperties, rhs.additionalProperties).isEquals();
    }



}
