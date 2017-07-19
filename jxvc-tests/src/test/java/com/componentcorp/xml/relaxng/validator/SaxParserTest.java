/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.relaxng.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author rlamont
 */
public class SaxParserTest extends TestCase {
    
    public SaxParserTest() {
    }
    
    @Before
    public void setUp() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    public void testSaxParsingLoadsCorrectValidator(){
        CountingErrorHandler errorHandler=new CountingErrorHandler();
//        InputStream input=null;
//        try {
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            factory.setValidating(true);
//            SAXParser parser = factory.newSAXParser();
//            XMLReader    reader = parser.getXMLReader();
//            URL schemaUrl=this.getClass().getResource("/relaxNGSchema.xml");
//            input=schemaUrl.openStream();
//            InputSource streamSource=new InputSource(input);
//            reader.setErrorHandler(errorHandler);
//            reader.parse(streamSource);
//            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
//            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
//            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(SaxParserTest.class.getName()).log(Level.SEVERE, null, ex);
//            fail("parser config exception thrown");
//        } catch (SAXException ex) {
//            Logger.getLogger(SaxParserTest.class.getName()).log(Level.SEVERE, null, ex);
//            fail("sax exception thrown");
//        } catch (IOException ex) {
//            Logger.getLogger(SaxParserTest.class.getName()).log(Level.SEVERE, null, ex);
//            fail("io exception thrown");
//        }
//        
    }
}
