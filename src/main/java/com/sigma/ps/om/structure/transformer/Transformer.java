package com.sigma.ps.om.structure.transformer;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sigma.om.sdk.order.enrichment.change.OrderChange;
import com.sigma.om.sdk.soi.framework.SoiContext;
import com.sigma.om.sdk.soi.framework.SoiRequest;
import com.sigma.om.soi.framework.SOIAdapterUtils;
import com.sigma.om.soi.framework.SoiConfig;
import com.sigma.om.soi.framework.exception.SoiTransformerException;
import com.sigma.ps.om.commons.envcfg.EnvironmentUtil;
import com.sigma.ps.om.commons.order.OrderUtils;
import com.sigma.ps.om.commons.soi.SoiMapping;
import com.sigma.ps.om.commons.soi.mapping.pojo.MappingConfig;

public abstract class Transformer implements Processor {

	public MappingConfig  getSoiMappingConfig(String adapterName, String filepath) {
		MappingConfig mappingConfig = new MappingConfig();
        try {
            mappingConfig = SoiMapping.getConfig(EnvironmentUtil.getEnvConfig(adapterName)
                    .get(filepath));
        } catch (final Exception e) {
            throw new SoiTransformerException("Error creating file mapping for " + adapterName);
        }
		return mappingConfig;
	}

	protected Logger log() {
		return LoggerFactory.getLogger(getClass());
	}

	@Override
    public void process(Exchange exchange) throws Exception {
		final SoiContext soiContext = SoiConfig.getSoIContextFromInBody(exchange);
		final String adapterName = SOIAdapterUtils.getSoiAdapter(exchange).getAdapterName();

		exchange = setHeaders(exchange, EnvironmentUtil.getEnvConfig(adapterName));

		for(SoiRequest soiRequest : soiContext.getSoiRequestList()) {
			Object request = buildRequest(soiRequest);

			if(request!= null) {
				final OrderChange orderChange = soiRequest.getEnrichmentInfo().getIpOrderChange();
				if (orderChange!= null) {
					request = manualTask(soiRequest, orderChange);
				}
				final String payload = SoiMapping.getJsonBuilder(request);

				if(payload!=null) {
					setPayload(payload);
					soiContext.setSoiExternalInteractions(OrderUtils.populateExternalInteractions(soiRequest));
				}
			}
		}
	}

	public abstract Exchange setHeaders(Exchange exchange, Map<String,String> envCfg);

	public abstract Object buildRequest(SoiRequest soiRequest);

	public abstract Object manualTask(SoiRequest soiRequest, OrderChange orderChange);

	public abstract void setPayload(Object request);

}
