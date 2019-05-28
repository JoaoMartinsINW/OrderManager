package om.unit.testing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "testSoiRequests" })
public class SoiTestCfg {

	@JsonProperty("testSoiRequests")
	private List<TestSoiRequest> testSoiRequests = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("testSoiRequests")
	public List<TestSoiRequest> getTestSoiRequests() {
		return testSoiRequests;
	}

	@JsonProperty("testSoiRequests")
	public void setTestSoiRequests(List<TestSoiRequest> testSoiRequests) {
		this.testSoiRequests = testSoiRequests;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "guid", "specKey", "testCoreAttrs", "testEntity" })
class TestCatalogEntity {

	@JsonProperty("guid")
	private String guid;
	@JsonProperty("specKey")
	private String specKey;
	@JsonProperty("testCoreAttrs")
	private List<TestCoreAttr> testCoreAttrs = null;
	@JsonProperty("testEntity")
	private TestEntity testEntity;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("guid")
	public String getGuid() {
		return guid;
	}

	@JsonProperty("guid")
	public void setGuid(String guid) {
		this.guid = guid;
	}

	@JsonProperty("specKey")
	public String getSpecKey() {
		return specKey;
	}

	@JsonProperty("specKey")
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}

	@JsonProperty("testCoreAttrs")
	public List<TestCoreAttr> getTestCoreAttrs() {
		return testCoreAttrs;
	}

	@JsonProperty("testCoreAttrs")
	public void setTestCoreAttrs(List<TestCoreAttr> testCoreAttrs) {
		this.testCoreAttrs = testCoreAttrs;
	}

	@JsonProperty("testEntity")
	public TestEntity getTestEntity() {
		return testEntity;
	}

	@JsonProperty("testEntity")
	public void setTestEntity(TestEntity testEntity) {
		this.testEntity = testEntity;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "testValues", "testPortfolioValues" })
class TestCharacteristic {

	@JsonProperty("name")
	private String name;
	@JsonProperty("testValues")
	private List<TestValue> testValues = null;
	@JsonProperty("testPortfolioValues")
	private List<TestPortfolioValue> testPortfolioValues = null;
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

	@JsonProperty("testValues")
	public List<TestValue> getTestValues() {
		return testValues;
	}

	@JsonProperty("testValues")
	public void setTestValues(List<TestValue> testValues) {
		this.testValues = testValues;
	}

	@JsonProperty("testPortfolioValues")
	public List<TestPortfolioValue> getTestPortfolioValues() {
		return testPortfolioValues;
	}

	@JsonProperty("testPortfolioValues")
	public void setTestPortfolioValues(List<TestPortfolioValue> testPortfolioValues) {
		this.testPortfolioValues = testPortfolioValues;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "action" })
class TestCharacteristicAction {

	@JsonProperty("action")
	private String action;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("action")
	public String getAction() {
		return action;
	}

	@JsonProperty("action")
	public void setAction(String action) {
		this.action = action;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "operation" })
class TestCharacteristicOperation {

	@JsonProperty("operation")
	private String operation;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("operation")
	public String getOperation() {
		return operation;
	}

	@JsonProperty("operation")
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "testCharacteristicOperation", "testCharacteristicAction" })
class TestComplexCharacteristic {

	@JsonProperty("name")
	private String name;
	@JsonProperty("testCharacteristicOperation")
	private TestCharacteristicOperation testCharacteristicOperation;
	@JsonProperty("testCharacteristicAction")
	private TestCharacteristicAction testCharacteristicAction;
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

	@JsonProperty("testCharacteristicOperation")
	public TestCharacteristicOperation getTestCharacteristicOperation() {
		return testCharacteristicOperation;
	}

	@JsonProperty("testCharacteristicOperation")
	public void setTestCharacteristicOperation(TestCharacteristicOperation testCharacteristicOperation) {
		this.testCharacteristicOperation = testCharacteristicOperation;
	}

	@JsonProperty("testCharacteristicAction")
	public TestCharacteristicAction getTestCharacteristicAction() {
		return testCharacteristicAction;
	}

	@JsonProperty("testCharacteristicAction")
	public void setTestCharacteristicAction(TestCharacteristicAction testCharacteristicAction) {
		this.testCharacteristicAction = testCharacteristicAction;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "value" })
