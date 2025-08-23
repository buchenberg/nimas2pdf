package org.eightfoldconsulting.nimas2pdf.web.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;

/**
 * JPA converter for ConversionProperties to/from JSON string in database.
 */
@Converter
public class ConversionPropertiesConverter implements AttributeConverter<ConversionProperties, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ConversionProperties conversionProperties) {
        if (conversionProperties == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(conversionProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting ConversionProperties to JSON", e);
        }
    }

    @Override
    public ConversionProperties convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ConversionProperties(); // Return default properties
        }
        try {
            return objectMapper.readValue(dbData, ConversionProperties.class);
        } catch (JsonProcessingException e) {
            // If there's an error parsing, return default properties
            return new ConversionProperties();
        }
    }
}
