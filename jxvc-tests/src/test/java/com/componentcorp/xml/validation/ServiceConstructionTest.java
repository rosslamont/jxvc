/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import junit.framework.TestCase;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

/**
 *
 * @author rlamont
 */
public class ServiceConstructionTest extends TestCase {
    public static final String RELAXNG_COMPACT_URI = "http://www.iana.org/assignments/media-types/application/relax-ng-compact-syntax";
    public static final String RELAXNG_XML_URI = "http://relaxng.org/ns/structure/1.0";
    
    public ServiceConstructionTest(String testName) {
        super(testName);
    }

    public void testNewCompactSyntaxFactory(){
        SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_COMPACT_URI);
        assertNotNull(factory);
        assertTrue(factory.isSchemaLanguageSupported(RELAXNG_COMPACT_URI));
    }

    public void testNewXMLSyntaxFactory(){
        SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_XML_URI);
        assertNotNull(factory);
        assertTrue(factory.isSchemaLanguageSupported(RELAXNG_XML_URI));
    }

    public void testNewXMLSyntaxFactory2(){
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        assertNotNull(factory);
        assertTrue(factory.isSchemaLanguageSupported(XMLConstants.RELAXNG_NS_URI));
    }
    
    public void testXMLSyntaxValidationProperSyntax(){
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            CountingErrorHandler errorHandler=new CountingErrorHandler();
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/relaxNGSchema.xml");
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
            
            Schema validationSchema = factory.newSchema(schemaUrl);
            Validator validator = validationSchema.newValidator();
            validator.setErrorHandler(errorHandler);
            input=schemaUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
        } catch (SAXException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "sax exception thrown",ex);
            fail("sax exception thrown");
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }
    
    public void testXMLSyntaxValidationFailingSchema(){
        
        CountingErrorHandler errorHandler=new CountingErrorHandler();
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/notASchema.xml");
            Schema validationSchema = factory.newSchema(schemaUrl);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
            Validator validator = validationSchema.newValidator();
            validator.setErrorHandler(errorHandler);
            input=schemaUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertFalse("No errors detected",errorHandler.getWarningCount()+errorHandler.getErrorCount()+errorHandler.getFatalErrorCount()==0);
        } catch (SAXException ex) {
            assertEquals("invalid schema",ex.getMessage());
            assertEquals(0,errorHandler.getWarningCount());
            assertEquals(2,errorHandler.getErrorCount());
            assertEquals(0,errorHandler.getFatalErrorCount());
                    
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }

    public void testXMLSyntaxValidationFailingSyntax(){
        
        CountingErrorHandler errorHandler=new CountingErrorHandler();
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/relaxNGSchema.xml");
            URL instanceUrl =this.getClass().getResource("/notASchema.xml");
            Schema validationSchema = factory.newSchema(schemaUrl);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
            Validator validator = validationSchema.newValidator();
            validator.setErrorHandler(errorHandler);
            input=instanceUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertFalse("No errors detected",errorHandler.getWarningCount()+errorHandler.getErrorCount()+errorHandler.getFatalErrorCount()==0);
        } catch (SAXException ex) {
            assertEquals("invalid schema",ex.getMessage());
            assertEquals(0,errorHandler.getWarningCount());
            assertEquals(2,errorHandler.getErrorCount());
            assertEquals(0,errorHandler.getFatalErrorCount());
                    
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }

    
    public void testCompactSyntaxSimpleValidation(){
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_COMPACT_URI);
            CountingErrorHandler errorHandler=new CountingErrorHandler();
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/simpletest.rnc");
            URL instanceUrl =this.getClass().getResource("/simple.xml");
            Schema validationSchema = factory.newSchema(schemaUrl);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
            Validator validator = validationSchema.newValidator();
            validator.setErrorHandler(errorHandler);
            input=instanceUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
        } catch (SAXException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "sax exception thrown",ex);
            fail("sax exception thrown");
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }
    
    
    public void testCompactSyntaxValidationProperSyntax(){
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_COMPACT_URI);
            CountingErrorHandler errorHandler=new CountingErrorHandler();
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/relaxNGCompactSchema.rnc");
            URL instanceUrl =this.getClass().getResource("/relaxNGSchema.xml");
            Schema validationSchema = factory.newSchema(schemaUrl);
            Validator validator = validationSchema.newValidator();
            input=instanceUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
        } catch (SAXException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "sax exception thrown",ex);
            fail("sax exception thrown");
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }
    
    public void testCompactSyntaxValidationFailingSyntax(){
        
        CountingErrorHandler errorHandler=new CountingErrorHandler();
        InputStream input=null;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_COMPACT_URI);
            factory.setErrorHandler(errorHandler);
            URL schemaUrl=this.getClass().getResource("/relaxNGCompactSchema.rnc");
            URL instanceUrl =this.getClass().getResource("/notASchema.xml");
            Schema validationSchema = factory.newSchema(schemaUrl);
            assertEquals("Warning count is not zero",0,errorHandler.getWarningCount());
            assertEquals("Error count is not zero",0,errorHandler.getErrorCount());
            assertEquals("FatalError count is not zero",0,errorHandler.getFatalErrorCount());
            Validator validator = validationSchema.newValidator();
            validator.setErrorHandler(errorHandler);
            input=instanceUrl.openStream();
            StreamSource streamSource=new StreamSource(input);
            validator.validate(streamSource);
            assertFalse("No errors detected",errorHandler.getWarningCount()+errorHandler.getErrorCount()+errorHandler.getFatalErrorCount()==0);
        } catch (SAXException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error exception ",ex);
            assertEquals("invalid schema",ex.getMessage());
            assertEquals(0,errorHandler.getWarningCount());
            assertEquals(2,errorHandler.getErrorCount());
            assertEquals(0,errorHandler.getFatalErrorCount());
                    
        } catch (IOException ex) {
            Logger.getLogger(ServiceConstructionTest.class.getName()).log(Level.SEVERE, "IOException thrown",ex);
            fail("sax exception thrown");
        }
    }

}
