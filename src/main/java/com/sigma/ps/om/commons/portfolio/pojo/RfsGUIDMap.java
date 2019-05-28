
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
    "entityDesc",
    "entityPath",
    "charMap"
})
public class RfsGUIDMap {

    @JsonProperty("entityDesc")
    private java.lang.String entityDesc;
    @JsonProperty("entityPath")
    private java.lang.String entityPath;
    @JsonProperty("charMap")
    private Map<String, String> charMap;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("entityDesc")
    public java.lang.String getEntityDesc() {
        return entityDesc;
    }

    @JsonProperty("entityDesc")
    public void setEntityDesc(java.lang.String entityDesc) {
        this.entityDesc = entityDesc;
    }

    @JsonProperty("entityPath")
    public java.lang.String getEntityPath() {
        return entityPath;
    }

    @JsonProperty("entityPath")
    public void setEntityPath(java.lang.String entityPath) {
        this.entityPath = entityPath;
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
        return new ToStringBuilder(this).append("entityDesc", entityDesc).append("entityPath", entityPath).append("charMap", charMap).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(entityPath).append(charMap).append(additionalProperties).append(entityDesc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RfsGUIDMap) == false) {
            return false;
        }
        RfsGUIDMap rhs = ((RfsGUIDMap) other);
        return new EqualsBuilder().append(entityPath, rhs.entityPath).append(charMap, rhs.charMap).append(additionalProperties, rhs.additionalProperties).append(entityDesc, rhs.entityDesc).isEquals();
    }

}
