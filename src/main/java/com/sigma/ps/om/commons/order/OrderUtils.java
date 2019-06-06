package com.sigma.ps.om.commons.order;

import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.cmn.JsonUtils;
import com.sigma.om.process.InteractionBuilderImpl;
import com.sigma.om.sdk.engine.interaction.Interaction;
import com.sigma.om.sdk.engine.interaction.InteractionState;
import com.sigma.om.sdk.engine.interaction.locator.InteractionResponseSenderLocator;
import com.sigma.om.sdk.order.ItemAction;
import com.sigma.om.sdk.order.ItemOperation;
import com.sigma.om.sdk.order.Order;
import com.sigma.om.sdk.order.OrderItem;
import com.sigma.om.sdk.order.OrderItemState;
import com.sigma.om.sdk.order.PortfolioItem;
import com.sigma.om.sdk.order.enrichment.change.CharacteristicChange;
import com.sigma.om.sdk.order.enrichment.change.EnrichmentInfo;
import com.sigma.om.sdk.order.enrichment.change.OrderChange;
import com.sigma.om.sdk.repo.locator.InteractionRepositoryLocator;
import com.sigma.om.sdk.repo.locator.OrderRepositoryLocator;
import com.sigma.om.sdk.soi.ContentType;
import com.sigma.om.sdk.soi.framework.SoiRequest;
import com.sigma.om.sdk.soi.framework.SoiRequestItem;
import com.sigma.om.sdk.soi.interaction.ExternalInteractionState;
import com.sigma.om.sdk.soi.interaction.IMExternalInteraction;
import com.sigma.om.soi.framework.exception.SoiRuntimeException;
import com.sigma.om.soi.interaction.ExternalInteractionImpl;
import com.sigma.om.soi.util.ExternalInteractionBuilderImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderUtils {

    private OrderUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtils.class);

    /**
     * Method responsible to check changes by each order header parameters
     *
     * @param soiRequest
     * @param charValueName
     * @return
     */
    public static String checkForCharChange(SoiRequest soiRequest, String charValueName) {
        if (soiRequest.getEnrichmentInfo().getIpOrderChange() != null) {
            final Collection<CharacteristicChange> charCollec = soiRequest.getEnrichmentInfo().getIpOrderChange()
                    .getCharacteristics();
            for (final CharacteristicChange charChange : charCollec) {
                final String charSet = charChange.getName();
                if (charSet.equalsIgnoreCase(charValueName)) {
                    LOGGER.debug("newValue in case of Retry : {}", charChange.getValues().iterator().next());
                    return charChange.getValues().iterator().next();
                }
            }
        }
        return "";
    }

    /**
     * Returns the map of cvlan with tvlan value if tvlan not populated will set default value as 0
     * for tvlan
     *
     * NOTE: This method ignore the orderItem with CANCEL operation or cancelling state.
     *
     * @param soiRequest
     * @param order
     * @param provRfsGuid
     * @param provRfsCvlanName
     * @param provRfsTvlanName
     * @return Returns the map of cvlan with tvlan value
     *
     *
     */

    // ESte método é extremamente orientado ao negócio não me parece que venha a ser reutilizado no
    // futuro
    public static Map<Integer, Integer> getAvailableTvlans(SoiRequest soiRequest, Order order, String provRfsGuid,
            String provRfsCvlanName, String provRfsTvlanName) {
        final Map<Integer, Integer> tvlans = new HashMap<>();
        if (order == null) {
            order = OrderUtils.getLatestOrder(soiRequest.getOrderRef().getKey());
        }

        for (final PortfolioItem portfolioItem : order.getCustomer().getAllPortfolioItem()) {
            if (portfolioItem.getSpec().getElementGUID().equals(provRfsGuid)
                    && portfolioItem.isCharacteristicPopulated(provRfsCvlanName)) {
                final Integer cvLan = Integer
                        .parseInt(portfolioItem.getEntity().getCharacteristic(provRfsCvlanName).getFirstValue());
                if (portfolioItem.isCharacteristicPopulated(provRfsTvlanName)) {
                    final Integer tvLan = Integer
                            .parseInt(portfolioItem.getEntity().getCharacteristic(provRfsTvlanName).getFirstValue());
                    LOGGER.info("Adding pair cvlan {} :: tvlan {} for portfolioItem {}", cvLan, tvLan,
                            portfolioItem.getKey());
                    tvlans.put(cvLan, tvLan);
                } else {
                    LOGGER.info("{} not populated on prov RFS portfolioItem {}", provRfsTvlanName,
                            portfolioItem.getKey());
                }
            }
        }

        for (final OrderItem orderItem : order.getAllOrderItem()) {
            if (orderItem.getCatalogEntity().getSpec().getElementGUID().equals(provRfsGuid)
                    && !(orderItem.getOperation().equals(ItemOperation.CANCEL)
                            || orderItem.getAction().equals(ItemAction.DELETE)
                            || orderItem.getState().equals(OrderItemState.CANCELLING))
                    && orderItem.getCatalogEntity().isCharacteristicPopulated(provRfsCvlanName)) {
                final Integer cvLan = Integer.parseInt(
                        orderItem.getCatalogEntity().getEntity().getCharacteristic(provRfsCvlanName).getFirstValue());
                if (orderItem.getCatalogEntity().isCharacteristicPopulated(provRfsTvlanName)) {
                    final Integer tvLan = Integer.parseInt(orderItem.getCatalogEntity().getEntity()
                            .getCharacteristic(provRfsTvlanName).getFirstValue());
                    LOGGER.info("Adding pair cvlan {} :: tvlan {} for item {}", cvLan, tvLan,
                            orderItem.getDisplayId());
                    tvlans.put(cvLan, tvLan);
                } else {
                    LOGGER.info("{} not populated on prov RFS display {}", provRfsTvlanName, orderItem.getDisplayId());
                    tvlans.put(cvLan, 0);
                }
            }
        }
        LOGGER.info("Available tvlans {}", JsonUtils.toPlainFormattedString(tvlans));
        return tvlans;
    }

    /**
     * Get value of order header Characteristic
     *
     * @param order
     * @param charName
     * @return value or order char String
     */
    public static String getCharacValueFromOrder(Order order, String charName) {
        return charName != null && order != null && order.isCharacteristicPopulated(charName)
                ? order.getEntity().getCharacteristic(charName).getFirstValue()
                : "";
    }

    /**
     * Get Interaction
     *
     * @param externalInteraction
     * @return Interaction
     */

    // TODO:: se concordares , este método deve ser apagado, isto não acrescenta nada... porque nao
    // chamar logo o IteractionRepositoryLocator?
    @Deprecated
    public static Interaction getInteraction(ExternalInteractionImpl externalInteraction) {
        if (externalInteraction != null) {
            return InteractionRepositoryLocator.getInstance().getInteraction(externalInteraction.getInteractionRef());
        }
        return null;
    }

    /**
     * Get value of order header Characteristic
     *
     * @param order
     * @param charName
     * @return value or order char Integer
     */

    // este método genérico já está no Catalog Utils
    @Deprecated
    public static Integer getIntValueFromOrder(Order order, String charName) {
        if (null != charName && null != order && order.isCharacteristicPopulated(charName)) {
            return Integer.parseInt(order.getEntity().getCharacteristic(charName).getFirstValue());
        } else {
            LOGGER.info("characteristic {} not populated on Order", charName);
            return null;
        }
    }

    /**
     * Get ItemOperation for a specific guid item
     *
     * @param soiRequest
     * @param guid
     * @return
     */
    public static ItemOperation getItemOperation(SoiRequest soiRequest, String guid) {
        final SoiRequestItem soiRequestItems = soiRequest.getRequestItems().stream().filter(
                soirequestitem -> soirequestitem.getItem().getCatalogEntity().getSpec().getElementGUID().equals(guid))
                .findFirst().orElse(null);
        if (soiRequestItems == null) {
            return null;
        }

        return soiRequestItems.getItem().getOperation();
    }

    /**
     * Get latest order
     *
     * @param soiRequest
     * @return Latest Order object
     */
    // public static Order getLatestOrder(String orderId) {
    // return OrderRepositoryLocator.getInstance().getOrder(orderId, -1L, -1L);
    // }

    /*
     * Get Order Item action for an entity GUID
     *
     * @param soiReq
     *
     * @param entityGUID
     *
     * @return
     */
    public static String getOrderAction(SoiRequest soiReq, String entityGUID) {
        final SoiRequestItem soiRequestItems = soiReq.getRequestItems().stream()
                .filter(soirequestitem -> soirequestitem.getItem().getCatalogEntity().getSpec().getElementGUID()
                        .equals(entityGUID))
                .findFirst().orElse(null);

        if (soiRequestItems == null) {
            return null;
        }

        return soiRequestItems.getItem().getAction().name();
    }

    /**
     * Get Portfolio Item for Guid
     *
     * @param order
     * @param guid
     * @return PortfolioItem
     */
    public static PortfolioItem getPortfolioItemByGuid(Order order, String guid) {
        for (final PortfolioItem portfolioItem : order.getCustomer().getAllPortfolioItem()) {
            if (StringUtils.equals(portfolioItem.getSpec().getElementGUID(), guid)) {
                LOGGER.debug("Found portfolioItem with Name {} ", portfolioItem.getSpec().getName());
                return portfolioItem;
            }
        }
        LOGGER.warn(" PortfolioItem not found for guid {}", guid);
        return null;
    }

    /**
     * get Root Item Action
     *
     * @param orderId
     * @return ItemAction at root
     */
    public static ItemAction getRootItemAction(Long orderId) {
        final Order order = OrderRepositoryLocator.getInstance().getOrder(String.valueOf(orderId), -1L, -1L);
        for (final OrderItem orderItem : order.getRootOrderItems()) {
            return orderItem.getAction();
        }
        throw new SoiRuntimeException("RootItemAction is null");
    }

    /**
     * Get root order item action
     *
     * @param soiRequest
     * @return ItemAction
     */
    public static ItemAction getRootItemAction(SoiRequest soiRequest) {
        // Catalog interest is must exist for activity so using zeroth(get(0)) is safe;
        // all entities
        // have the same root
        String orderItemKey = soiRequest.getRequestItems().get(0).getItem().getParentItemKey();
        if (CommonUtils.isEmpty(orderItemKey)) {
            // if first item itself is the rootOrderItem
            return soiRequest.getRequestItems().get(0).getItem().getAction();
        } else {
            final Order order = OrderRepositoryLocator.getInstance()
                    .getOrder(String.valueOf(soiRequest.getOrderRef().getKey()), -1L, -1L);

            while (!CommonUtils.isEmpty(order.getOrderItem(orderItemKey).getParentItemKey())) {
                orderItemKey = order.getOrderItem(orderItemKey).getParentItemKey();
            }

            return order.getOrderItem(orderItemKey).getAction();
        }
    }

    public static ItemOperation getRootOperation(SoiRequest soiRequest) {
        // Catalog interest is must exist for activity so using zeroth(get(0)) is safe;
        // all entities
        // have the same root
        String orderItemKey = soiRequest.getRequestItems().get(0).getItem().getParentItemKey();
        if (CommonUtils.isEmpty(orderItemKey)) {
            // if first item itself is the rootOrderItem
            return soiRequest.getRequestItems().get(0).getItem().getOperation();
        } else {
            final Order order = OrderRepositoryLocator.getInstance().getOrder(soiRequest.getOrderRef().getKey(), -1L,
                    -1L);

            while (!CommonUtils.isEmpty(order.getOrderItem(orderItemKey).getParentItemKey())) {
                orderItemKey = order.getOrderItem(orderItemKey).getParentItemKey();
            }

            return order.getOrderItem(orderItemKey).getOperation();
        }
    }

    /**
     * Get StubbedVALUE from order header Characteristic
     *
     * @param order
     * @param charName
     * @return value or order char String
     */

    public static Boolean getStubbedFromOrder(Order order, String charName) {
        if ((null != charName && null != order && order.isCharacteristicPopulated(charName))
                && order.getEntity().getCharacteristic(charName).getFirstValue().equals("true")) {
            return true;
        } else {
            LOGGER.info("Stubbed {} not populated on Order", charName);
            return false;
        }
    }

    public static boolean isOrderCharChanged(String characteristic, Order currentOrder, Order prevOrder) {
        if (StringUtils.isNotEmpty(OrderUtils.getCharacValueFromOrder(currentOrder, characteristic))
                && StringUtils.isNotEmpty(OrderUtils.getCharacValueFromOrder(prevOrder, characteristic))
                && !StringUtils.equalsIgnoreCase(OrderUtils.getCharacValueFromOrder(currentOrder, characteristic),
                        OrderUtils.getCharacValueFromOrder(prevOrder, characteristic))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Notify Interaction status with EnrichmentInfo
     *
     * @param externalInteraction
     * @param enrichmentInfo
     * @param state
     */

    public static void notifyInteraction(ExternalInteractionImpl externalInteraction, EnrichmentInfo enrichmentInfo,
            String state) {
        externalInteraction.setState(ExternalInteractionState.valueOf(state));

        final Interaction interaction = InteractionBuilderImpl.make(externalInteraction).enrichmentInfo(enrichmentInfo)
                .state(InteractionState.valueOf(state)).build();

        try {
            InteractionResponseSenderLocator.getInstance().notify(interaction);
        } catch (final Exception e) {
            LOGGER.error(" Exception in notifyInteraction {} ", e);
        }
    }

    /**
     * Create External Interaction
     *
     * @param req
     * @return List of IMExternalInteraction
     */

    public static List<IMExternalInteraction> populateExternalInteractions(SoiRequest req) {
        final List<IMExternalInteraction> soiExtInteractions = new ArrayList<>();
        final IMExternalInteraction soiExtInteraction = ExternalInteractionBuilderImpl.make(req).build();
        soiExtInteractions.add(soiExtInteraction);
        return soiExtInteractions;
    }

    /**
     * Get External Interactions
     *
     * @param soiRequest
     * @return list of External Interaction
     */
    public static List<IMExternalInteraction> populateExternalInteractionsJson(SoiRequest soiRequest) {
        final List<IMExternalInteraction> soiExtInteractions = new ArrayList<IMExternalInteraction>();
        final IMExternalInteraction soiExtInteraction = ExternalInteractionBuilderImpl.make(soiRequest)
                .contentType(ContentType.JSON.name()).build();
        soiExtInteractions.add(soiExtInteraction);
        return soiExtInteractions;
    }

    /**
     * * Update order header characteristics
     *
     * @param orderChange
     * @param orderEnrich map with characteristic and value
     * @return OrderChange object
     */
    public static OrderChange updateHeaders(OrderChange orderChange, Map<String, String> orderEnrich) {
        for (final Entry entry : orderEnrich.entrySet()) {
            final CharacteristicChange characteristicChange = new CharacteristicChange();
            characteristicChange.setName(String.valueOf(entry.getKey()));
            characteristicChange.addValue(String.valueOf(entry.getValue()));
            orderChange.addCharacteristic(characteristicChange);
        }
        LOGGER.debug("updateHeaders, orderChange {}", JsonUtils.toString(orderChange));
        return orderChange;
    }

}
