package org.eightfoldconsulting.nimas2pdf.web.service;

import net.sf.saxon.s9api.*;
import org.apache.fop.apps.*;
import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service for converting NIMAS DTBook XML to PDF using XSLT transformation and Apache FOP.
 */
@Service
public class XMLConverter {

    private final TransformerFactory transformerFactory;

    public XMLConverter() {
        // Initialize TransformerFactory (will use Saxon-HE if available)
        this.transformerFactory = TransformerFactory.newInstance();
    }

    /**
     * Convert NIMAS DTBook XML to PDF
     * 
     * @param xmlFile Path to the DTBook XML file
     * @param outputPath Path where the PDF should be saved
     * @param xslStylesheet Path to the XSLT stylesheet (optional, will use default if null)
     * @param conversionProperties Conversion properties for the transformation (optional, will use defaults if null)
     * @throws Exception if conversion fails
     */
    public void convertToPdf(Path xmlFile, Path outputPath, Path xslStylesheet, ConversionProperties conversionProperties) throws Exception {
        // Step 1: Transform DTBook XML to XSL-FO using XSLT
        Path foFile = outputPath.resolveSibling(outputPath.getFileName().toString().replace(".pdf", ".fo"));
        transformXmlToFo(xmlFile, foFile, xslStylesheet, conversionProperties);
        
        // Step 2: Convert XSL-FO to PDF using Apache FOP
        convertFoToPdf(foFile, outputPath);
        
        // Clean up temporary FO file
        try {
            Files.deleteIfExists(foFile);
        } catch (IOException e) {
            // Log warning but don't fail the conversion
            System.err.println("Warning: Could not delete temporary FO file: " + e.getMessage());
        }
    }

    /**
     * Convert NIMAS DTBook XML to PDF with default conversion properties
     * 
     * @param xmlFile Path to the DTBook XML file
     * @param outputPath Path where the PDF should be saved
     * @param xslStylesheet Path to the XSLT stylesheet (optional, will use default if null)
     * @throws Exception if conversion fails
     */
    public void convertToPdf(Path xmlFile, Path outputPath, Path xslStylesheet) throws Exception {
        convertToPdf(xmlFile, outputPath, xslStylesheet, null);
    }

    /**
     * Transform DTBook XML to XSL-FO using XSLT
     */
    private void transformXmlToFo(Path xmlFile, Path foFile, Path xslStylesheet, ConversionProperties conversionProperties) throws Exception {
        // Use custom stylesheet if none provided
        if (xslStylesheet == null) {
            xslStylesheet = getCustomStylesheet();
        }

        // Create transformer
        Source xsltSource = new StreamSource(xslStylesheet.toFile());
        Transformer transformer = transformerFactory.newTransformer(xsltSource);
        
        // Use provided conversion properties or defaults
        ConversionProperties props = conversionProperties != null ? conversionProperties : new ConversionProperties();
        
        // Set transformation parameters from ConversionProperties
        transformer.setParameter("baseFontSize", props.getBaseFontSize());
        transformer.setParameter("tableFontSize", props.getTableFontSize());
        transformer.setParameter("lineHeight", props.getLineHeight());
        transformer.setParameter("baseFontFamily", props.getBaseFontFamily());
        transformer.setParameter("headerFontFamily", props.getHeaderFontFamily());
        transformer.setParameter("pageOrientation", props.getPageOrientation());
        transformer.setParameter("pageWidth", props.getPageWidth());
        transformer.setParameter("pageHeight", props.getPageHeight());
        transformer.setParameter("bookmarkHeaders", props.isBookmarkHeaders());
        transformer.setParameter("bookmarkTables", props.isBookmarkTables());
        
        // Perform transformation
        Source xmlSource = new StreamSource(xmlFile.toFile());
        try (OutputStream foStream = new FileOutputStream(foFile.toFile())) {
            transformer.transform(xmlSource, new javax.xml.transform.stream.StreamResult(foStream));
        }
    }

    /**
     * Convert XSL-FO to PDF using Apache FOP
     */
    private void convertFoToPdf(Path foFile, Path pdfFile) throws Exception {
        // Configure FOP
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        
        // Create FOP
        try (InputStream foStream = new FileInputStream(foFile.toFile());
             OutputStream pdfStream = new FileOutputStream(pdfFile.toFile())) {
            
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfStream);
            
            // Parse FO and generate PDF
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source foSource = new StreamSource(foStream);
            transformer.transform(foSource, new javax.xml.transform.sax.SAXResult(fop.getDefaultHandler()));
        }
    }

    /**
     * Get the custom XSLT stylesheet for DTBook to XSL-FO conversion
     */
    private Path getCustomStylesheet() throws Exception {
        try {
            // Try to get the stylesheet from classpath resources first
            ClassPathResource resource = new ClassPathResource("xml/xslt/dtbook2fo.xsl");
            if (resource.exists()) {
                return resource.getFile().toPath();
            }
            
            // Fallback to file system
            Path customStylesheet = Path.of("src/main/resources/xml/xslt/dtbook2fo.xsl");
            if (!Files.exists(customStylesheet)) {
                throw new FileNotFoundException("Required custom stylesheet xml/xslt/dtbook2fo.xsl not found in classpath or file system");
            }
            return customStylesheet;
        } catch (Exception e) {
            throw new FileNotFoundException("Failed to locate custom stylesheet: " + e.getMessage());
        }
    }


}
