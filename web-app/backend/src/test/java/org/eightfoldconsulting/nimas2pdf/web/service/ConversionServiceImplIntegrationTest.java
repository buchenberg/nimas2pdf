package org.eightfoldconsulting.nimas2pdf.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ConversionServiceImpl.
 * This test verifies the basic functionality without mocking.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ConversionServiceImplIntegrationTest {

    @Test
    void testConversionServiceCreation(@TempDir Path tempDir) throws Exception {
        // Test that the service can be created and basic operations work
        XMLConverter xmlConverter = new XMLConverter();
        ConversionServiceImpl conversionService = new ConversionServiceImpl(xmlConverter);
        
        assertNotNull(conversionService);
        
        // Test that the service returns default properties
        Object properties = conversionService.getProperties();
        assertNotNull(properties);
        assertTrue(properties instanceof org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties);
    }

    @Test
    void testDefaultConversionProperties() {
        org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties props = 
            new org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties();
        
        assertEquals("18pt", props.getBaseFontSize());
        assertEquals("18pt", props.getTableFontSize());
        assertEquals("1.5em", props.getLineHeight());
        assertEquals("DejaVu Sans", props.getBaseFontFamily());
        assertEquals("DejaVu Serif", props.getHeaderFontFamily());
        assertEquals("portrait", props.getPageOrientation());
        assertEquals("8.5in", props.getPageWidth());
        assertEquals("11in", props.getPageHeight());
        assertFalse(props.isBookmarkHeaders());
        assertFalse(props.isBookmarkTables());
    }
}
