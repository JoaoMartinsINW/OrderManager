package om.unit.testing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.sigma.om.sdk.engine.interaction.InteractionState;
import com.sigma.om.sdk.engine.interaction.InteractionType;
import com.sigma.om.sdk.order.CatalogEntity;
import com.sigma.om.sdk.order.CatalogEntityBuilder;
import com.sigma.om.sdk.order.Characteristic;
import com.sigma.om.sdk.order.CharacteristicBuilder;
import com.sigma.om.sdk.order.ComplexCharacteristic;
import com.sigma.om.sdk.order.Entity;
import com.sigma.om.sdk.order.EntityBuilder;
import com.sigma.om.sdk.order.EntityLink;
import com.sigma.om.sdk.order.ItemOperation;
import com.sigma.om.sdk.order.OrderItem;
import com.sigma.om.sdk.order.OrderItemBuilder;
import com.sigma.om.sdk.order.OrderRef;
import com.sigma.om.sdk.order.Rate;
import com.sigma.om.sdk.order.enrichment.change.EnrichmentInfo;
import com.sigma.om.sdk.soi.framework.SoiContext;
import com.sigma.om.sdk.soi.framework.SoiRequest;
import com.sigma.om.sdk.soi.framework.SoiRequestItem;
import com.sigma.om.sdk.soi.interaction.ExternalInteractionState;
import com.sigma.om.sdk.spec.EntitySpec;
import com.sigma.om.sdk.spec.EntitySpecBuilder;
import com.sigma.om.soi.interaction.ExternalInteractionImpl;

public class TestingUtils {

	CharacteristicBuilder characteristicBuilder;
	CatalogEntityBuilder catalogEntityBuilder;
	EntitySpecBuilder entitySpecBuilder;
	EntityBuilder entityBuilder;
	OrderItemBuilder orderItemBuilder;

	TestingUtils() {
		characteristicBuilder = Characteristic.builder();
		catalogEntityBuilder = CatalogEntity.builder();
		entityBuilder = Entity.builder();
		orderItemBuilder = OrderItem.builder();
		entitySpecBuilder = EntitySpec.builder();
	}

	public Characteristic createCharacteristic(String name, Set<String> values, Set<String> portfolioValues) {
		characteristicBuilder.name(name);
		if (values != null)
			characteristicBuilder.values(values);
		if (portfolioValues != null)
			characteristicBuilder.portfolioValues(portfolioValues);
		return characteristicBuilder.build();
	}

	public CatalogEntity createCatalogEntity(String key, String specKey, Map<String, String> coreAttrs, Entity entity) {
		catalogEntityBuilder.key(key);
		if (coreAttrs != null)
			catalogEntityBuilder.addCoreAttributes(coreAttrs);
		if (entity != null)
			catalogEntityBuilder.entity(entity);
		return catalogEntityBuilder.build();
	}

	public EntitySpec createEntitySpec() {
		return entitySpecBuilder.build();
	}

	public EnrichmentInfo createEnrichmentInfo() {
		EnrichmentInfo enrichInfo = new EnrichmentInfo();
		return enrichInfo;
	}

	public Entity createEntity(Set<Characteristic> characteristics, Set<ComplexCharacteristic> complexCharacteristics,
			Set<EntityLink> entityLinks, Set<Rate> rates) {
		if (characteristics != null)
			entityBuilder.characteristic(characteristics);
		if (complexCharacteristics != null)
			entityBuilder.complexCharacteristics(complexCharacteristics);
		if (entityLinks != null)
			entityBuilder.entityLinks(entityLinks);
		if (rates != null)
			entityBuilder.rates(rates);
		return entityBuilder.build();
	}

	public OrderItem createOrderItem(String displayId, String key, String specKey, CatalogEntity catalogEntity,
			String createdBy, boolean isPonrReached, ItemOperation operation) {
		orderItemBuilder.key(key);
		orderItemBuilder.displayId(displayId);
		orderItemBuilder.catalogEntity(catalogEntity);
		orderItemBuilder.createdBy(createdBy);
		orderItemBuilder.createdDtm(new Timestamp(System.currentTimeMillis()));
		orderItemBuilder.modifiedDtm(new Timestamp((System.currentTimeMillis())));
		orderItemBuilder.isPonrReached(isPonrReached);
		orderItemBuilder.operation(operation);
		return orderItemBuilder.build();
	}

