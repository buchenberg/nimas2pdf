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
import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;


/**
 *
 * @author Gregory Buchenberger
 */
public class OPFReader {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String title;
    private String publisher;
    private String identifier;
    private String daisyXML;
    private String subject;
    private File opfFile;
    private Document xmlDocument;
    private XPath xPath;
    private NodeList imageList;

    /**
     *
     * @param opffile
     */
    public OPFReader(File opffile) {
        this.opfFile = opffile;
        initObjects();
        this.readOPF();
    }

    private void initObjects() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //dbf.setAttribute("http: // apache.org / xml / features / nonvalidating / load-external-dtd",false);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            builder.setEntityResolver(new XMLEntityResolver());
            xmlDocument = builder.parse(opfFile);
            xPath = XPathFactory.newInstance().newXPath();
        } catch (IOException ex) {
            logger.severe("IO Exception: "+ex.getMessage());
        } catch (SAXException ex) {
            logger.severe("SAX Exception: "+ex.getMessage());
        } catch (ParserConfigurationException ex) {
            logger.severe("Parser Configuration Exception: "+ex.getMessage());
        }
    }

    private Object eval(String expression,
            QName returnType) {
        try {
            XPathExpression xPathExpression =
                    xPath.compile(expression);
            return xPathExpression.evaluate(xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            logger.severe("XPath Expression Exception: "+ex.getMessage());
            return null;
        }
    }

    private void readOPF() {
        // get Title
        String expression = "//Title/text()";
        title = (String) eval(expression, XPathConstants.STRING);
        // get Publisher
        expression = "//Publisher/text()";
        publisher = (String) eval(expression, XPathConstants.STRING);
        // get Subject
        expression = "/package/metadata/dc-metadata/Subject/text()";
        subject = (String) eval(expression, XPathConstants.STRING);
        // get Identifier
        expression = "//Identifier[1]/text()";
        identifier = (String) eval(expression, XPathConstants.STRING);
        // get Daisy XML
        expression = "//spine/itemref[1]/@idref";
        String itemRef = (String) eval(expression, XPathConstants.STRING);
        expression = "//manifest/item[@id='" + itemRef + "']/@href";
        daisyXML = (String) eval(expression, XPathConstants.STRING);
        // get image list
        expression = "/package/manifest/item[@media-type='image/jpg']/@href|/package/manifest/item[@media-type='image/jpeg']/@href|/package/manifest/item[@media-type='image/png']/@href";
        imageList = (NodeList) eval(expression, XPathConstants.NODESET);
    }

        /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the daisyXML
     */
    public String getDaisyXML() {
        return daisyXML;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @return
     */
    public NodeList getImageList(){
        return imageList;
    }
}