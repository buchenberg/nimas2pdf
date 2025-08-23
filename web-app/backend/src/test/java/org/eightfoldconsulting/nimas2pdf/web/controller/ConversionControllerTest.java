package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionResponse;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.eightfoldconsulting.nimas2pdf.web.service.ConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ConversionController.
 */
@ExtendWith(MockitoExtension.class)
class ConversionControllerTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ConversionController conversionController;

    private MockMultipartFile mockFile;
    private static final String TEST_CONVERSION_ID = "test-conversion-123";

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
    }

    @Test
    void testConvertNimas_Success() throws Exception {
        // Arrange
        when(conversionService.startConversion(any(), anyString()))
            .thenReturn(TEST_CONVERSION_ID);

        // Act
        ResponseEntity<ConversionResponse> response = conversionController.convertNimas(mockFile, "output.pdf");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_CONVERSION_ID, response.getBody().getConversionId());
        assertEquals("Conversion started", response.getBody().getMessage());
        verify(conversionService).startConversion(mockFile, "output.pdf");
    }

    @Test
    void testConvertNimas_WithoutOutputName() throws Exception {
        // Arrange
        when(conversionService.startConversion(any(), any()))
            .thenReturn(TEST_CONVERSION_ID);

        // Act
        ResponseEntity<ConversionResponse> response = conversionController.convertNimas(mockFile, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_CONVERSION_ID, response.getBody().getConversionId());
        verify(conversionService).startConversion(mockFile, null);
    }

    @Test
    void testConvertNimas_ServiceException() throws Exception {
        // Arrange
        when(conversionService.startConversion(any(), anyString()))
            .thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<ConversionResponse> response = conversionController.convertNimas(mockFile, "output.pdf");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error: Service error", response.getBody().getMessage());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetStatus_Success() {
        // Arrange
        ConversionStatus mockStatus = new ConversionStatus();
        mockStatus.setConversionId(TEST_CONVERSION_ID);
        mockStatus.setStatus("PROCESSING");
        when(conversionService.getStatus(TEST_CONVERSION_ID)).thenReturn(mockStatus);

        // Act
        ResponseEntity<ConversionStatus> response = conversionController.getStatus(TEST_CONVERSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_CONVERSION_ID, response.getBody().getConversionId());
        assertEquals("PROCESSING", response.getBody().getStatus());
    }

    @Test
    void testGetStatus_NotFound() {
        // Arrange
        when(conversionService.getStatus(TEST_CONVERSION_ID)).thenReturn(null);

        // Act
        ResponseEntity<ConversionStatus> response = conversionController.getStatus(TEST_CONVERSION_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDownloadPdf_Success() throws Exception {
        // Arrange
        Resource mockResource = new ByteArrayResource("PDF content".getBytes());
        when(conversionService.getPdfResource(TEST_CONVERSION_ID)).thenReturn(mockResource);

        // Act
        ResponseEntity<Resource> response = conversionController.downloadPdf(TEST_CONVERSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().exists());
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("attachment"));
    }

    @Test
    void testDownloadPdf_NotFound() throws Exception {
        // Arrange
        when(conversionService.getPdfResource(TEST_CONVERSION_ID)).thenReturn(null);

        // Act
        ResponseEntity<Resource> response = conversionController.downloadPdf(TEST_CONVERSION_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDownloadPdf_ServiceException() throws Exception {
        // Arrange
        when(conversionService.getPdfResource(TEST_CONVERSION_ID))
            .thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<Resource> response = conversionController.downloadPdf(TEST_CONVERSION_ID);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetProperties_Success() {
        // Arrange
        Object mockProperties = new Object();
        when(conversionService.getProperties()).thenReturn(mockProperties);

        // Act
        ResponseEntity<?> response = conversionController.getProperties();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(conversionService).getProperties();
    }

    @Test
    void testUpdateProperties_Success() {
        // Arrange
        Object mockProperties = new Object();

        // Act
        ResponseEntity<?> response = conversionController.updateProperties(mockProperties);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(conversionService).updateProperties(mockProperties);
    }
}
