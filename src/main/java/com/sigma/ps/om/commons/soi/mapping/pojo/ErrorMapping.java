
package com.sigma.ps.om.commons.soi.mapping.pojo;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "systemErrorCode",
    "omErrorCode",
    "omErrorMsg"
})
public class ErrorMapping {

    @JsonProperty("systemErrorCode")
    private String systemErrorCode;
    @JsonProperty("omErrorCode")
    private String omErrorCode;
    @JsonProperty("omErrorMsg")
    private String omErrorMsg;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("systemErrorCode")
    public String getSystemErrorCode() {
        return systemErrorCode;
    }

    @JsonProperty("systemErrorCode")
    public void setSystemErrorCode(String systemErrorCode) {
        this.systemErrorCode = systemErrorCode;
    }

    @JsonProperty("omErrorCode")
    public String getOmErrorCode() {
        return omErrorCode;
    }

    @JsonProperty("omErrorCode")
    public void setOmErrorCode(String omErrorCode) {
        this.omErrorCode = omErrorCode;
    }

    @JsonProperty("omErrorMsg")
    public String getOmErrorMsg() {
        return omErrorMsg;
    }

    @JsonProperty("omErrorMsg")
    public void setOmErrorMsg(String omErrorMsg) {
        this.omErrorMsg = omErrorMsg;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("systemErrorCode", systemErrorCode).append("omErrorCode", omErrorCode).append("omErrorMsg", omErrorMsg).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(systemErrorCode).append(omErrorCode).append(omErrorMsg).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ErrorMapping) == false) {
            return false;
        }
        ErrorMapping rhs = ((ErrorMapping) other);
        return new EqualsBuilder().append(systemErrorCode, rhs.systemErrorCode).append(omErrorCode, rhs.omErrorCode).append(omErrorMsg, rhs.omErrorMsg).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
