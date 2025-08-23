package org.eightfoldconsulting.nimas2pdf.web.util;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Shared EntityResolver for handling DTD resolution locally.
 * This resolver intercepts DTD resolution requests and serves them from local classpath resources.
 */
public class DTDEntityResolver implements EntityResolver {
    
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        try {
            // Handle the specific DTBook DTD
            if (systemId != null && systemId.contains("dtbook-2005-3.dtd")) {
                ClassPathResource dtdResource = new ClassPathResource("xml/dtd/dtbook-2005-3.dtd");
                if (dtdResource.exists()) {
                    return new InputSource(dtdResource.getInputStream());
                }
            }
            
            // Handle other DTDs if needed
            if (systemId != null && systemId.contains("oeb12.ent")) {
                ClassPathResource entResource = new ClassPathResource("xml/dtd/oeb12.ent");
                if (entResource.exists()) {
                    return new InputSource(entResource.getInputStream());
                }
            }
            
            if (systemId != null && systemId.contains("oebpkg12.dtd")) {
                ClassPathResource pkgResource = new ClassPathResource("xml/dtd/oebpkg12.dtd");
                if (pkgResource.exists()) {
                    return new InputSource(pkgResource.getInputStream());
                }
            }
            
            // Return null to use default resolution behavior
            return null;
        } catch (Exception e) {
            throw new SAXException("Error resolving entity: " + systemId, e);
        }
    }
}
