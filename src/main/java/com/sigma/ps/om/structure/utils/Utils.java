package com.sigma.ps.om.structure.utils;

import org.drools.core.util.StringUtils;

import com.sigma.om.process.InteractionBuilderImpl;
import com.sigma.om.sdk.engine.interaction.Interaction;
import com.sigma.om.sdk.engine.interaction.InteractionState;
import com.sigma.om.sdk.engine.interaction.locator.InteractionResponseSenderLocator;
import com.sigma.om.sdk.order.enrichment.change.EnrichmentInfo;
import com.sigma.om.sdk.soi.interaction.ExternalInteractionState;
import com.sigma.om.soi.interaction.ExternalInteractionImpl;

public class Utils {

	public static void notifyEngine(ExternalInteractionImpl externalInteraction, String errorMsg, String errorCode,
			ExternalInteractionState extIntState, InteractionState intState) throws Exception {
		final Interaction interaction = InteractionBuilderImpl.make(externalInteraction)
				.enrichmentInfo(new EnrichmentInfo()).build();

		externalInteraction.setState(extIntState);
		interaction.setState(intState);

		if (!StringUtils.isEmpty(errorMsg) && !StringUtils.isEmpty(errorCode)) {
			externalInteraction.setErrorCode(errorCode);
			externalInteraction.setErrorDescription(errorMsg);
			interaction.setErrorCode(errorCode);
			interaction.setErrorDescription(errorMsg);
		}

		InteractionResponseSenderLocator.getInstance().notify(interaction);
	}

}
