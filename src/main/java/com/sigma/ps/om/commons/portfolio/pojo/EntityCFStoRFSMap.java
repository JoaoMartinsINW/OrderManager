
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
    "cfsGUIDMap",
    "rfsGUIDMap"
})
public class EntityCFStoRFSMap {

    @JsonProperty("cfsGUIDMap")
    private Map<String, String> cfsGUIDMap;
    @JsonProperty("rfsGUIDMap")
    private List<RfsGUIDMap> rfsGUIDMap = new ArrayList<RfsGUIDMap>();
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("cfsGUIDMap")
    public Map<String, String> getCfsGUIDMap() {
        return cfsGUIDMap;
    }

    @JsonProperty("cfsGUIDMap")
    public void setCfsGUIDMap(Map<String, String> cfsGUIDMap) {
        this.cfsGUIDMap = cfsGUIDMap;
    }

    @JsonProperty("rfsGUIDMap")
    public List<RfsGUIDMap> getRfsGUIDMap() {
        return rfsGUIDMap;
    }

    @JsonProperty("rfsGUIDMap")
    public void setRfsGUIDMap(List<RfsGUIDMap> rfsGUIDMap) {
        this.rfsGUIDMap = rfsGUIDMap;
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
        return new ToStringBuilder(this).append("cfsGUIDMap", cfsGUIDMap).append("rfsGUIDMap", rfsGUIDMap).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(cfsGUIDMap).append(rfsGUIDMap).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EntityCFStoRFSMap) == false) {
            return false;
        }
        EntityCFStoRFSMap rhs = ((EntityCFStoRFSMap) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(cfsGUIDMap, rhs.cfsGUIDMap).append(rfsGUIDMap, rhs.rfsGUIDMap).isEquals();
    }

}
