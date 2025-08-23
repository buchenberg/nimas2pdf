package org.eightfoldconsulting.nimas2pdf.web.util;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionRequest;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionResponse;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;

/**
 * Factory class for creating test data objects.
 */
public class TestDataFactory {

    /**
     * Creates a mock XML file for testing.
     */
    public static MockMultipartFile createMockXmlFile() {
        return new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
    }

    /**
     * Creates a mock XML file with custom content.
     */
    public static MockMultipartFile createMockXmlFile(String filename, String content) {
        return new MockMultipartFile(
            "file",
            filename,
            "application/xml",
            content.getBytes()
        );
    }

    /**
     * Creates a mock ZIP file for testing.
     */
    public static MockMultipartFile createMockZipFile() {
        return new MockMultipartFile(
            "file",
            "test.zip",
            "application/zip",
            "PK\003\004".getBytes() // Minimal ZIP header
        );
    }

    /**
     * Creates a ConversionRequest with test data.
     */
    public static ConversionRequest createConversionRequest() {
        ConversionRequest request = new ConversionRequest();
        request.setInputFile("test.xml");
        request.setOutputName("output.pdf");
        request.setProperties("{\"dpi\":300}");
        return request;
    }

    /**
     * Creates a ConversionRequest with custom data.
     */
    public static ConversionRequest createConversionRequest(String inputFile, String outputName) {
        ConversionRequest request = new ConversionRequest();
        request.setInputFile(inputFile);
        request.setOutputName(outputName);
        return request;
    }

    /**
     * Creates a ConversionResponse with test data.
     */
    public static ConversionResponse createConversionResponse() {
        return new ConversionResponse("test-conversion-123", "Conversion started");
    }

    /**
     * Creates a ConversionResponse with custom data.
     */
    public static ConversionResponse createConversionResponse(String conversionId, String message) {
        return new ConversionResponse(conversionId, message);
    }

    /**
     * Creates a ConversionStatus with test data.
     */
    public static ConversionStatus createConversionStatus() {
        return new ConversionStatus("test-conversion-123", "PENDING", 0, "File uploaded");
    }

    /**
     * Creates a ConversionStatus with custom data.
     */
    public static ConversionStatus createConversionStatus(String conversionId, String status, int progress, String message) {
        return new ConversionStatus(conversionId, status, progress, message);
    }

    /**
     * Creates a completed ConversionStatus.
     */
    public static ConversionStatus createCompletedConversionStatus(String conversionId) {
        ConversionStatus status = new ConversionStatus(conversionId, "COMPLETED", 100, "Conversion completed");
        status.setOutputFileName("output.pdf");
        status.setFileSize(1024L);
        return status;
    }

    /**
     * Creates a failed ConversionStatus.
     */
    public static ConversionStatus createFailedConversionStatus(String conversionId, String errorMessage) {
        return new ConversionStatus(conversionId, "FAILED", 0, errorMessage);
    }

    /**
     * Creates a processing ConversionStatus.
     */
    public static ConversionStatus createProcessingConversionStatus(String conversionId, int progress) {
        return new ConversionStatus(conversionId, "PROCESSING", progress, "Processing file...");
    }

    /**
     * Generates a unique conversion ID for testing.
     */
    public static String generateConversionId() {
        return "test-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Creates sample XML content for testing.
     */
    public static String createSampleXmlContent() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <dtbook xmlns="http://www.daisy.org/z3986/2005/dtbook/" version="2005-3" xml:lang="en">
                <book>
                    <frontmatter>
                        <doctitle>Test Book</doctitle>
                    </frontmatter>
                    <bodymatter>
                        <level1>
                            <h1>Chapter 1</h1>
                            <p>This is a test paragraph.</p>
                        </level1>
                    </bodymatter>
                </book>
            </dtbook>
            """;
    }

    /**
     * Creates sample properties JSON for testing.
     */
    public static String createSamplePropertiesJson() {
        return """
            {
                "dpi": 300,
                "pageSize": "A4",
                "margins": "1in",
                "fontSize": "12pt",
                "lineHeight": "1.5"
            }
            """;
    }
}
