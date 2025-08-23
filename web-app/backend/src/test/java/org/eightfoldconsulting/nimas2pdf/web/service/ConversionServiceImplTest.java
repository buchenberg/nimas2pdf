package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConversionServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ConversionServiceImplTest {

    private ConversionServiceImpl conversionService;

    @Mock
    private XMLConverter xmlConverter;

    @TempDir
    Path tempDir;

    private MockMultipartFile mockFile;
    private static final String TEST_FILENAME = "test.xml";

    @BeforeEach
    void setUp() {
        conversionService = new ConversionServiceImpl(xmlConverter);
        
        mockFile = new MockMultipartFile(
            "file",
            TEST_FILENAME,
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
    }

    @Test
    void testStartConversion_Success() throws Exception {
        // Act
        String conversionId = conversionService.startConversion(mockFile, "output.pdf");

        // Assert
        assertNotNull(conversionId);
        assertFalse(conversionId.isEmpty());
        
        // Wait a bit for the async conversion to start
        Thread.sleep(100);
        
        ConversionStatus status = conversionService.getStatus(conversionId);
        assertNotNull(status);
        assertEquals(conversionId, status.getConversionId());
        // Status could be PENDING, PROCESSING, or already COMPLETED due to async nature
        assertTrue(status.getStatus().equals("PENDING") || 
                  status.getStatus().equals("PROCESSING") || 
                  status.getStatus().equals("COMPLETED"));
        assertTrue(status.getMessage().contains("File uploaded") || 
                  status.getMessage().contains("Reading NIMAS file") ||
                  status.getMessage().contains("Conversion completed"));
    }

    @Test
    void testStartConversion_WithoutOutputName() throws Exception {
        // Act
        String conversionId = conversionService.startConversion(mockFile, null);

        // Assert
        assertNotNull(conversionId);
        
        // Wait a bit for the async conversion to start
        Thread.sleep(100);
        
        ConversionStatus status = conversionService.getStatus(conversionId);
        assertNotNull(status);
        // Status could be PENDING, PROCESSING, or already COMPLETED due to async nature
        assertTrue(status.getStatus().equals("PENDING") || 
                  status.getStatus().equals("PROCESSING") || 
                  status.getStatus().equals("COMPLETED"));
    }

    @Test
    void testStartConversion_WithEmptyOutputName() throws Exception {
        // Act
        String conversionId = conversionService.startConversion(mockFile, "");

        // Assert
        assertNotNull(conversionId);
        
        // Wait a bit for the async conversion to start
        Thread.sleep(100);
        
        ConversionStatus status = conversionService.getStatus(conversionId);
        assertNotNull(status);
        // Status could be PENDING, PROCESSING, or already COMPLETED due to async nature
        assertTrue(status.getStatus().equals("PENDING") || 
                  status.getStatus().equals("PROCESSING") || 
                  status.getStatus().equals("COMPLETED"));
    }

    @Test
    void testGetStatus_ExistingConversion() throws Exception {
        // Arrange
        String conversionId = conversionService.startConversion(mockFile, "output.pdf");

        // Act
        ConversionStatus status = conversionService.getStatus(conversionId);

        // Assert
        assertNotNull(status);
        assertEquals(conversionId, status.getConversionId());
        // Status could be PENDING, PROCESSING, or already COMPLETED due to async nature
        assertTrue(status.getStatus().equals("PENDING") || 
                  status.getStatus().equals("PROCESSING") || 
                  status.getStatus().equals("COMPLETED"));
    }

    @Test
    void testGetStatus_NonExistentConversion() {
        // Act
        ConversionStatus status = conversionService.getStatus("non-existent-id");

        // Assert
        assertNull(status);
    }

    @Test
    void testGetStatus_NullConversionId() {
        // Act
        ConversionStatus status = conversionService.getStatus(null);

        // Assert
        assertNull(status);
    }

    @Test
    void testGetPdfResource_NonExistentConversion() throws Exception {
        // Act
        Resource resource = conversionService.getPdfResource("non-existent-id");

        // Assert
        assertNull(resource);
    }

    @Test
    void testGetPdfResource_NullConversionId() throws Exception {
        // Act
        Resource resource = conversionService.getPdfResource(null);

        // Assert
        assertNull(resource);
    }

    @Test
    void testGetProperties() {
        // Act
        Object properties = conversionService.getProperties();

        // Assert
        assertNotNull(properties);
    }

    @Test
    void testUpdateProperties() {
        // Arrange
        Object testProperties = new Object();

        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> conversionService.updateProperties(testProperties));
    }

    @Test
    void testMultipleConversions() throws Exception {
        // Arrange
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.xml", "application/xml", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.xml", "application/xml", "content2".getBytes());

        // Act
        String conversionId1 = conversionService.startConversion(file1, "output1.pdf");
        String conversionId2 = conversionService.startConversion(file2, "output2.pdf");

        // Assert
        assertNotNull(conversionId1);
        assertNotNull(conversionId2);
        assertNotEquals(conversionId1, conversionId2);

        ConversionStatus status1 = conversionService.getStatus(conversionId1);
        ConversionStatus status2 = conversionService.getStatus(conversionId2);

        assertNotNull(status1);
        assertNotNull(status2);
        assertEquals(conversionId1, status1.getConversionId());
        assertEquals(conversionId2, status2.getConversionId());
    }

    @Test
    void testFileExtensionHandling() throws Exception {
        // Test with different file extensions
        MockMultipartFile xmlFile = new MockMultipartFile("xml", "test.xml", "application/xml", "content".getBytes());
        MockMultipartFile zipFile = new MockMultipartFile("zip", "test.zip", "application/zip", "content".getBytes());
        MockMultipartFile noExtFile = new MockMultipartFile("noext", "test", "text/plain", "content".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> conversionService.startConversion(xmlFile, "output.pdf"));
        assertDoesNotThrow(() -> conversionService.startConversion(zipFile, "output.pdf"));
        assertDoesNotThrow(() -> conversionService.startConversion(noExtFile, "output.pdf"));
    }

    @Test
    void testConversionIdUniqueness() throws Exception {
        // Act
        String conversionId1 = conversionService.startConversion(mockFile, "output1.pdf");
        String conversionId2 = conversionService.startConversion(mockFile, "output2.pdf");
        String conversionId3 = conversionService.startConversion(mockFile, "output3.pdf");

        // Assert
        assertNotNull(conversionId1);
        assertNotNull(conversionId2);
        assertNotNull(conversionId3);
        
        // All should be unique
        assertNotEquals(conversionId1, conversionId2);
        assertNotEquals(conversionId1, conversionId3);
        assertNotEquals(conversionId2, conversionId3);
    }
}
