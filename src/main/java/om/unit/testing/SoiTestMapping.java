package om.unit.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.soi.framework.exception.SoiRuntimeException;
import com.sigma.ps.om.commons.file.FilesProcessorUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SoiTestMapping {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SoiTestMapping.class);
	/** Map of SOI mapping cfg */
	private static Map<String, SoiTestCfg> soiMappingCfg = new HashMap<String, SoiTestCfg>();
	/** soiMappingLock */
	private static Object soiMappingLock = new Object();
	/** ObjectMapper instance */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * Get SOI mapping configuration fileName with relative path example
	 * "./tdc/polling/mapping-configuration.json"
	 *
	 * @param fileName
	 * @return MappingConfig object
	 */
	public static SoiTestCfg getConfig(String fileName) {
		if (CommonUtils.isEmpty(soiMappingCfg) || CommonUtils.isEmpty(soiMappingCfg.get(fileName))) {
			synchronized (soiMappingLock) {
				final Map<String, SoiTestCfg> soiMappings = Collections.emptyMap();
				String data;
				SoiTestCfg mappingConfig;
				try {
					data = FilesProcessorUtils.readSystemFile(fileName);
					mappingConfig = mapper.readValue(data, SoiTestCfg.class);
				} catch (final IOException e) {
					final String errMsg = "IOException while getting the mapping configuration at " + fileName;
					LOGGER.error(errMsg);
					throw new SoiRuntimeException(errMsg, e);
				}
				if (CommonUtils.isEmpty(mappingConfig)) {
					final String errMsg = "Unable to get mapping configuration object from " + fileName;
					LOGGER.error(errMsg);
					throw new SoiRuntimeException(errMsg);
				}
				LOGGER.debug(" fileLoc : {} :  mapping configuration {} ", fileName, soiMappings);
				soiMappingCfg.put(fileName, mappingConfig);
			}
		}
		return soiMappingCfg.get(fileName);
	}

	/**
	 * Method responsible to transform a java pojo object into a json string
	 *
	 * @param request
	 * @return {@link String}
	 */
	public static String getJsonBuilder(Object request) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			LOGGER.info("JSON created on jsonBuilder method");
			return mapper.writeValueAsString(request);
		} catch (final JsonProcessingException e) {
			LOGGER.info("JsonProcessingException: {}", e.getMessage());
		}
		return null;
	}
}
