/*
 * Copyright 2017 Component Corporation Pty Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.componentcorp.xml.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public class XMLSchemaValidationTest {
    
    public XMLSchemaValidationTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void simpleSAXRootXSDValidatorHandler(){
        Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRoot.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    
    @Test
    public void simpleSAXRootFailXSDValidatorHandler() throws SAXParseException{
        Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRootFail.xml");
        assertEquals("Should have been validation errors", 1,faults.size());
        SAXException se = faults.iterator().next();
        assertTrue("Exception thrown should have been a SAX ParseException",se instanceof SAXParseException);
        SAXParseException spe = (SAXParseException) se;
        final int FAIL_LINE_NO=20;
        final int FAIL_COL_NO=16;
        assertEquals("Wrong exception - wrong line no",FAIL_LINE_NO,spe.getLineNumber());
        assertEquals("Wrong exception - wrong column no",FAIL_COL_NO,spe.getColumnNumber());
    }
    
    @Test
    public void simpleSAXRootXSDValidator() throws SAXException{
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/simpleRoot.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    
    @Test
    public void simpleSAXRootFailXSDValidator() throws SAXException{
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/simpleRootFail.xml");
        assertEquals("Should have been validation errors", 1,faults.size());
        SAXException se = faults.iterator().next();
        assertTrue("Exception thrown should have been a SAX ParseException",se instanceof SAXParseException);
        SAXParseException spe = (SAXParseException) se;
        final int FAIL_LINE_NO=20;
        final int FAIL_COL_NO=16;
        assertEquals("Wrong exception - wrong line no",FAIL_LINE_NO,spe.getLineNumber());
        assertEquals("Wrong exception - wrong column no",FAIL_COL_NO,spe.getColumnNumber());
        
    }
    
    @Test
    public void simpleDOMRootXSDValidator() throws SAXException{
        Collection<SAXParseException> faults=performDOMValidatorTest("/xml-model/simpleRoot.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    
    @Test
    public void simpleDOMRootFailXSDValidator() throws SAXException{
        Collection<SAXParseException> faults=performDOMValidatorTest("/xml-model/simpleRootFail.xml");
        assertEquals("Should have been validation errors", 1,faults.size());
        SAXException se = faults.iterator().next();
        assertTrue("Exception thrown should have been a SAX ParseException",se instanceof SAXParseException);
        SAXParseException spe = (SAXParseException) se;
        final int FAIL_LINE_NO=20;
        final int FAIL_COL_NO=16;
        assertEquals("Wrong exception - wrong line no",FAIL_LINE_NO,spe.getLineNumber());
        assertEquals("Wrong exception - wrong column no",FAIL_COL_NO,spe.getColumnNumber());
    }
    


    @Test
    public void simpleSAXRootXSDXSIValidatorHandler(){
        Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xsi/simpleRoot.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    
    @Test
    public void simpleSAXRootFailXSDXSIValidatorHandler() throws SAXParseException{
        Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xsi/simpleRootFail.xml");
        assertEquals("Should have been validation errors", 1,faults.size());
        SAXException se = faults.iterator().next();
        assertTrue("Exception thrown should have been a SAX ParseException",se instanceof SAXParseException);
        SAXParseException spe = (SAXParseException) se;
        final int FAIL_LINE_NO=22;
        final int FAIL_COL_NO=16;
        assertEquals("Wrong exception - wrong line no",FAIL_LINE_NO,spe.getLineNumber());
        assertEquals("Wrong exception - wrong column no",FAIL_COL_NO,spe.getColumnNumber());
    }
    




    private Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile){
        TestContentHandler contentHandler = new TestContentHandler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(ValidationConstants.INTRINSIC_NS_URI);
        if (schemaFactory ==null){
            fail("Should have found the intrinsic factory");
        }
        try{
            Schema schema =schemaFactory.newSchema();
            //ValidatorHandler handler =schema.newValidatorHandler();
            
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setSchema(schema);
            SAXParser parser = parserFactory.newSAXParser();
            
            InputStream is = getClass().getResourceAsStream(testFile);
            parser.parse(is, contentHandler);
        }
        catch(SAXException ex){
            fail("Should not have thrown an SAXException creating schema");
        }
        catch(ParserConfigurationException pce){
            fail("Should not have thrown an ParserConfigurationException creating schema");
        }
        catch(IOException io){
            fail("Should not have thrown an IOException creating schema");
        }
        return contentHandler.getFaults();
    }

    private Collection<SAXParseException>  performSAXValidatorTest(String testFile) throws SAXException {
        final TestContentHandler contentHandler = new TestContentHandler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(ValidationConstants.INTRINSIC_NS_URI);
        if (schemaFactory ==null){
            fail("Should have found the intrinsic factory");
        }
        try{
            
            Schema schema =schemaFactory.newSchema();
            
            //ValidatorHandler handler =schema.newValidatorHandler();
            Validator validator=schema.newValidator();
            validator.setFeature(ValidationConstants.FEATURE_NAMESPACE_AWARE, true); //just test the feature is working even though its default
            validator.setErrorHandler(contentHandler);
            validator.setResourceResolver(contentHandler);
            InputStream is = getClass().getResourceAsStream(testFile);
            Source source = new StreamSource(is);
            Result result = new StreamResult();
            validator.validate(source, result);
        }
        catch(SAXException ex){
            fail("Should not have thrown an SAXException creating schema");
        }
        catch(IOException io){
            fail("Should not have thrown an IOException creating schema");
        }
        return contentHandler.getFaults();
    }

    private Collection<SAXParseException> performDOMValidatorTest(String testFile) {
        final TestContentHandler contentHandler = new TestContentHandler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(ValidationConstants.INTRINSIC_NS_URI);
        if (schemaFactory ==null){
            fail("Should have found the intrinsic factory");
        }
        try{
            Schema schema =schemaFactory.newSchema();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            docFactory.setSchema(schema);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            InputStream is = getClass().getResourceAsStream(testFile);
            docBuilder.setEntityResolver(contentHandler);
            docBuilder.setErrorHandler(contentHandler);
            docBuilder.parse(is);
        }
        catch(SAXException ex){
            fail("Should not have thrown an SAXException creating schema");
        }
        catch(IOException io){
            fail("Should not have thrown an IOException creating schema");
        } catch (ParserConfigurationException ex) {
            fail("Should not have thrown an ParserConfigurationException creating schema");
        }
        return contentHandler.getFaults();
    }
}
