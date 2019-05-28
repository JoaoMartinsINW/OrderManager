package com.sigma.ps.om.structure.transformer;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.sdk.soi.framework.SoiContext;
import com.sigma.om.sdk.soi.framework.SoiRequest;
import com.sigma.om.soi.framework.SOIAdapterUtils;
import com.sigma.om.soi.framework.SoiConfig;
import com.sigma.om.soi.framework.exception.SoiRuntimeException;
import com.sigma.om.soi.framework.exception.SoiTransformerException;
import com.sigma.ps.om.commons.envcfg.EnvironmentUtil;
import com.sigma.ps.om.commons.soi.SoiMapping;
import com.sigma.ps.om.commons.soi.mapping.pojo.MappingConfig;


public abstract class Transformer implements Processor {

	final protected SoiContext soiContext;
	final protected String adapterName;
	final protected Map<String,String> envCfg;
	final protected List<SoiRequest> soiRequestList;

	Transformer(Exchange exchange) throws Exception{
		this.soiContext = SoiConfig.getSoIContextFromInBody(exchange);
		this.adapterName = SOIAdapterUtils.getSoiAdapter(exchange).getAdapterName();
		this.envCfg = EnvironmentUtil.getEnvConfig(this.adapterName);
		this.soiRequestList = this.soiContext.getSoiRequestList();
	}

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
		stepBefore();
		buildPayload();
		manualTask();
		stepAfter();
	}

	public abstract void stepBefore();

	public abstract void buildPayload();

	public abstract void manualTask();

	public void stepAfter() {

	}

}
