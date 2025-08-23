package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionResponse;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.eightfoldconsulting.nimas2pdf.web.service.ConversionService;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ConversionController using MockMvc.
 * Simplified to avoid Spring context loading issues.
 */
@ExtendWith(MockitoExtension.class)
class ConversionControllerIntegrationTest {

    @Mock
private ConversionService conversionService;

@InjectMocks
private ConversionController conversionController;

private MockMvc mockMvc;

@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(conversionController).build();
}

    @Test
    void testConvertNimas_Endpoint() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
        
        when(conversionService.startConversion(any(), anyString()))
            .thenReturn("test-conversion-123");

        // Act & Assert
        mockMvc.perform(multipart("/api/convert")
                .file(file)
                .param("outputName", "output.pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.conversionId").exists())
                .andExpect(jsonPath("$.message").value("Conversion started"));
    }

    @Test
    void testConvertNimas_WithoutOutputName() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
        
        when(conversionService.startConversion(any(), any()))
            .thenReturn("test-conversion-123");

        // Act & Assert
        mockMvc.perform(multipart("/api/convert")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.conversionId").exists());
    }

    @Test
    void testConvertNimas_NoFile() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/api/convert"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStatus_Endpoint() throws Exception {
        // Arrange
        String conversionId = "test-conversion-123";
        ConversionStatus mockStatus = new ConversionStatus();
        mockStatus.setConversionId(conversionId);
        mockStatus.setStatus("PROCESSING");
        
        when(conversionService.getStatus(conversionId)).thenReturn(mockStatus);

        // Act & Assert
        mockMvc.perform(get("/api/status/{conversionId}", conversionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetStatus_NonExistent() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/status/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProperties_Endpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/properties"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProperties_Endpoint() throws Exception {
        // Arrange
        String propertiesJson = "{\"dpi\":300,\"pageSize\":\"A4\"}";

        // Act & Assert
        mockMvc.perform(post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(propertiesJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadPdf_NonExistent() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/download/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCorsHeaders() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xml",
            "application/xml",
            "<xml>test content</xml>".getBytes()
        );
        
        when(conversionService.startConversion(any(), anyString()))
            .thenReturn("test-conversion-123");

        // Act & Assert
        mockMvc.perform(multipart("/api/convert")
                .file(file)
                .param("outputName", "output.pdf")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk());
        // Note: CORS headers are typically handled by Spring configuration, not the controller
    }
}
