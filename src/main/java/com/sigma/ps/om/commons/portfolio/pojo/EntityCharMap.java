
package com.sigma.ps.om.commons.portfolio.pojo;

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
    "guidMap",
    "charMap"
})
public class EntityCharMap {

    @JsonProperty("guidMap")
    private Map<String, String> guidMap;
    @JsonProperty("charMap")
    private Map<String, String> charMap;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("guidMap")
    public Map<String, String> getGuidMap() {
        return guidMap;
    }

    @JsonProperty("guidMap")
    public void setGuidMap(Map<String, String> guidMap) {
        this.guidMap = guidMap;
    }

    @JsonProperty("charMap")
    public Map<String, String> getCharMap() {
        return charMap;
    }

    @JsonProperty("charMap")
    public void setCharMap(Map<String, String> charMap) {
        this.charMap = charMap;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return new ToStringBuilder(this).append("guidMap", guidMap).append("charMap", charMap).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(charMap).append(additionalProperties).append(guidMap).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EntityCharMap) == false) {
            return false;
        }
        EntityCharMap rhs = ((EntityCharMap) other);
        return new EqualsBuilder().append(charMap, rhs.charMap).append(additionalProperties, rhs.additionalProperties).append(guidMap, rhs.guidMap).isEquals();
    }

}
