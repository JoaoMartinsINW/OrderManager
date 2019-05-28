
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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "description",
    "operations",
    "errorMappings",
    "defaultMessages"
})
public class MappingConfig {

    @JsonProperty("description")
    private java.lang.String description;
    @JsonProperty("operations")
    private List<Operation> operations = new ArrayList<Operation>();
    @JsonProperty("errorMappings")
    private List<ErrorMapping> errorMappings = new ArrayList<ErrorMapping>();
    @JsonProperty("defaultMessages")
    private Map<String, String> defaultMessages;
    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("description")
    public java.lang.String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @JsonProperty("operations")
    public List<Operation> getOperations() {
        return operations;
    }

    @JsonProperty("operations")
    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @JsonProperty("errorMappings")
    public List<ErrorMapping> getErrorMappings() {
        return errorMappings;
    }

    @JsonProperty("errorMappings")
    public void setErrorMappings(List<ErrorMapping> errorMappings) {
        this.errorMappings = errorMappings;
    }

    @JsonProperty("defaultMessages")
    public Map<String, String> getDefaultMessages() {
        return defaultMessages;
    }

    @JsonProperty("defaultMessages")
    public void setDefaultMessages(Map<String, String> defaultMessages) {
        this.defaultMessages = defaultMessages;
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
        return new ToStringBuilder(this).append("description", description).append("operations", operations).append("errorMappings", errorMappings).append("defaultMessages", defaultMessages).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(defaultMessages).append(description).append(errorMappings).append(operations).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MappingConfig) == false) {
            return false;
        }
        MappingConfig rhs = ((MappingConfig) other);
        return new EqualsBuilder().append(defaultMessages, rhs.defaultMessages).append(description, rhs.description).append(errorMappings, rhs.errorMappings).append(operations, rhs.operations).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
