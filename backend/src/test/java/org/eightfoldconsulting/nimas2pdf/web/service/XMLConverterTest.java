package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XMLConverterTest {

    private XMLConverter xmlConverter;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        xmlConverter = new XMLConverter();
    }

    @Test
    void testConvertToPdf() throws Exception {
        // Create a proper DTBook XML file with the expected namespace and structure
        String testXml = """
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
        
        Path xmlFile = tempDir.resolve("test.xml");
        Path pdfFile = tempDir.resolve("test.pdf");
        
        Files.writeString(xmlFile, testXml);
        
        // Perform conversion
        xmlConverter.convertToPdf(xmlFile, pdfFile, null);
        
        // Verify PDF was created
        assertTrue(Files.exists(pdfFile), "PDF file should be created");
        assertTrue(Files.size(pdfFile) > 0, "PDF file should not be empty");
        
        // Verify temporary FO file was cleaned up
        Path foFile = tempDir.resolve("test.fo");
        assertFalse(Files.exists(foFile), "Temporary FO file should be deleted");
    }

    @Test
    void testConvertToPdfWithCustomStylesheet() throws Exception {
        // Create a custom XSLT stylesheet
        String customXsl = """
            <?xml version="1.0" encoding="UTF-8"?>
            <xsl:stylesheet version="2.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
                
                <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
                
                <xsl:template match="/">
                    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
                        <fo:layout-master-set>
                            <fo:simple-page-master master-name="default-page" 
                                page-width="6in" 
                                page-height="9in"
                                margin="0.25in">
                                <fo:region-body margin="0.25in"/>
                            </fo:simple-page-master>
                        </fo:layout-master-set>
                        
                        <fo:page-sequence master-reference="default-page">
                            <fo:flow flow-name="xsl-region-body">
                                <fo:block font-size="16pt" font-weight="bold">
                                    Custom Stylesheet Test
                                </fo:block>
                                <xsl:apply-templates/>
                            </fo:flow>
                        </fo:page-sequence>
                    </fo:root>
                </xsl:template>
                
                <xsl:template match="*">
                    <xsl:apply-templates/>
                </xsl:template>
                
                <xsl:template match="text()">
                    <xsl:value-of select="."/>
                </xsl:template>
            </xsl:stylesheet>
            """;
        
        // Create test files
        String testXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <dtbook xmlns="http://www.daisy.org/z3986/2005/dtbook/" version="2005-3" xml:lang="en">
                <book>
                    <p>Simple test content</p>
                </book>
            </dtbook>
            """;
        
        Path xmlFile = tempDir.resolve("test2.xml");
        Path xslFile = tempDir.resolve("custom.xsl");
        Path pdfFile = tempDir.resolve("test2.pdf");
        
        Files.writeString(xmlFile, testXml);
        Files.writeString(xslFile, customXsl);
        
        // Perform conversion with custom stylesheet
        xmlConverter.convertToPdf(xmlFile, pdfFile, xslFile);
        
        // Verify PDF was created
        assertTrue(Files.exists(pdfFile), "PDF file should be created with custom stylesheet");
        assertTrue(Files.size(pdfFile) > 0, "PDF file should not be empty");
    }

    @Test
    void testConvertToPdfWithInvalidXml() {
        // Create invalid XML
        String invalidXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <dtbook>
                <book>
                    <unclosed>
                        <p>Invalid XML
                </book>
            </dtbook>
            """;
        
        Path xmlFile = tempDir.resolve("invalid.xml");
        Path pdfFile = tempDir.resolve("invalid.pdf");
        
        try {
            Files.writeString(xmlFile, invalidXml);
            
            // This should throw an exception
            assertThrows(Exception.class, () -> {
                xmlConverter.convertToPdf(xmlFile, pdfFile, null);
            }, "Should throw exception for invalid XML");
            
        } catch (Exception e) {
            fail("Failed to write test file: " + e.getMessage());
        }
    }

    @Test
    void testConvertToPdfWithDtdReference() throws Exception {
        // Create test XML with DTD reference
        String testXmlWithDtd = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE dtbook PUBLIC "-//NISO//DTD dtbook 2005-3//EN" "http://www.daisy.org/z3986/2005/dtbook-2005-3.dtd">
            <dtbook xmlns="http://www.daisy.org/z3986/2005/dtbook/" version="2005-3" xml:lang="En-US">
                <head/>
                <book>
                    <frontmatter>
                        <doctitle>Test Book with DTD</doctitle>
                        <level1>
                            <h1>Test Chapter</h1>
                            <p>This is a test paragraph to verify DTD resolution works.</p>
                        </level1>
                    </frontmatter>
                </book>
            </dtbook>
            """;

        Path xmlFile = tempDir.resolve("test-with-dtd.xml");
        Path pdfFile = tempDir.resolve("test-with-dtd.pdf");
        
        Files.writeString(xmlFile, testXmlWithDtd);
        
        // This should now work with our EntityResolver
        xmlConverter.convertToPdf(xmlFile, pdfFile, null);
        
        assertTrue(Files.exists(pdfFile), "PDF file should be created even with DTD reference");
        assertTrue(Files.size(pdfFile) > 0, "PDF file should not be empty");
        
        // Clean up
        Files.deleteIfExists(xmlFile);
        Files.deleteIfExists(pdfFile);
    }
}
