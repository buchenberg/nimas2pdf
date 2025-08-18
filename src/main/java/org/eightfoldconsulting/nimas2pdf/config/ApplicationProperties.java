package org.eightfoldconsulting.nimas2pdf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "/application.properties";

    static {
        try (InputStream input = ApplicationProperties.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                logger.error("Could not find {}", PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}