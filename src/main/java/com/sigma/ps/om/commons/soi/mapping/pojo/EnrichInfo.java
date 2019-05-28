
package com.sigma.ps.om.commons.soi.mapping.pojo;

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
import com.sigma.om.sdk.order.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entities",
    "orderHeaders"
})
public class EnrichInfo {

    @JsonProperty("entities")
    private List<Entity> entities = new ArrayList<Entity>();
    @JsonProperty("orderHeaders")
    private Map<String, String> orderHeaders;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("entities")
    public List<Entity> getEntities() {
        return entities;
    }

    @JsonProperty("entities")
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @JsonProperty("orderHeaders")
    public Map<String, String> getOrderHeaders() {
        return orderHeaders;
    }

    @JsonProperty("orderHeaders")
    public void setOrderHeaders(Map<String, String> orderHeaders) {
        this.orderHeaders = orderHeaders;
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
        return new ToStringBuilder(this).append("entities", entities).append("orderHeaders", orderHeaders).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(orderHeaders).append(entities).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EnrichInfo) == false) {
            return false;
        }
        EnrichInfo rhs = ((EnrichInfo) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(orderHeaders, rhs.orderHeaders).append(entities, rhs.entities).isEquals();
    }

}
