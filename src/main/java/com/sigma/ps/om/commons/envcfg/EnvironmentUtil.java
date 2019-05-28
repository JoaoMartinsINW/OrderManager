package com.sigma.ps.om.commons.envcfg;

import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.envcfg.CfgMgrEnvCfg;
import com.sigma.om.envcfg.cfg.EnvironmentConfigObject;
import com.sigma.om.envcfg.cfg.GenericCfg;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EnvironmentUtil {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentUtil.class);
    /** Map of SOI env cfg */
    private static Map<String, Map<String, String>> soiEnvCfg     = new HashMap<String, Map<String, String>>();
    /** soiEnvCfgLock */
    private static Object              soiEnvCfgLock = new Object();

    /**
     * @param adapterName
     * @return Map with SOI key value properties
     * @throws Exception
     */
    public static Map<String, String> getEnvConfig(String adapterName) throws Exception {
        if(CommonUtils.isEmpty(soiEnvCfg) || CommonUtils.isEmpty(soiEnvCfg.get(adapterName))){
            synchronized (soiEnvCfgLock) {
                Map<String, String> adapterEnvParam = Collections.emptyMap();
                try {
                    adapterEnvParam = ((GenericCfg) ((EnvironmentConfigObject) CfgMgrEnvCfg
                            .getBaseEnvEntryFromCachedWS(CfgMgrEnvCfg.ENVCFG_OBJECT_NAME, adapterName, null,
                                    false, null)).getEntryValue(adapterName)).getEntries();
                    if (adapterEnvParam == null || adapterEnvParam.isEmpty()) {
                        LOGGER.warn(" Adapter : {} :  CANNOT find the evironment configuration ",
                                adapterName);
                        return adapterEnvParam;
                    }
                } catch (final Exception e) {
                    LOGGER.error(" Adapter : {} :  Error is getting the evironment configuration   Error: {}", adapterName, e);
                    return adapterEnvParam;
                }
                LOGGER.debug(" Adapter : {} :  Environment Parameters {} ", adapterName,adapterEnvParam);
                soiEnvCfg.put(adapterName, adapterEnvParam);
            }
        }
        return soiEnvCfg.get(adapterName);


    }
}
