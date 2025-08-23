package org.eightfoldconsulting.nimas2pdf.web.service;

import net.sf.saxon.s9api.*;
import org.apache.fop.apps.*;
import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.eightfoldconsulting.nimas2pdf.web.util.DTDEntityResolver;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Service for converting NIMAS DTBook XML to PDF using XSLT transformation and Apache FOP.
 */
@Service
public class XMLConverter {

    private final TransformerFactory transformerFactory;
    private final EntityResolver entityResolver;

    public XMLConverter() {
        // Initialize TransformerFactory (will use Saxon-HE if available)
        this.transformerFactory = TransformerFactory.newInstance();
        
        // Set URI resolver to handle includes as classpath resources
        this.transformerFactory.setURIResolver(new URIResolver() {
            @Override
            public Source resolve(String href, String base) throws TransformerException {
                try {
                    // Handle relative includes like "./includes/layout.xsl"
                    String resourcePath = href;
                    if (href.startsWith("./")) {
                        resourcePath = href.substring(2);
                    }
                    
                    // Convert to classpath resource path
                    String classpathPath = "xml/xslt/" + resourcePath;
                    ClassPathResource includeResource = new ClassPathResource(classpathPath);
                    
                    if (includeResource.exists()) {
                        return new StreamSource(includeResource.getInputStream());
                    } else {
                        throw new TransformerException("Could not resolve XSLT include: " + href);
                    }
                } catch (Exception e) {
                    throw new TransformerException("Error resolving XSLT include: " + href, e);
                }
            }
        });
        
        // Set up EntityResolver to handle DTD resolution locally
        this.entityResolver = new DTDEntityResolver();
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
        Source xsltSource;
        if (xslStylesheet == null) {
            // Get stylesheet from classpath as a stream
            ClassPathResource resource = new ClassPathResource("xml/xslt/dtbook2fo.xsl");
            if (!resource.exists()) {
                throw new FileNotFoundException("Required custom stylesheet xml/xslt/dtbook2fo.xsl not found in classpath");
            }
            xsltSource = new StreamSource(resource.getInputStream());
        } else {
            xsltSource = new StreamSource(xslStylesheet.toFile());
        }

        // Create transformer
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
        // Parse XML with proper DTD resolution using EntityResolver
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false); // Don't validate, just resolve entities
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(entityResolver);
        
        // Parse the XML file
        Document document = builder.parse(xmlFile.toFile());
        
        // Create DOM source for transformation
        javax.xml.transform.dom.DOMSource xmlSource = new javax.xml.transform.dom.DOMSource(document);
        
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
}
