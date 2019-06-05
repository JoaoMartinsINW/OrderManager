package com.sigma.ps.om.commons.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FilesProcessorUtils {
    INSTANCE;
    private static final String                UTF8    = "UTF-8";
    protected static final Map<String, String> readMap = new HashMap<>();
    public static final Logger                 LOGGER  = LoggerFactory.getLogger(FilesProcessorUtils.class.getName());

    public static String readFileFromConfig(String directory, String subDirectory, String fileName)
            throws IOException {

        if (readMap.containsKey(fileName)) {
            LOGGER.info("returning statically loaded {} file", fileName);
            return readMap.get(fileName);
        }

        final String rootFolder = System.getProperty(directory);
        final String subFolder = System.getProperty(subDirectory);
        final File confFile = new File(rootFolder + File.separator + subFolder + File.separator + fileName);
        // load properties file depends on system property and application conf
        // directory
        LOGGER.debug(" SOI configuration file {}", confFile);
        if (confFile.exists()) {
            final String fileRead = FileUtils.readFileToString(confFile, UTF8);
            LOGGER.info("File {} successfully loaded in static map ", fileName);
            readMap.put(fileName, fileRead);
            return fileRead;
        } else {
            throw new FileNotFoundException(confFile.getAbsolutePath());
        }
    }

    public static String readSystemFile(String absoluteFilePath) throws IOException {
        final File file = new File(absoluteFilePath);
        if (file.exists()) {
            return FileUtils.readFileToString(file, UTF8);
        }
        return "";
    }

    public static void writeSystemFile(String data, String fileName) throws IOException {
        final File file = new File(fileName);
        FileUtils.writeStringToFile(file, data, UTF8);
    }
}
