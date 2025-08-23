package org.eightfoldconsulting.nimas2pdf.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasPackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ConversionPropertiesController.
 */
@WebMvcTest(ConversionPropertiesController.class)
class ConversionPropertiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NimasPackageRepository packageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetConversionProperties_ExistingPackage() throws Exception {
        // Arrange
        Long packageId = 1L;
        ConversionProperties properties = new ConversionProperties();
        properties.setBaseFontSize("20pt");
        properties.setPageWidth("8.5in");
        
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        nimasPackage.setConversionProperties(properties);
        
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(nimasPackage));

        // Act & Assert
        mockMvc.perform(get("/api/packages/{packageId}/properties", packageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.baseFontSize").value("20pt"))
                .andExpect(jsonPath("$.pageWidth").value("8.5in"));
    }

    @Test
    void testGetConversionProperties_NonExistentPackage() throws Exception {
        // Arrange
        Long packageId = 999L;
        when(packageRepository.findById(packageId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/packages/{packageId}/properties", packageId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetConversionProperties_PackageWithoutProperties() throws Exception {
        // Arrange
        Long packageId = 1L;
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        // No conversion properties set (null)
        
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(nimasPackage));

        // Act & Assert
        mockMvc.perform(get("/api/packages/{packageId}/properties", packageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.baseFontSize").value("18pt")) // Default value
                .andExpect(jsonPath("$.pageWidth").value("8.5in")); // Default value
    }

    @Test
    void testUpdateConversionProperties_Success() throws Exception {
        // Arrange
        Long packageId = 1L;
        ConversionProperties newProperties = new ConversionProperties();
        newProperties.setBaseFontSize("22pt");
        newProperties.setPageWidth("11in");
        
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        NimasPackage savedPackage = new NimasPackage("test-123", "Test Package");
        savedPackage.setConversionProperties(newProperties);
        
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(nimasPackage));
        when(packageRepository.save(any(NimasPackage.class))).thenReturn(savedPackage);

        // Act & Assert
        mockMvc.perform(put("/api/packages/{packageId}/properties", packageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProperties)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.baseFontSize").value("22pt"))
                .andExpect(jsonPath("$.pageWidth").value("11in"));
    }

    @Test
    void testUpdateConversionProperties_NonExistentPackage() throws Exception {
        // Arrange
        Long packageId = 999L;
        ConversionProperties properties = new ConversionProperties();
        
        when(packageRepository.findById(packageId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/packages/{packageId}/properties", packageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(properties)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testResetConversionProperties_Success() throws Exception {
        // Arrange
        Long packageId = 1L;
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        NimasPackage savedPackage = new NimasPackage("test-123", "Test Package");
        savedPackage.setConversionProperties(new ConversionProperties()); // Default properties
        
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(nimasPackage));
        when(packageRepository.save(any(NimasPackage.class))).thenReturn(savedPackage);

        // Act & Assert
        mockMvc.perform(post("/api/packages/{packageId}/properties/reset", packageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.baseFontSize").value("18pt")) // Default value
                .andExpect(jsonPath("$.pageWidth").value("8.5in")); // Default value
    }
}
