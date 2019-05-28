package com.sigma.ps.om.commons.catalog;

import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.cmn.JsonUtils;
import com.sigma.om.sdk.engine.interaction.InteractionItem;
import com.sigma.om.sdk.order.CatalogEntity;
import com.sigma.om.sdk.order.Characteristic;
import com.sigma.om.sdk.order.ItemAction;
import com.sigma.om.sdk.order.Order;
import com.sigma.om.sdk.order.OrderItem;
import com.sigma.om.sdk.order.OrderRef;
import com.sigma.om.sdk.order.enrichment.change.CatalogEntityChange;
import com.sigma.om.sdk.order.enrichment.change.CharacteristicChange;
import com.sigma.om.sdk.order.enrichment.change.OrderChange;
import com.sigma.om.sdk.order.enrichment.change.OrderItemChange;
import com.sigma.om.sdk.repo.locator.OrderRepositoryLocator;
import com.sigma.om.sdk.soi.framework.SoiRequest;
import com.sigma.om.sdk.soi.framework.SoiRequestItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogUtils {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogUtils.class);

    public static OrderChange addAtrritbuteInOrderHeader(OrderChange orderChange, String itemName, String itemValue) {
        LOGGER.info("Inside addAtrritbuteInOrderHeader");
        final CharacteristicChange charChange = new CharacteristicChange();
        charChange.addValue(itemValue);
        charChange.setName(itemName);
        orderChange.addCharacteristic(charChange);
        return orderChange;
    }

    public static OrderChange addAtrritbuteInOrderHeader(String itemName, String itemValue) {
        LOGGER.info("Inside addAtrritbuteInOrderHeader");

        final OrderChange orderChange = new OrderChange();
        final CharacteristicChange charChange = new CharacteristicChange();
        charChange.addValue(itemValue);
        charChange.setName(itemName);
        orderChange.addCharacteristic(charChange);

        return orderChange;
    }

    /**
     * Method responsible to check changes by each catalog entity parameters
     *
     * @param soiRequest
     * @param charValueName
     * @return
     */
    public static String checkForCharChangeEntity(SoiRequest soiRequest, String charValueName) {

        final Map<String, OrderItemChange> orderitem = soiRequest.getEnrichmentInfo().getIpOrderChange().getItems();
        for (final OrderItemChange item : orderitem.values()) {
            for (final CharacteristicChange charChange : item.getCatalogEntity().getCharacteristics()) {
                final String charSet = charChange.getName();
                if (charSet.equalsIgnoreCase(charValueName)) {
                    LOGGER.debug("newValue in case of Retry : {}", charChange.getValues().iterator().next());
                    return charChange.getValues().iterator().next();
                }

            }
        }
        return null;
    }

    /**
     * Method responsible to check changes made for Order Item's catalog entity parameters
     *
     * @param soiRequest
     * @param orderItemKey
     * @param charValueName
     * @return
     */
    public static String checkForCharChangeEntity(SoiRequest soiRequest, String orderItemKey, String charValueName) {
        if (soiRequest.getEnrichmentInfo() != null && soiRequest.getEnrichmentInfo().getIpOrderChange() != null) {
            final Map<String, OrderItemChange> oItems = soiRequest.getEnrichmentInfo().getIpOrderChange().getItems();
            if (!oItems.isEmpty() && oItems.containsKey(orderItemKey)) {
                for (final CharacteristicChange charChange : oItems.get(orderItemKey).getCatalogEntity()
                        .getCharacteristics()) {
                    final String charSet = charChange.getName();
                    if (charSet.equalsIgnoreCase(charValueName)) {
                        LOGGER.debug("newValue in case of Retry : {}", charChange.getValues().iterator().next());
                        return charChange.getValues().iterator().next();
                    }

                }
            }
        }
        return null;
    }

    public static OrderItemChange createOrderItemChange(String oItemKey, Map<String, String> charVal) {
        final OrderItemChange itemChange = new OrderItemChange();
        itemChange.setKey(oItemKey);
        final CatalogEntityChange entityChange = new CatalogEntityChange();
        for (final Entry<String, String> entry : charVal.entrySet()) {
            final CharacteristicChange characChange = new CharacteristicChange();
            LOGGER.debug("createOrderItemChange, key {}, value {} ", entry.getKey(), entry.getValue());
            characChange.setName(entry.getKey());
            characChange.addValue(entry.getValue());
            entityChange.addCharacteristic(characChange);
        }
        itemChange.setCatalogEntity(entityChange);
        return itemChange;
    }

    public static OrderItemChange createOrderItemChange(String oItemKey, String tvLanCharName, String value) {
        final OrderItemChange itemChange = new OrderItemChange();
        final CatalogEntityChange entityChange = new CatalogEntityChange();
        final CharacteristicChange characChange = new CharacteristicChange();
        characChange.setName(tvLanCharName);
        characChange.addValue(value);
        entityChange.addCharacteristic(characChange);
        itemChange.setCatalogEntity(entityChange);
        itemChange.setKey(oItemKey);
        return itemChange;
    }

    /**
     * @param catalogEntity
     * @param charName
     * @return value of charName Boolean
     */
    public static Boolean getBooleanValueFromEntity(CatalogEntity catalogEntity, String charName) {
        if (null != charName && null != catalogEntity && catalogEntity.isCharacteristicPopulated(charName)) {
            LOGGER.debug("characteristic {} , value {} ", charName,
                    catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
            return Boolean.parseBoolean(catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
        } else {
            LOGGER.info("characteristic {} not populated", charName);
            return null;
        }
    }

    /**
     * Get list of CatalogEntities for GUID from order
     *
     * @param order
     * @param guid
     * @return List of CatalogEntity
     */
    public static List<CatalogEntity> getCatalogEntitiesByGuid(Order order, String guid) {
        final List<CatalogEntity> catalogEntities = new ArrayList<>();
        for (final OrderItem oi : order.getAllOrderItem()) {
            LOGGER.debug(" entity name : {}, spec key {}", oi.getCatalogEntity().getSpecFQNameWithoutPath(),
                    oi.getCatalogEntity().getSpecKey());
            if (StringUtils.equalsIgnoreCase(oi.getCatalogEntity().getSpec().getElementGUID(), guid)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(oi.getCatalogEntity()));
                catalogEntities.add(oi.getCatalogEntity());
            }
        }
        LOGGER.info("{} CatalogEntity found for guid {}", catalogEntities.size(), guid);
        return catalogEntities;
    }

    /**
     * Get list of CatalogEntities for GUID from soiRequest
     *
     * @param SoiRequest soiRequest
     * @param String guid
     * @return List of CatalogEntity
     */
    public static List<CatalogEntity> getCatalogEntitiesByGuid(SoiRequest soiRequest, String guid) {
        final List<CatalogEntity> catalogEntities = new ArrayList<>();
        for (final SoiRequestItem soiRequestItem : soiRequest.getRequestItems()) {
            LOGGER.debug("Catalog Entity with Name {} ",
                    soiRequestItem.getItem().getCatalogEntity().getSpecFQNameWithoutPath());
            if (StringUtils.equalsIgnoreCase(soiRequestItem.getItem().getCatalogEntity().getSpec().getElementGUID(),
                    guid)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(soiRequestItem.getItem().getCatalogEntity()));
                catalogEntities.add(soiRequestItem.getItem().getCatalogEntity());
            }
        }
        LOGGER.info("{} CatalogEntity found for guid {}", catalogEntities.size(), guid);
        return catalogEntities;
    }

    /**
     * Get CatalogEntity from order
     *
     * @param order
     * @param guid
     * @return CatalogEntity
     */
    public static CatalogEntity getCatalogEntityByGuid(Order order, String guid) {
        for (final OrderItem oi : order.getAllOrderItem()) {
            LOGGER.debug(" entity name : {}, spec key {}", oi.getCatalogEntity().getSpecFQNameWithoutPath(),
                    oi.getCatalogEntity().getSpecKey());
            if (StringUtils.equalsIgnoreCase(oi.getCatalogEntity().getSpec().getElementGUID(), guid)) {
                LOGGER.info("return entity : {}", JsonUtils.toString(oi.getCatalogEntity()));
                return oi.getCatalogEntity();
            }
        }
        LOGGER.info("Unable to find the CatalogEntity for guid {}", guid);
        return null;
    }

    /**
     * Get CatalogEntity from soiRequest
     *
     * @param soiRequest
     * @param guid
     * @return CatalogEntity
     */
    public static CatalogEntity getCatalogEntityByGuid(SoiRequest soiRequest, String guid) {
        for (final SoiRequestItem soiRequestItem : soiRequest.getRequestItems()) {
            LOGGER.debug("Catalog Entity with Name {} ",
                    soiRequestItem.getItem().getCatalogEntity().getSpecFQNameWithoutPath());
            if (StringUtils.equals(soiRequestItem.getItem().getCatalogEntity().getSpec().getElementGUID(), guid)) {
                LOGGER.debug(" found Catalog Entity with Name {} ",
                        soiRequestItem.getItem().getCatalogEntity().getSpecFQNameWithoutPath());
                return soiRequestItem.getItem().getCatalogEntity();
            }
        }
        LOGGER.warn(" Catalog Entity not found for guid {}", guid);
        return null;
    }

    /**
     * To Get the CFSServiceName value from the CFS entity of given RFS by checking the
     *
     * Check the Product ID is populated on CFS if populated get the value and compare both the
     * Product ID value & SIK value if matches return the CFSCserviceName value
     *
     *
     */
    public static String getCFSServiceName(Order order, String sikValue, String orderItemKey, boolean found) {
        LOGGER.debug("getCFSServiceName ,orderID {},sikValue {},orderItemKey {} ", order.getKey(), sikValue,
                orderItemKey);
        String cfsServiceName = "";
        if (!CommonUtils.isEmpty(order) && !found) {
            if (!CommonUtils.isEmpty(order.getOrderItem(orderItemKey))) {
                final OrderItem parentItem = order.getOrderItem(order.getOrderItem(orderItemKey).getParentItemKey());
                if (!CommonUtils.isEmpty(parentItem)) {
                    LOGGER.debug("parentItem Catalog Entity with Name {} , DisplayId {}  ",
                            parentItem.getCatalogEntity().getSpecFQNameWithoutPath(), parentItem.getDisplayId());
                    if (parentItem.getCatalogEntity().isCharacteristicPopulated("Product ID")) {
                        final String productIdValue = parentItem.getCatalogEntity().getEntity()
                                .getCharacteristic("Product ID").getFirstValue();
                        if (StringUtils.equals(productIdValue, sikValue)) {
                            if (parentItem.getCatalogEntity().isCharacteristicPopulated("CFSCserviceName")) {
                                cfsServiceName = parentItem.getCatalogEntity().getEntity()
                                        .getCharacteristic("CFSCserviceName").getFirstValue();
                                LOGGER.info("return found CFS DisplayId {}, CFSServiceName value {} ",
                                        parentItem.getDisplayId(), cfsServiceName);
                                found = true;
                                return cfsServiceName;
                            }
                        }
                    } else {
                        if (!CommonUtils.isEmpty(parentItem) && !CommonUtils.isEmpty(parentItem.getParentItemKey())
                                && !found) {
                            LOGGER.info("ParentItemKey {},  found value {}", parentItem.getParentItemKey(), found);
                            LOGGER.debug("calling getCFSServiceName for orderItemKey {} ,DisplayId {} ",
                                    parentItem.getKey(), parentItem.getDisplayId());
                            return getCFSServiceName(order, sikValue, parentItem.getKey(), found);
                        }
                    }
                }
            }
        }
        LOGGER.info(" before end of method , found {} cfsServiceName {}", found, cfsServiceName);
        return cfsServiceName;
    }

    /**
     * Method to get a characteristic from a specific entity
     *
     * @param parentEntity
     * @param characteristic
     * @return
     */
    public static Characteristic getCharacteristicFromEntity(CatalogEntity parentEntity, String characteristic) {
        final Collection<Characteristic> col = parentEntity.getEntity().getCharacteristics();
        LOGGER.debug("Characteristics collection size: {}", col.size());
        LOGGER.debug("Characteristic in method to search: {}", characteristic);
        for (final Characteristic c : col) {
            LOGGER.debug("CHAR NAME : {}\n CHAR firstvalue: {}\n CHAR portfoliofirstvalue: {}", c.getName(),
                    c.getFirstValue(), c.getFirstPortfolioValue());
            if (c.getName().equals(characteristic)) {
                return c;
            }
        }
        LOGGER.debug(" CHAR NAME : {} , not found for parentEntity {} , path {}", characteristic,
                parentEntity.getSpecFQNameWithoutPath(), parentEntity.getSpecKey());
        return null;
    }

    /**
     * returns the child order item key for referenced GUID
     *
     * @param orderRef -- Current Order ref
     * @param orderItem -- parent order item
     * @param guid -- child entity guid
     * @return
     */
    public static OrderItem getChildOrderItemByGuid(OrderRef orderRef, OrderItem orderItem, String guid) {

        if (orderItem == null)
            return null;

        if (orderItem.getCatalogEntity().getSpec().getElementGUID().equals(guid))
            return orderItem;

        if (orderItem.getSubOrderItemKeys() != null && !orderItem.getSubOrderItemKeys().isEmpty()) {
            final Set<OrderItem> subOrderItems = OrderRepositoryLocator.getInstance().getOrderItem(
                    orderItem.getSubOrderItemKeys(), orderRef.getKey(), orderRef.getMajorVersion(),
                    orderRef.getMinorVersion());
            LOGGER.debug("Found  {} child Items of GUID {} inside {} {}", subOrderItems.size(), guid,
                    orderItem.getKey(), orderItem.getCatalogEntity().getSpec().getName());
            for (final OrderItem item : subOrderItems) {
                final OrderItem childItem = getChildOrderItemByGuid(orderRef, item, guid);
                if (childItem != null)
                    return childItem;
            }
        }
        return null;
    }

    /**
     * returns a list of child order item key for referenced GUID
     *
     * @param orderRef -- Current Order ref
     * @param orderItem -- parent order item
     * @param guid -- child entity guid
     * @return
     */
    public static List<OrderItem> getChildOrderItemsByGuid(SoiRequest soiRequest, String guid) {

        final List<OrderItem> orderChilds = new ArrayList<>();
        final OrderRef orderRef = soiRequest.getOrderRef();
        for (final OrderItem orderItem : getOrderItemsByGuid(soiRequest, guid)) {

            if (orderItem == null)
                return orderChilds;

            if (orderItem.getCatalogEntity().getSpec().getElementGUID().equals(guid)) {
                orderChilds.add(orderItem);
                return orderChilds;
            }

            if (orderItem.getSubOrderItemKeys() != null && !orderItem.getSubOrderItemKeys().isEmpty()) {
                final Set<OrderItem> subOrderItems = OrderRepositoryLocator.getInstance().getOrderItem(
                        orderItem.getSubOrderItemKeys(), orderRef.getKey(), orderRef.getMajorVersion(),
                        orderRef.getMinorVersion());
                LOGGER.debug("Found  {} child Items of GUID {} inside {} {}", subOrderItems.size(), guid,
                        orderItem.getKey(), orderItem.getCatalogEntity().getSpec().getName());
                for (final OrderItem item : subOrderItems) {
                    final OrderItem childItem = getChildOrderItemByGuid(orderRef, item, guid);
                    if (childItem != null)
                        orderChilds.add(childItem);
                }
            }
        }
        return orderChilds;
    }

    /**
     * @param catalogEntity
     * @param charName
     * @return value of charName Integer
     */
    public static Integer getIntValueFromEntity(CatalogEntity catalogEntity, String charName) {
        if (null != charName && null != catalogEntity && catalogEntity.isCharacteristicPopulated(charName)) {
            LOGGER.debug("characteristic {} , value {} ", charName,
                    catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
            return Integer.parseInt(catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
        } else {
            LOGGER.info("characteristic {} not populated", charName);
            return null;
        }
    }

    public static OrderItem getOrderItemByGuid(SoiRequest soiRequest, Order order, String guid) {
        if (soiRequest != null) {
            final SoiRequestItem soiRequestItem = soiRequest.getRequestItems().stream().filter(
                    item -> item.getItem().getCatalogEntity().getSpec().getElementGUID().equalsIgnoreCase(guid))
                    .findFirst().orElse(null);
            return soiRequestItem != null ? soiRequestItem.getItem() : null;
        }
        if (order != null) {
            return order.getAllOrderItem().stream()
                    .filter(item -> item.getCatalogEntity().getSpec().getElementGUID().equalsIgnoreCase(guid))
                    .findFirst().orElse(null);
        }
        return null;
    }

    /**
     * Get list of order item keys or ids for GUID from order
     *
     * @param order
     * @param guid
     * @return List of orderItemKey
     */
    public static List<String> getOrderItemKeys(Order order, String guid) {
        final List<String> orderItemKeys = new ArrayList<>();
        for (final OrderItem oi : order.getAllOrderItem()) {
            LOGGER.debug(" entity name : {}, spec key {}", oi.getCatalogEntity().getSpecFQNameWithoutPath(),
                    oi.getCatalogEntity().getSpecKey());
            if (StringUtils.equalsIgnoreCase(oi.getCatalogEntity().getSpec().getElementGUID(), guid)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(oi.getCatalogEntity()));
                orderItemKeys.add(oi.getKey());
            }
        }
        LOGGER.info("{} orderItemKeys found for guid {}", orderItemKeys.size(), guid);
        return orderItemKeys;
    }

    /**
     * Get list of order item keys or ids for GUID from soiRequest
     *
     * @param soiRequest
     * @param guid
     * @return List of orderItemKey
     */
    public static List<String> getOrderItemKeys(SoiRequest soiRequest, String guid) {
        final List<String> orderItemKeys = new ArrayList<>();
        for (final SoiRequestItem soiRequestItem : soiRequest.getRequestItems()) {
            LOGGER.debug("Catalog Entity with Name {} ",
                    soiRequestItem.getItem().getCatalogEntity().getSpecFQNameWithoutPath());
            if (StringUtils.equalsIgnoreCase(soiRequestItem.getItem().getCatalogEntity().getSpec().getElementGUID(),
                    guid)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(soiRequestItem.getItem().getCatalogEntity()));
                orderItemKeys.add(soiRequestItem.getItem().getKey());
            }
        }
        LOGGER.info("{} orderItemKeys found for guid {}", orderItemKeys.size(), guid);
        return orderItemKeys;
    }

    /**
     * Get list of order item keys or ids for GUID and action from order
     *
     * @param order
     * @param guid
     * @param action
     * @return List of orderItemKey
     */
    public static List<String> getOrderItemKeysForAction(Order order, String guid, ItemAction action) {
        final List<String> orderItemKeys = new ArrayList<>();
        for (final OrderItem oi : order.getAllOrderItem()) {
            LOGGER.debug(" entity name : {}, spec key {}", oi.getCatalogEntity().getSpecFQNameWithoutPath(),
                    oi.getCatalogEntity().getSpecKey());
            if (StringUtils.equalsIgnoreCase(oi.getCatalogEntity().getSpec().getElementGUID(), guid)
                    && oi.getAction().equals(action)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(oi.getCatalogEntity()));
                orderItemKeys.add(oi.getKey());
            }
        }
        LOGGER.info("{} orderItemKeys found for guid {}", orderItemKeys.size(), guid);
        return orderItemKeys;
    }

    /**
     * Get list of OrderItems for GUID from soiRequest
     *
     * @param SoiRequest soiRequest
     * @param String guid
     * @return List of OrderItems
     */
    public static List<OrderItem> getOrderItemsByGuid(SoiRequest soiRequest, String guid) {
        final List<OrderItem> orderItems = new ArrayList<>();
        for (final SoiRequestItem soiRequestItem : soiRequest.getRequestItems()) {
            LOGGER.debug("Catalog Entity with Name {} ",
                    soiRequestItem.getItem().getCatalogEntity().getSpecFQNameWithoutPath());
            if (StringUtils.equalsIgnoreCase(soiRequestItem.getItem().getCatalogEntity().getSpec().getElementGUID(),
                    guid)) {
                LOGGER.debug(" entity found : {}", JsonUtils.toString(soiRequestItem.getItem()));
                orderItems.add(soiRequestItem.getItem());
            }
        }
        LOGGER.info("{} CatalogEntity found for guid {}", orderItems.size(), guid);
        return orderItems;
    }

    /**
     * Get list of order items for given GUID and action from order
     *
     * @param order
     * @param guid
     * @param action
     * @return List of orderItemKey
     */
    public static List<OrderItem> getOrderItemsForAction(Order order, String guid, ItemAction action) {
        final List<OrderItem> orderItems = new ArrayList<>();
        for (final OrderItem oi : order.getAllOrderItem()) {
            if (StringUtils.equalsIgnoreCase(oi.getCatalogEntity().getSpec().getElementGUID(), guid)
                    && oi.getAction().equals(action)) {
                LOGGER.debug("OrderItem found : {}", JsonUtils.toString(oi));
                orderItems.add(oi);
            }
        }
        LOGGER.info("{} OrderItems found for guid {}", orderItems.size(), guid);
        return orderItems;
    }

    /**
     * @param catalogEntity
     * @param charName
     * @return value of charName String
     */
    public static String getValueFromEntity(CatalogEntity catalogEntity, String charName) {
        if (null != charName && null != catalogEntity && catalogEntity.isCharacteristicPopulated(charName)) {
            LOGGER.debug("characteristic {} , value {} ", charName,
                    catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
            return catalogEntity.getEntity().getCharacteristic(charName).getFirstValue();
        } else {
            LOGGER.info("characteristic {} not populated", charName);
            return null;
        }
    }

    /**
     * @param catalogEntity
     * @param charName
     * @return values of charName String
     */
    public static Set<String> getValuesFromEntity(CatalogEntity catalogEntity, String charName) {
        if (null != charName && null != catalogEntity && catalogEntity.isCharacteristicPopulated(charName)) {
            LOGGER.debug("characteristic {} , value {} ", charName,
                    catalogEntity.getEntity().getCharacteristic(charName).getFirstValue());
            return catalogEntity.getEntity().getCharacteristic(charName).getValues();
        } else {
            LOGGER.info("characteristic {} not populated", charName);
            return new HashSet<>();
        }
    }

    public static OrderChange updateEntities(Order order, List<InteractionItem> interactionItems,
            OrderChange orderChange, Map<String, Map<String, String>> catalogEnrich) {
        for (final Entry<?, ?> entry : catalogEnrich.entrySet()) {
            final OrderItemChange itemChange = new OrderItemChange();
            final CatalogEntityChange entityChange = new CatalogEntityChange();
            LOGGER.debug(" guid {}, charVal map {}", entry.getKey(), entry.getValue());

            for (final InteractionItem interactionItem : interactionItems) {
                final OrderItem oItem = order.getOrderItem(interactionItem.getOrderItemKey());
                if (StringUtils.equalsIgnoreCase(oItem.getCatalogEntity().getSpec().getElementGUID(),
                        entry.getKey().toString())) {
                    itemChange.setKey(oItem.getKey());
                    @SuppressWarnings("unchecked")
                    final Map<String, String> charValMap = (Map<String, String>) entry.getValue();
                    for (final Entry<?, ?> cvEntry : charValMap.entrySet()) {
                        final CharacteristicChange characteristicChange = new CharacteristicChange();
                        characteristicChange.setName(String.valueOf(cvEntry.getKey()));
                        characteristicChange.addValue(String.valueOf(cvEntry.getValue()));
                        entityChange.addCharacteristic(characteristicChange);
                    }
                }
            }
            itemChange.setCatalogEntity(entityChange);
            orderChange.addOrderItem(itemChange);
        }
        return orderChange;
    }

    /**
     * Update catalog entities for enrichment values
     *
     * @param soiRequest
     * @param orderChange
     * @param catalogEnrich map of guid and map of characteristics and value
     * @return OrderChange object
     */
    public static OrderChange updateEntities(SoiRequest soiRequest, OrderChange orderChange,
            Map<String, Map<String, String>> catalogEnrich) {
        for (final Entry<?, ?> entry : catalogEnrich.entrySet()) {
            final OrderItemChange itemChange = new OrderItemChange();
            final CatalogEntityChange entityChange = new CatalogEntityChange();
            LOGGER.debug("catalog GUID {}, charVal map {}", entry.getKey().toString(), entry.getValue());

            for (final SoiRequestItem soiRequestItem : soiRequest.getRequestItems()) {
                if (StringUtils.equalsIgnoreCase(
                        soiRequestItem.getItem().getCatalogEntity().getSpec().getElementGUID(),
                        entry.getKey().toString())) {
                    itemChange.setKey(soiRequestItem.getItem().getKey());
                    @SuppressWarnings("unchecked")
                    final Map<String, String> charValMap = (Map<String, String>) entry.getValue();
                    for (final Entry<?, ?> cvEntry : charValMap.entrySet()) {
                        final CharacteristicChange characteristicChange = new CharacteristicChange();
                        characteristicChange.setName(String.valueOf(cvEntry.getKey()));
                        characteristicChange.addValue(String.valueOf(cvEntry.getValue()));
                        entityChange.addCharacteristic(characteristicChange);
                    }
                }
            }
            itemChange.setCatalogEntity(entityChange);
            orderChange.addOrderItem(itemChange);
        }
        return orderChange;
    }
}
