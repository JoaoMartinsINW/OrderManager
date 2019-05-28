
package com.sigma.ps.om.commons.portfolio.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "entityCharMap",
    "entityCFStoRFSMap"
})
public class Operation {

    @JsonProperty("name")
    private String name;
    @JsonProperty("entityCharMap")
    private List<EntityCharMap> entityCharMap = new ArrayList<EntityCharMap>();
    @JsonProperty("entityCFStoRFSMap")
    private List<EntityCFStoRFSMap> entityCFStoRFSMap = new ArrayList<EntityCFStoRFSMap>();
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

    @JsonProperty("entityCharMap")
    public List<EntityCharMap> getEntityCharMap() {
        return entityCharMap;
    }

    @JsonProperty("entityCharMap")
    public void setEntityCharMap(List<EntityCharMap> entityCharMap) {
        this.entityCharMap = entityCharMap;
    }

    @JsonProperty("entityCFStoRFSMap")
    public List<EntityCFStoRFSMap> getEntityCFStoRFSMap() {
        return entityCFStoRFSMap;
    }

    @JsonProperty("entityCFStoRFSMap")
    public void setEntityCFStoRFSMap(List<EntityCFStoRFSMap> entityCFStoRFSMap) {
        this.entityCFStoRFSMap = entityCFStoRFSMap;
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
        return new ToStringBuilder(this).append("name", name).append("entityCharMap", entityCharMap).append("entityCFStoRFSMap", entityCFStoRFSMap).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(entityCFStoRFSMap).append(additionalProperties).append(entityCharMap).toHashCode();
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
        return new EqualsBuilder().append(name, rhs.name).append(entityCFStoRFSMap, rhs.entityCFStoRFSMap).append(additionalProperties, rhs.additionalProperties).append(entityCharMap, rhs.entityCharMap).isEquals();
    }

}
