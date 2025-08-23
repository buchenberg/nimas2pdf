package org.eightfoldconsulting.nimas2pdf.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConversionResponse DTO.
 */
class ConversionResponseTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ConversionResponse response = new ConversionResponse();

        // Assert
        assertNull(response.getConversionId());
        assertNull(response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    void testParameterizedConstructor_WithConversionId() {
        // Arrange
        String conversionId = "test-123";
        String message = "Conversion started";

        // Act
        ConversionResponse response = new ConversionResponse(conversionId, message);

        // Assert
        assertEquals(conversionId, response.getConversionId());
        assertEquals(message, response.getMessage());
        assertTrue(response.isSuccess());
    }

    @Test
    void testParameterizedConstructor_WithoutConversionId() {
        // Arrange
        String message = "Conversion failed";

        // Act
        ConversionResponse response = new ConversionResponse(null, message);

        // Assert
        assertNull(response.getConversionId());
        assertEquals(message, response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ConversionResponse response = new ConversionResponse();
        String conversionId = "test-456";
        String message = "Test message";
        boolean success = true;

        // Act
        response.setConversionId(conversionId);
        response.setMessage(message);
        response.setSuccess(success);

        // Assert
        assertEquals(conversionId, response.getConversionId());
        assertEquals(message, response.getMessage());
        assertEquals(success, response.isSuccess());
    }

    @Test
    void testSuccessCalculation_WithConversionId() {
        // Arrange
        String conversionId = "test-789";
        String message = "Success message";

        // Act
        ConversionResponse response = new ConversionResponse(conversionId, message);

        // Assert
        assertTrue(response.isSuccess());
    }

    @Test
    void testSuccessCalculation_WithoutConversionId() {
        // Arrange
        String message = "Error message";

        // Act
        ConversionResponse response = new ConversionResponse(null, message);

        // Assert
        assertFalse(response.isSuccess());
    }

    @Test
    void testSuccessCalculation_EmptyConversionId() {
        // Arrange
        String message = "Error message";

        // Act
        ConversionResponse response = new ConversionResponse("", message);

        // Assert
        assertFalse(response.isSuccess());
    }

    @Test
    void testSuccessCalculation_BlankConversionId() {
        // Arrange
        String message = "Error message";

        // Act
        ConversionResponse response = new ConversionResponse("   ", message);

        // Assert
        assertFalse(response.isSuccess());
    }
}
