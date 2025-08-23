package org.eightfoldconsulting.nimas2pdf.web.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConversionStatus DTO.
 */
class ConversionStatusTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ConversionStatus status = new ConversionStatus();

        // Assert
        assertNull(status.getConversionId());
        assertNull(status.getStatus());
        assertEquals(0, status.getProgress());
        assertNull(status.getMessage());
        assertNull(status.getStartTime());
        assertNull(status.getLastUpdate());
        assertNull(status.getOutputFileName());
        assertEquals(0, status.getFileSize());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String conversionId = "test-123";
        String status = "PROCESSING";
        int progress = 50;
        String message = "Processing file";

        // Act
        ConversionStatus conversionStatus = new ConversionStatus(conversionId, status, progress, message);

        // Assert
        assertEquals(conversionId, conversionStatus.getConversionId());
        assertEquals(status, conversionStatus.getStatus());
        assertEquals(progress, conversionStatus.getProgress());
        assertEquals(message, conversionStatus.getMessage());
        assertNotNull(conversionStatus.getStartTime());
        assertNotNull(conversionStatus.getLastUpdate());
        assertNull(conversionStatus.getOutputFileName());
        assertEquals(0, conversionStatus.getFileSize());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ConversionStatus status = new ConversionStatus();
        String conversionId = "test-456";
        String statusValue = "COMPLETED";
        int progress = 100;
        String message = "Conversion completed";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String outputFileName = "output.pdf";
        long fileSize = 1024L;

        // Act
        status.setConversionId(conversionId);
        status.setStatus(statusValue);
        status.setProgress(progress);
        status.setMessage(message);
        status.setStartTime(startTime);
        status.setLastUpdate(lastUpdate);
        status.setOutputFileName(outputFileName);
        status.setFileSize(fileSize);

        // Assert
        assertEquals(conversionId, status.getConversionId());
        assertEquals(statusValue, status.getStatus());
        assertEquals(progress, status.getProgress());
        assertEquals(message, status.getMessage());
        assertEquals(startTime, status.getStartTime());
        assertEquals(lastUpdate, status.getLastUpdate());
        assertEquals(outputFileName, status.getOutputFileName());
        assertEquals(fileSize, status.getFileSize());
    }

    @Test
    void testTimestamps_AreSetOnConstruction() {
        // Act
        ConversionStatus status = new ConversionStatus("test", "PENDING", 0, "Starting");

        // Assert
        assertNotNull(status.getStartTime());
        assertNotNull(status.getLastUpdate());
        
        // Timestamps should be very close to now
        LocalDateTime now = LocalDateTime.now();
        assertTrue(status.getStartTime().isBefore(now.plusSeconds(1)));
        assertTrue(status.getStartTime().isAfter(now.minusSeconds(1)));
        assertTrue(status.getLastUpdate().isBefore(now.plusSeconds(1)));
        assertTrue(status.getLastUpdate().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testProgressBounds() {
        // Arrange
        ConversionStatus status = new ConversionStatus();

        // Act & Assert - Progress should accept various values
        status.setProgress(0);
        assertEquals(0, status.getProgress());

        status.setProgress(50);
        assertEquals(50, status.getProgress());

        status.setProgress(100);
        assertEquals(100, status.getProgress());

        status.setProgress(-10);
        assertEquals(-10, status.getProgress());

        status.setProgress(150);
        assertEquals(150, status.getProgress());
    }

    @Test
    void testFileSizeHandling() {
        // Arrange
        ConversionStatus status = new ConversionStatus();

        // Act & Assert
        status.setFileSize(0L);
        assertEquals(0L, status.getFileSize());

        status.setFileSize(1024L);
        assertEquals(1024L, status.getFileSize());

        status.setFileSize(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, status.getFileSize());

        status.setFileSize(-1L);
        assertEquals(-1L, status.getFileSize());
    }

    @Test
    void testStatusValues() {
        // Arrange
        ConversionStatus status = new ConversionStatus();

        // Act & Assert - Status should accept various string values
        String[] validStatuses = {"PENDING", "PROCESSING", "COMPLETED", "FAILED", "CANCELLED"};
        
        for (String validStatus : validStatuses) {
            status.setStatus(validStatus);
            assertEquals(validStatus, status.getStatus());
        }

        // Test with null and empty values
        status.setStatus(null);
        assertNull(status.getStatus());

        status.setStatus("");
        assertEquals("", status.getStatus());
    }

    @Test
    void testMessageHandling() {
        // Arrange
        ConversionStatus status = new ConversionStatus();

        // Act & Assert
        status.setMessage("Normal message");
        assertEquals("Normal message", status.getMessage());

        status.setMessage("");
        assertEquals("", status.getMessage());

        status.setMessage(null);
        assertNull(status.getMessage());

        status.setMessage("Very long message with special characters: !@#$%^&*()");
        assertEquals("Very long message with special characters: !@#$%^&*()", status.getMessage());
    }

    @Test
    void testOutputFileNameHandling() {
        // Arrange
        ConversionStatus status = new ConversionStatus();

        // Act & Assert
        status.setOutputFileName("document.pdf");
        assertEquals("document.pdf", status.getOutputFileName());

        status.setOutputFileName("file with spaces.pdf");
        assertEquals("file with spaces.pdf", status.getOutputFileName());

        status.setOutputFileName("");
        assertEquals("", status.getOutputFileName());

        status.setOutputFileName(null);
        assertNull(status.getOutputFileName());
    }
}
