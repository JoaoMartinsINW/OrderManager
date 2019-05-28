package com.sigma.ps.om.commons.portfolio;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.om.cmn.CommonUtils;
import com.sigma.ps.om.commons.file.FilesProcessorUtils;
import com.sigma.ps.om.commons.portfolio.pojo.EntityCFStoRFSMap;
import com.sigma.ps.om.commons.portfolio.pojo.EntityCharMap;
import com.sigma.ps.om.commons.portfolio.pojo.EntityCharMapping;
import com.sigma.ps.om.commons.portfolio.pojo.Operation;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils for portfolio common methods
 */
public class PortfolioUtils {
    /* entityCfgMap */
    private static Map<String, Object>    entityCfgMap      = new HashMap<>();
    private static Map<String, String>    guidMap           = new HashMap<>();
    private static final String           ENTITYCFSTORFSMAP = "EntityCFStoRFSMap";
    private static final String           ENTITYCHARMAP     = "EntityCharMap";

    private static final Logger           LOGGER            = LoggerFactory.getLogger(PortfolioUtils.class);
    /* DateTimeFormatter */
    public final static DateTimeFormatter format            = DateTimeFormatter.ofPattern("MMddyyyyHHmmssSSS");
    /* PATH_SEPERATOR */
    public static final String            PATH_SEPERATOR    = "/";
    /* PREFIX for portfolio item id */
    public static final String            PREFIX            = "PS";

    ObjectMapper                          mapper            = new ObjectMapper()
            .setSerializationInclusion(Include.NON_NULL);

    /**
     * createEntityCFStoRFSMap
     *
     * @param operation
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static List<EntityCFStoRFSMap> createEntityCFStoRFSMap(String operation, String fileName)
            throws IOException {
        final String key = new StringBuilder(fileName).append(operation).append(ENTITYCFSTORFSMAP).toString();
        if (CommonUtils.isEmpty(entityCfgMap) || CommonUtils.isEmpty(entityCfgMap.get(key))) {
            entityCfgMap = new HashMap<>();
            if (CommonUtils.isEmpty(entityCfgMap.get(key))) {
                final EntityCharMapping mapping = new ObjectMapper()
                        .readValue(FilesProcessorUtils.readSystemFile(fileName), EntityCharMapping.class);
                for (final Operation operationObj : mapping.getOperations()) {
                    if (StringUtils.equalsIgnoreCase(operationObj.getName(), operation)) {
                        entityCfgMap.put(key, operationObj.getEntityCFStoRFSMap());
                    }
                }
            }
            // log for first time creation
            LOGGER.debug("entityCFStoRFSMapLst {} ", entityCfgMap.get(key));
            return (List<EntityCFStoRFSMap>) entityCfgMap.get(key);
        }
        LOGGER.info("entityCFStoRFSMapLst is already created");
        return (List<EntityCFStoRFSMap>) entityCfgMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, String>> createEntityCharMap(String operation, String fileName)
            throws IOException {
        final String key = new StringBuilder(fileName).append(operation).append(ENTITYCHARMAP).toString();
        if (CommonUtils.isEmpty(entityCfgMap) || CommonUtils.isEmpty(entityCfgMap.get(key))) {
            entityCfgMap = new HashMap<>();
            final Map<String, Map<String, String>> entityCharMap = new HashMap<>();
            final EntityCharMapping mapping = new ObjectMapper()
                    .readValue(FilesProcessorUtils.readSystemFile(fileName), EntityCharMapping.class);
            for (final Operation operationObj : mapping.getOperations()) {
                if (StringUtils.equalsIgnoreCase(operationObj.getName(), operation)) {
                    for (final EntityCharMap entityCharMapping : operationObj.getEntityCharMap()) {
                        Map<String, String> charMap = null;
                        for (final Entry<String, String> charMapping : entityCharMapping.getCharMap().entrySet()) {
                            if (CommonUtils.isEmpty(charMap)) {
                                charMap = new HashMap<>();
                                charMap.put(charMapping.getKey(), charMapping.getValue());
                            } else {
                                charMap.put(charMapping.getKey(), charMapping.getValue());
                            }
                        }
                        for (final Entry<String, String> guidEntry : entityCharMapping.getGuidMap().entrySet()) {
                            entityCharMap.put(String.valueOf(guidEntry.getKey()), charMap);
                        }
                    }
                }
            }
            entityCfgMap.put(key, entityCharMap);
            // log for first time creation
            LOGGER.info("entityCharMap {} ", entityCharMap);
            return (Map<String, Map<String, String>>) entityCfgMap.get(key);
        }
        LOGGER.info("createEntityCharMap is already created");
        return (Map<String, Map<String, String>>) entityCfgMap.get(key);
    }

    public static Map<String, String> getGuidMap(String operation, String fileName) throws IOException {
        if (CommonUtils.isEmpty(guidMap)) {
            for (final EntityCFStoRFSMap cfStoRFSMap : createEntityCFStoRFSMap(operation, fileName)) {
                guidMap.putAll(cfStoRFSMap.getCfsGUIDMap());
            }
        }
        LOGGER.debug("guidMap {} ", guidMap);
        return guidMap;
    }
}
