/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eightfoldconsulting.nimas2pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 *
 * @author Greg
 */
public class XMLEntityResolver implements EntityResolver {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private static String jarDirPath;

    /**
     * 
     */
    public XMLEntityResolver(){
        try {
            File jarFile = new File(FrameMain.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString());
            String jarDir = jarFile.getParent().toString();
            jarDirPath = URLDecoder.decode(jarDir, "UTF-8");
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }
 @Override
public InputSource resolveEntity (String publicId, String systemId)
   {

     //return new InputSource(new StringReader(""));
     if (systemId.equals("http://openebook.org/dtds/oeb-1.2/oebpkg12.dtd")) {
         File dtbFile = new File(jarDirPath, "xml/dtd/oebpkg12.dtd");
         InputStream in;
             // debug message
         logger.info("Resolving entity "+systemId);
            try {
                in = new FileInputStream(dtbFile);
                return new InputSource(in);

            } catch (FileNotFoundException ex) {
                logger.severe("openebook DTB not found: "+ex.getMessage());
                return null;
            }
       
     } else if (systemId.equals("http://openebook.org/dtds/oeb-1.2/oeb12.ent")) {
          File dtbFile = new File(jarDirPath, "xml/dtd/oeb12.ent ");
         InputStream in;
             // debug message
         logger.info("Resolving entity: "+systemId);
            try {
                in = new FileInputStream(dtbFile);
                return new InputSource(in);

            } catch (FileNotFoundException ex) {
                logger.severe("openebook ENT not found: "+ex.getMessage());
                return null;
            }

     } else if (systemId.equals("http://www.daisy.org/z3986/2005/dtbook-2005-3.dtd")) {
          File dtbFile = new File(jarDirPath, "xml/dtd/dtbook-2005-3.dtd");
         InputStream in;
             // debug message
         logger.info("Resolving entity "+systemId);
            try {
                in = new FileInputStream(dtbFile);
                return new InputSource(in);

            } catch (FileNotFoundException ex) {
                logger.severe("DTBook DTB not found: "+ex.getMessage());
                return null;
            }

     }else{
              // debug message
         logger.info("Entity resolver provided empty string for: "+systemId);
      return new InputSource(new StringReader(""));
       //return null;
     }
   }
}
