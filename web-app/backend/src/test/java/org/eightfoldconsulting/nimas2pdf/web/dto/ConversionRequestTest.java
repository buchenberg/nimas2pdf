package org.eightfoldconsulting.nimas2pdf.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConversionRequest DTO.
 */
class ConversionRequestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ConversionRequest request = new ConversionRequest();

        // Assert
        assertNull(request.getInputFile());
        assertNull(request.getOutputName());
        assertNull(request.getProperties());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String inputFile = "input.xml";
        String outputName = "output.pdf";

        // Act
        ConversionRequest request = new ConversionRequest(inputFile, outputName);

        // Assert
        assertEquals(inputFile, request.getInputFile());
        assertEquals(outputName, request.getOutputName());
        assertNull(request.getProperties());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ConversionRequest request = new ConversionRequest();
        String inputFile = "test.xml";
        String outputName = "result.pdf";
        String properties = "{\"dpi\":300}";

        // Act
        request.setInputFile(inputFile);
        request.setOutputName(outputName);
        request.setProperties(properties);

        // Assert
        assertEquals(inputFile, request.getInputFile());
        assertEquals(outputName, request.getOutputName());
        assertEquals(properties, request.getProperties());
    }

    @Test
    void testInputFileHandling() {
        // Arrange
        ConversionRequest request = new ConversionRequest();

        // Act & Assert
        request.setInputFile("normal.xml");
        assertEquals("normal.xml", request.getInputFile());

        request.setInputFile("file with spaces.xml");
        assertEquals("file with spaces.xml", request.getInputFile());

        request.setInputFile("");
        assertEquals("", request.getInputFile());

        request.setInputFile(null);
        assertNull(request.getInputFile());

        request.setInputFile("very/long/path/to/file.xml");
        assertEquals("very/long/path/to/file.xml", request.getInputFile());
    }

    @Test
    void testOutputNameHandling() {
        // Arrange
        ConversionRequest request = new ConversionRequest();

        // Act & Assert
        request.setOutputName("output.pdf");
        assertEquals("output.pdf", request.getOutputName());

        request.setOutputName("file with spaces.pdf");
        assertEquals("file with spaces.pdf", request.getOutputName());

        request.setOutputName("");
        assertEquals("", request.getOutputName());

        request.setOutputName(null);
        assertNull(request.getOutputName());

        request.setOutputName("very/long/path/to/output.pdf");
        assertEquals("very/long/path/to/output.pdf", request.getOutputName());
    }

    @Test
    void testPropertiesHandling() {
        // Arrange
        ConversionRequest request = new ConversionRequest();

        // Act & Assert
        request.setProperties("{\"dpi\":300}");
        assertEquals("{\"dpi\":300}", request.getProperties());

        request.setProperties("{\"pageSize\":\"A4\",\"margins\":\"1in\"}");
        assertEquals("{\"pageSize\":\"A4\",\"margins\":\"1in\"}", request.getProperties());

        request.setProperties("");
        assertEquals("", request.getProperties());

        request.setProperties(null);
        assertNull(request.getProperties());

        request.setProperties("simple string");
        assertEquals("simple string", request.getProperties());
    }

    @Test
    void testConstructorWithNullValues() {
        // Act
        ConversionRequest request = new ConversionRequest(null, null);

        // Assert
        assertNull(request.getInputFile());
        assertNull(request.getOutputName());
        assertNull(request.getProperties());
    }

    @Test
    void testConstructorWithEmptyStrings() {
        // Act
        ConversionRequest request = new ConversionRequest("", "");

        // Assert
        assertEquals("", request.getInputFile());
        assertEquals("", request.getOutputName());
        assertNull(request.getProperties());
    }

    @Test
    void testMultipleSetOperations() {
        // Arrange
        ConversionRequest request = new ConversionRequest();

        // Act & Assert - Multiple set operations should work correctly
        request.setInputFile("first.xml");
        assertEquals("first.xml", request.getInputFile());

        request.setInputFile("second.xml");
        assertEquals("second.xml", request.getInputFile());

        request.setOutputName("first.pdf");
        assertEquals("first.pdf", request.getOutputName());

        request.setOutputName("second.pdf");
        assertEquals("second.pdf", request.getOutputName());

        request.setProperties("{\"first\":\"value\"}");
        assertEquals("{\"first\":\"value\"}", request.getProperties());

        request.setProperties("{\"second\":\"value\"}");
        assertEquals("{\"second\":\"value\"}", request.getProperties());
    }
}
