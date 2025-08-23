package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for NIMAS to PDF conversion operations.
 */
public interface ConversionService {

    /**
     * Start a new conversion process.
     * 
     * @param file The NIMAS file to convert
     * @param outputName Optional custom output filename
     * @return Conversion ID for tracking
     */
    String startConversion(MultipartFile file, String outputName) throws Exception;

    /**
     * Get the current status of a conversion.
     * 
     * @param conversionId The conversion ID
     * @return Conversion status information
     */
    ConversionStatus getStatus(String conversionId);

    /**
     * Get the converted PDF as a resource.
     * 
     * @param conversionId The conversion ID
     * @return PDF resource for download
     */
    Resource getPdfResource(String conversionId) throws Exception;

    /**
     * Get current conversion properties/configuration.
     * 
     * @return Properties object
     */
    Object getProperties();

    /**
     * Update conversion properties/configuration.
     * 
     * @param properties New properties
     */
    void updateProperties(Object properties);
}