	public ExternalInteractionImpl createSoiExtInteractions(SoiRequest soiRequest) {
		ExternalInteractionImpl extInteraction = new ExternalInteractionImpl();
		// extInteraction.setId(SequenceMgr.getInstance().getNextId("EXT_INTERACTION_ID_SEQ"));
		extInteraction.setId("externalInteractionId");
		extInteraction.setInteractionRef(soiRequest.getRef());
		extInteraction.setAdapterName(soiRequest.getComponentName());
		extInteraction.setOrderId(-1L);
		extInteraction.setState(ExternalInteractionState.valueOf(soiRequest.getState().name()));
		extInteraction.setFlowName(soiRequest.getFlowname());
		extInteraction.setInteractionType(soiRequest.getInteractionType());
		extInteraction.setStop(Boolean.toString(soiRequest.isStopFlag()));
		extInteraction.setCreatedDtm(new Timestamp(0));

		return extInteraction;
	}

	public List<SoiRequestItem> createSoiRequestItems(OrderItem item) {
		List<SoiRequestItem> soiRequestItems = new ArrayList<SoiRequestItem>();
		SoiRequestItem soiRequestItem = new SoiRequestItem();
		//CREATE, AMEND, CANCEL,  NOCHANGE
		soiRequestItem.setItem(item);
		soiRequestItems.add(soiRequestItem);
		return soiRequestItems;
	}

	public SoiRequest createSoiRequest(String adapterName, String componentName, EnrichmentInfo enrichmentInfo,
			String flowName, List<SoiRequestItem> soiRequestItems, String operationName, InteractionState state,
			boolean stopFlag, InteractionType type) {
		SoiRequest soiRequest = new SoiRequest();
		soiRequest.setAdaptername(adapterName);
		soiRequest.setEnrichmentInfo(enrichmentInfo);
		soiRequest.setRequestItems(soiRequestItems);

		soiRequest.setSOIOperationRef(operationName);
		soiRequest.setSOIRef("SoiRef");
		soiRequest.setComponentName(componentName);
		soiRequest.setOrderRef(new OrderRef());
		soiRequest.setState(state);

		soiRequest.setFlowname(flowName);
		// DO, REDO, UNDO, FIX, SKIP, STOP
		soiRequest.setInteractionType(type);
		soiRequest.setStopFlag(stopFlag);
		return soiRequest;
	}

	public ItemOperation getItemOperation(String operation) {
		switch (operation) {
		case "CREATE":
			return ItemOperation.CREATE;
		case "AMEND":
			return ItemOperation.AMEND;
		case "NOCHANGE":
			return ItemOperation.NOCHANGE;
		case "CANCEL":
			return ItemOperation.CANCEL;
		default:
			return ItemOperation.CREATE;
		}
	}

	public InteractionState getInteractionState(String state) {
		switch (state) {
		case "RUNNING":
			return InteractionState.RUNNING;
		case "ABORTED":
			return InteractionState.ABORTED;
		case "DONE":
			return InteractionState.DONE;
		case "FAILED":
			return InteractionState.FAILED;
		case "FIXED":
			return InteractionState.FIXED;
		default:
			return InteractionState.RUNNING;
		}
	}

	public InteractionType getInteractionType(String type) {
		switch (type) {
		case "DO":
			return InteractionType.DO;
		case "REDO":
			return InteractionType.REDO;
		case "UNDO":
			return InteractionType.UNDO;
		case "FIX":
			return InteractionType.FIX;
		case "SKIP":
			return InteractionType.SKIP;
		case "STOP":
			return InteractionType.STOP;
		default:
			return InteractionType.DO;
		}
	}

	public void getSystemPropeties() {
		Properties prop = System.getProperties();
		Set<Object> keySet = prop.keySet();
		keySet.forEach(property -> System.out.println(
				"System property: {" + property.toString() + ": " + System.getProperty(property.toString()) + "}"));
	}

