// Copyright (C) 2009 Eightfold Consulting LLC
//
// This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with this program;
// if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package org.eightfoldconsulting.nimas2pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamResult;

//FOP
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.PageSequenceResults;

//JAXP
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.eightfoldconsulting.nimas2pdf.config.ApplicationProperties;

/**
 * Class defining 3 methods used to convert xml as dtbook or xsl-fo to
 * PDF and from dtbook to xsl-fo
 */
public class XMLConverter {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private File xmlFile;
    private File xmlDir;
    private File xsltFile;
    private FopFactory fopFactory;

    /**
     *
     * @param xmlfile
     * @param xsltfile
     */
    public XMLConverter(File xmlfile, File xsltfile) {
        this.xmlFile = xmlfile;
        this.xsltFile = xsltfile;
        initFopFactory();
    }

    private void initFopFactory() {
        try {
            File jarFile = new File(FrameMain.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String jarDir = jarFile.getParent();
            xmlDir = xmlFile.getParentFile();

            // Create FOP configuration from config file
            File userconfig = new File(jarDir, "conf/fopconf.xml");

            // Create FOP factory with configuration file
            this.fopFactory = FopFactory.newInstance(userconfig.toURI());

            // Set base URL through builder
            FopFactoryBuilder builder = new FopFactoryBuilder(xmlDir.toURI());
            this.fopFactory = builder.build();

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void setParams(Transformer transformer) {
        String baseFontFamily = ApplicationProperties.getProperty("baseFontFamily", "Times New Roman");
        String headerFontFamily = ApplicationProperties.getProperty("headerFontFamily", "Arial");
        String baseFontSize = ApplicationProperties.getProperty("baseFontSize", "12pt");
        String tableFontSize = ApplicationProperties.getProperty("tableFontSize", "10pt");
        String bookmarkHeaders = ApplicationProperties.getProperty("bookmarkHeaders", "true");
        String bookmarkTables = ApplicationProperties.getProperty("bookmarkTables", "true");
        String pageWidth = ApplicationProperties.getProperty("pageWidth", "8.5in");
        String pageHeight = ApplicationProperties.getProperty("pageHeight", "11in");
        String pageOrientation = ApplicationProperties.getProperty("pageOrientation", "portrait");

        if (baseFontFamily != null) {
            transformer.setParameter("baseFontFamily", baseFontFamily);
        }
        if (headerFontFamily != null) {
            transformer.setParameter("headerFontFamily", headerFontFamily);
        }
        if (baseFontSize != null) {
            transformer.setParameter("baseFontSize", baseFontSize);
        }
        if (tableFontSize != null) {
            transformer.setParameter("tableFontSize", tableFontSize);
        }
        if (bookmarkHeaders != null) {
            transformer.setParameter("bookmarkHeaders", bookmarkHeaders);
        }
        if (bookmarkTables != null) {
            transformer.setParameter("bookmarkTables", bookmarkTables);
        }
        if (pageWidth != null) {
            transformer.setParameter("pageWidth", pageWidth);
        }
        if (pageHeight != null) {
            transformer.setParameter("pageHeight", pageHeight);
        }
        if (pageOrientation != null) {
            transformer.setParameter("pageOrientation", pageOrientation);
        }
    }

    /**
     * Method to convert NIMAS XML to XSL-FO using Xalan
     * 
     * @param resultFile
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    public void convertNIMAS2FO(File resultFile)
            throws IOException, SAXException, TransformerException {

        // debug message
        logger.info("Transforming " + xmlFile.getName() + " to XSL-FO...");
        logger.info("Using " + xsltFile.getName() + " for tranformation...");

        // Setup output
        OutputStream out = new java.io.FileOutputStream(resultFile);
        out = new java.io.BufferedOutputStream(out);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setEntityResolver(new XMLEntityResolver());
            // Setup JAXP using identity transformer
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            // Setup XSLT
            Source xslt = new StreamSource(xsltFile);
            TransformerHandler handler = factory.newTransformerHandler(xslt);
            Transformer transformer = handler.getTransformer();
            reader.setContentHandler(handler);
            // Set stylesheet parameters
            setParams(transformer);
            // Setup input for XSLT transformation
            InputStream in = new FileInputStream(xmlFile);
            InputSource src = new InputSource(in);

            Result res = new StreamResult(out);
            handler.setResult(res);

            // Start XSLT transformation and FOP processing by invoking the XML parser
            reader.parse(src);
        } catch (IOException ex) {
            logger.severe("IO Exception" + ex.getMessage());
        } catch (SAXException ex) {
            logger.severe("SAX Exception" + ex.getMessage());
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        } finally {
            logger.info("Transformation complete.");
            out.close();
        }
    }

    /**
     * Method to convert XSL-FO to a PDF file using FOP
     * 
     * @param opfReader
     * @param resultfile
     * @throws IOException
     * @throws FOPException
     */
    @SuppressWarnings("unchecked")
    public void convertFO2PDF(OPFReader opfReader, File resultfile)
            throws IOException, FOPException {

        OutputStream out = null;

        try {
            // configure user agent
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // PDF/A-1b
            boolean isPDFA = Boolean.parseBoolean(ApplicationProperties.getProperty("pdfA", "false"));
            if (isPDFA) {
                foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF/A-1b");
            }
            // Accessible PDF
            boolean isAccessiblePDF = Boolean.parseBoolean(ApplicationProperties.getProperty("accessiblePDF", "true"));
            if (isAccessiblePDF) {
                foUserAgent.setAccessibility(true);
            }
            foUserAgent.setTitle(opfReader.getTitle());
            foUserAgent.setSubject(opfReader.getSubject());
            foUserAgent.setKeywords(opfReader.getIdentifier());
            foUserAgent.setProducer("NIMAS2PDF");

            File foFile = new File(xmlDir, opfReader.getIdentifier() + ".fo");
            // debug message
            logger.info("Transforming " + foFile.getName() + " to PDF.");
            logger.info("Using " + xsltFile.getName() + " for tranformation.");
            // setup output based on parameter
            out = new java.io.FileOutputStream(resultfile);
            out = new java.io.BufferedOutputStream(out);

            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer

            // Setup input stream
            Source src = new StreamSource(foFile);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);

            // Result processing
            FormattingResults foResults = fop.getResults();
            java.util.List pageSequences = foResults.getPageSequences();
            for (java.util.Iterator it = pageSequences.iterator(); it.hasNext();) {
                PageSequenceResults pageSequenceResults = (PageSequenceResults) it.next();
                logger.info("PageSequence " + (String.valueOf(pageSequenceResults.getID()).length() > 0
                        ? pageSequenceResults.getID()
                        : "<no id>") + " generated " + pageSequenceResults.getPageCount() + " pages.");
            }
            logger.info("Generated " + foResults.getPageCount() + " pages in total.");

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        } finally {
            out.close();
        }
    }

    /**
     * Method to convert NIMAS XML to a PDF file using Xalan and FOP
     * 
     * @param opfReader
     * @param resultfile
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     * @throws FOPException
     */
    @SuppressWarnings("unchecked")
    public void convertXML2PDF(OPFReader opfReader, File resultfile)
            throws IOException, SAXException, TransformerException, FOPException {
        OutputStream out = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setEntityResolver(new XMLEntityResolver());
            // create user agent
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // PDF/A-1b
            boolean isPDFa = Boolean.parseBoolean(ApplicationProperties.getProperty("pdfA", "false"));
            if (isPDFa) {
                foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF/A-1b");
            }
            // Accessible PDF
            boolean isAccessiblePDF = Boolean.parseBoolean(ApplicationProperties.getProperty("accessiblePDF", "true"));
            if (isAccessiblePDF) {
                foUserAgent.setAccessibility(true);
            }
            foUserAgent.setTitle(opfReader.getTitle());
            foUserAgent.setSubject(opfReader.getSubject());
            foUserAgent.setKeywords(opfReader.getIdentifier());
            foUserAgent.setProducer("NIMAS2PDF");
            // debug message
            logger.info("Transforming " + xmlFile.getName() + " to PDF.");
            logger.info("Using " + xsltFile.getName() + " for tranformation.");

            // setup output based on parameter
            out = new java.io.FileOutputStream(resultfile);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Setup JAXP using identity transformer
                SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

                // Setup XSLT
                Source xslt = new StreamSource(xsltFile);
                TransformerHandler handler = factory.newTransformerHandler(xslt);
                Transformer transformer = handler.getTransformer();
                reader.setContentHandler(handler);
                // Set stylesheet parameters
                setParams(transformer);

                // Setup input for XSLT transformation
                InputStream in = new FileInputStream(xmlFile);
                InputSource src = new InputSource(in);

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                handler.setResult(res);
                // handler.getTransformer().setErrorListener(new DefaultErrorListener());

                // Start XSLT transformation and FOP processing by invoking the XML parser
                reader.parse(src);

                // Result processing
                FormattingResults formatingResults = fop.getResults();
                java.util.List pageSequences = formatingResults.getPageSequences();
                for (java.util.Iterator it = pageSequences.iterator(); it.hasNext();) {
                    PageSequenceResults pageSequenceResults = (PageSequenceResults) it.next();
                    logger.info("PageSequence " + (String.valueOf(pageSequenceResults.getID()).length() > 0
                            ? pageSequenceResults.getID()
                            : "<no id>") + " generated " + pageSequenceResults.getPageCount() + " pages.");
                }
                logger.info("Generated " + formatingResults.getPageCount() + " pages in total.");

            } catch (IOException ex) {
                logger.severe("IO Exception" + ex.getMessage());
            } catch (SAXException ex) {
                logger.severe("SAX Exception" + ex.getMessage());
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            } finally {
                out.close();
            }

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }
}
