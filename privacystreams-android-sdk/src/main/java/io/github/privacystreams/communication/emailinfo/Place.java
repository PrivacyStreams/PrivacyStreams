package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
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
 * Place
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "address",
        "name"
})
public class Place extends Sift{
        @JsonProperty("address")
        private String mAddress;
        @JsonProperty("name")
        private String mName;
        @JsonIgnore
        private Map<String, Object> mAdditionalProperties = new HashMap<String, Object>();

        /**
         *
         * @return
         *     The address
         */
        @JsonProperty("address")
        public String getAddress() {
            return mAddress;
        }

        /**
         *
         * @param address
         *     The address
         */
        @JsonProperty("address")
        public void setAddress(String address) {
            this.mAddress = address;
        }

        /**
         *
         * @return
         *     The name
         */
        @JsonProperty("name")
        public String getName() {
            return mName;
        }

        /**
         *
         * @param name
         *     The name
         */
        @JsonProperty("name")
        public void setName(String name) {
            this.mName = name;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.mAdditionalProperties;
        }

        @JsonAnyGetter
        public void setAdditionalProperty(String name, Object value) {
            this.mAdditionalProperties.put(name, value);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(mAddress).append(mName).append(mAdditionalProperties).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof Place) == false) {
                return false;
            }
            Place rhs = ((Place) other);
            return new EqualsBuilder().append(mAddress, rhs.mAddress).append(mName, rhs.mName).append(mAdditionalProperties, rhs.mAdditionalProperties).isEquals();
        }



}