	public SoiContext initializeSoiTestContext(String filepath) throws FileNotFoundException, IOException, ParseException {

		SoiContext soiContext = new SoiContext();
		SoiTestCfg soiTestCfg = SoiTestMapping.getConfig(filepath);
		List<SoiRequest> soiRequestList = new ArrayList<SoiRequest>();
		SoiRequest soiRequest = null;
		List<SoiRequestItem> soiRequestItems = new ArrayList<SoiRequestItem>();
		List<TestSoiRequest> testSoiRequestList = soiTestCfg.getTestSoiRequests();

		for (TestSoiRequest request : testSoiRequestList) {
			List<TestOperation> operations = request.getTestOperations();
			for (TestOperation operation : operations) {
				List<TestSoiRequestItem> requestItems = operation.getTestSoiRequestItems();
				for (TestSoiRequestItem requestItem : requestItems) {

					List<TestCharacteristic> characteristics = requestItem.getTestCatalogEntity().getTestEntity()
							.getTestCharacteristics();
					List<TestComplexCharacteristic> complexCharacteristics = requestItem.getTestCatalogEntity()
							.getTestEntity().getTestComplexCharacteristics();
					Set<String> values = new HashSet<String>();
					Set<String> portfolioValues = new HashSet<String>();
					Set<Characteristic> charList = new HashSet<Characteristic>();

					for (TestCharacteristic characteristic : characteristics) {
						characteristic.getTestValues().forEach(charVal -> values.add(charVal.getValue()) );
						characteristic.getTestPortfolioValues()
								.forEach(charVal -> portfolioValues.add(charVal.getPortfolioValue()));
						charList.add(createCharacteristic(characteristic.getName(), values, portfolioValues));
					}

					Set<ComplexCharacteristic> complexCharList = new HashSet<ComplexCharacteristic>();
					Set<EntityLink> entityLinks = new HashSet<EntityLink>();
					Set<Rate> rates = new HashSet<Rate>();

					for (TestComplexCharacteristic complexCharacteristic : complexCharacteristics) {
						// create complex characteristic missing
					}

					Entity entity = createEntity(charList, complexCharList, entityLinks, rates);
					Map<String, String> coreAttrs = new HashMap<String, String>();

					List<TestCoreAttr> testCoreAttrs = requestItem.getTestCatalogEntity().getTestCoreAttrs();

					for (TestCoreAttr coreAttr : testCoreAttrs) {
						coreAttrs.put(coreAttr.getName(), coreAttr.getValue());
					}

					CatalogEntity catalogEntity = createCatalogEntity(requestItem.getTestCatalogEntity().getGuid(),
							requestItem.getTestCatalogEntity().getSpecKey(), coreAttrs, entity);

					OrderItem item = createOrderItem(requestItem.getDisplayID(), requestItem.getKey(),
							requestItem.getSpecKey(), catalogEntity, TestingConstants.CREATED_BY.value(),
							requestItem.isPonrReached(), getItemOperation(requestItem.getOperation()));

					SoiRequestItem newItem = new SoiRequestItem();
					newItem.setItem(item);
					soiRequestItems.add(newItem);
				}

				soiRequest = createSoiRequest(request.getName(), "componentName", null, "flowName",
						soiRequestItems, operation.getName(), getInteractionState(request.getState()), false, getInteractionType(request.getType()));

			}

		}

		ExternalInteractionImpl extInteraction = (ExternalInteractionImpl) createSoiExtInteractions(soiRequest);
		soiRequestList.add(soiRequest);
		soiContext.setSoiRequestList(soiRequestList);
		return soiContext;
	}



	public void printTestText(String text) {
		System.out.println("[testing] " + text);
	}

	public Map<String, String> setMapping(String adapter, Map<String, String> properties) {
		Map<String, String> adapterMapping = new HashMap<String, String>();
		for(Map.Entry<String, String> entry : properties.entrySet()) {
			adapterMapping.put(entry.getKey(), entry.getValue());
		}
		return adapterMapping;
	}

	public void setSystemProperties() {
		System.setProperty("md.node.name", "node1");
	}

}
