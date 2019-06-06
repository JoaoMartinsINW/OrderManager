package com.sigma.ps.om.structure.outbound;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sigma.om.cmn.JsonUtils;
import com.sigma.om.process.InteractionBuilderImpl;
import com.sigma.om.sdk.engine.interaction.Interaction;
import com.sigma.om.sdk.engine.interaction.InteractionState;
import com.sigma.om.sdk.engine.interaction.locator.InteractionResponseSenderLocator;
import com.sigma.om.sdk.order.enrichment.change.EnrichmentInfo;
import com.sigma.om.sdk.soi.framework.SoiContext;
import com.sigma.om.sdk.soi.interaction.ExternalInteractionState;
import com.sigma.om.soi.framework.SOIAdapterUtils;
import com.sigma.om.soi.framework.SoiConfig;
import com.sigma.om.soi.framework.exception.SoiTransformerException;
import com.sigma.om.soi.interaction.ExternalInteractionImpl;
import com.sigma.om.soi.interaction.ExternalResponseImpl;
import com.sigma.ps.om.commons.envcfg.EnvironmentUtil;
import com.sigma.ps.om.commons.soi.SoiMapping;
import com.sigma.ps.om.commons.soi.mapping.pojo.MappingConfig;
import com.sigma.ps.om.structure.errors.ErrorCodes;
import com.sigma.ps.om.structure.utils.Utils;

public abstract class Outbound implements Processor {

	private final Logger LOGGER = log();

	abstract ExternalInteractionImpl setExternalInteraction(ExternalInteractionImpl extInt,
			ExternalResponseImpl soiExtResponse, Object response);

	abstract ExternalInteractionImpl handleNotify(ExternalInteractionImpl extInt, Object response);

	void handleResponse(Response response, String adapterName, ExternalInteractionImpl extInt) throws Exception {
		final ExternalResponseImpl extResponse = new ExternalResponseImpl();
		extResponse.setExtInteractionId(extInt.getId());

		switch (response.getStatus()) {
		case HttpStatus.SC_OK:

			break;
		case HttpStatus.SC_NOT_FOUND:
			extResponse.setResponseString("Not Found");
			extInt.addResponse(extResponse);

			break;
		default:
			extResponse.setResponseString("Error");
			extInt.addResponse(extResponse);
			Utils.notifyEngine(extInt, HttpStatus.getStatusText(response.getStatus()),
					ErrorCodes.BACKEND_SYSTEM_INTERNAL_SERVER_ERROR, ExternalInteractionState.FAILED,
					InteractionState.FAILED);
			break;
		}

		Object responseObject = buildResponse(response);

	}

	protected Logger log() {
		return LoggerFactory.getLogger(getClass());
	}

	private Object buildResponse(Response response) throws IOException {
		final InputStream iStream = (InputStream) response.getEntity();
		final Writer writer = new StringWriter();
		String inputEncode = null;
		IOUtils.copy(iStream, writer, inputEncode);

		return writer.toString();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		final SoiContext soiContext = SoiConfig.getSoIContextFromInBody(exchange);
		final String adapterName = SOIAdapterUtils.getSoiAdapter(exchange).getAdapterName();
		ExternalInteractionImpl extInt = (ExternalInteractionImpl) soiContext.getSoiExternalInteraction().get(0);

		final Response response = (Response) extInt.getResponses().get(0).getResponse();

		if (response != null) {

			handleResponse(response, adapterName, extInt);

			/*extInt = setExternalInteraction(extInt, new ExternalResponseImpl(), responseObject);
			extInt = handleNotify(extInt, responseObject);*/
		} else {
			LOGGER.debug("Null response");
			Utils.notifyEngine(extInt, ErrorCodes.NULL_RESPONSE, ErrorCodes.NULL_RESPONSE,
					ExternalInteractionState.FAILED, InteractionState.FAILED);
		}

		exchange.getIn().setBody(soiContext);

	}

}