class TestCoreAttr {

	@JsonProperty("name")
	private String name;
	@JsonProperty("value")
	private String value;
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

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "testCharacteristics", "testComplexCharacteristics" })
class TestEntity {

	@JsonProperty("testCharacteristics")
	private List<TestCharacteristic> testCharacteristics = null;
	@JsonProperty("testComplexCharacteristics")
	private List<TestComplexCharacteristic> testComplexCharacteristics = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("testCharacteristics")
	public List<TestCharacteristic> getTestCharacteristics() {
		return testCharacteristics;
	}

	@JsonProperty("testCharacteristics")
	public void setTestCharacteristics(List<TestCharacteristic> testCharacteristics) {
		this.testCharacteristics = testCharacteristics;
	}

	@JsonProperty("testComplexCharacteristics")
	public List<TestComplexCharacteristic> getTestComplexCharacteristics() {
		return testComplexCharacteristics;
	}

	@JsonProperty("testComplexCharacteristics")
	public void setTestComplexCharacteristics(List<TestComplexCharacteristic> testComplexCharacteristics) {
		this.testComplexCharacteristics = testComplexCharacteristics;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "testSoiRequestItems" })
class TestOperation {

	@JsonProperty("name")
	private String name;
	@JsonProperty("testSoiRequestItems")
	private List<TestSoiRequestItem> testSoiRequestItems = null;
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

	@JsonProperty("testSoiRequestItems")
	public List<TestSoiRequestItem> getTestSoiRequestItems() {
		return testSoiRequestItems;
	}

	@JsonProperty("testSoiRequestItems")
	public void setTestSoiRequestItems(List<TestSoiRequestItem> testSoiRequestItems) {
		this.testSoiRequestItems = testSoiRequestItems;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "portfolioValue" })
class TestPortfolioValue {

	@JsonProperty("portfolioValue")
	private String portfolioValue;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("portfolioValue")
	public String getPortfolioValue() {
		return portfolioValue;
	}

	@JsonProperty("portfolioValue")
	public void setPortfolioValue(String portfolioValue) {
		this.portfolioValue = portfolioValue;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "testOperations", "state", "type" })
class TestSoiRequest {

	@JsonProperty("name")
	private String name;
	@JsonProperty("testOperations")
	private List<TestOperation> testOperations = null;
	@JsonProperty("state")
	private String state;
	@JsonProperty("type")
	private String type;
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

	@JsonProperty("testOperations")
	public List<TestOperation> getTestOperations() {
		return testOperations;
	}

	@JsonProperty("testOperations")
	public void setTestOperations(List<TestOperation> testOperations) {
		this.testOperations = testOperations;
	}

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "displayID", "key", "specKey", "testCatalogEntity", "operation" })
class TestSoiRequestItem {

	@JsonProperty("displayID")
	private String displayID;
	@JsonProperty("key")
	private String key;
	@JsonProperty("specKey")
	private String specKey;
	@JsonProperty("testCatalogEntity")
	private TestCatalogEntity testCatalogEntity;
	@JsonProperty("operation")
	private String operation;
	@JsonProperty("isPonrReached")
	private boolean isPonrReached;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("displayID")
	public String getDisplayID() {
		return displayID;
	}

	@JsonProperty("displayID")
	public void setDisplayID(String displayID) {
		this.displayID = displayID;
	}


	@JsonProperty("isPonrReached")
	public boolean isPonrReached() {
		return isPonrReached;
	}

	@JsonProperty("isPonrReached")
	public void setPonr(boolean isPonrReached) {
		this.isPonrReached = isPonrReached;
	}

	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	@JsonProperty("specKey")
	public String getSpecKey() {
		return specKey;
	}

	@JsonProperty("specKey")
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}

	@JsonProperty("testCatalogEntity")
	public TestCatalogEntity getTestCatalogEntity() {
		return testCatalogEntity;
	}

	@JsonProperty("testCatalogEntity")
	public void setTestCatalogEntity(TestCatalogEntity testCatalogEntity) {
		this.testCatalogEntity = testCatalogEntity;
	}

	@JsonProperty("operation")
	public String getOperation() {
		return operation;
	}

	@JsonProperty("operation")
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "value" })
class TestValue {

	@JsonProperty("value")
	private String value;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}