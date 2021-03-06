
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
    "name",
    "sourcingInfo",
    "enrichInfo"
})
public class Operation {

    @JsonProperty("name")
    private String name;
    @JsonProperty("sourcingInfo")
    private SourcingInfo sourcingInfo;
    @JsonProperty("enrichInfo")
    private EnrichInfo enrichInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("sourcingInfo")
    public SourcingInfo getSourcingInfo() {
        return sourcingInfo;
    }

    @JsonProperty("sourcingInfo")
    public void setSourcingInfo(SourcingInfo sourcingInfo) {
        this.sourcingInfo = sourcingInfo;
    }

    @JsonProperty("enrichInfo")
    public EnrichInfo getEnrichInfo() {
        return enrichInfo;
    }

    @JsonProperty("enrichInfo")
    public void setEnrichInfo(EnrichInfo enrichInfo) {
        this.enrichInfo = enrichInfo;
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
        return new ToStringBuilder(this).append("name", name).append("sourcingInfo", sourcingInfo).append("enrichInfo", enrichInfo).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(sourcingInfo).append(enrichInfo).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Operation) == false) {
            return false;
        }
        Operation rhs = ((Operation) other);
        return new EqualsBuilder().append(name, rhs.name).append(sourcingInfo, rhs.sourcingInfo).append(enrichInfo, rhs.enrichInfo).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
