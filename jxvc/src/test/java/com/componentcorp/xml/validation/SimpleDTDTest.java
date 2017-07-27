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
package com.componentcorp.xml.validation;

import com.componentcorp.xml.validation.base.ValidatorHandlerConstructionCallback;
import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest;
import static com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest.INTRINSIC_NS_URI;
import com.componentcorp.xml.validation.test.helpers.TestContentHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;

/**
 *
 * @author rlamont
 */
public class SimpleDTDTest extends BaseXMLValidationTest{
    
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    public SimpleDTDTest() {
    }
    
    @Before
    public void setUp() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void simpleSAXDTDHandler(){
        try {
            Map<String,Object> properties = new HashMap();
            properties.put(ValidationConstants.PROPERTY_DEFAULT_VALIDATOR, null);
            Map<String,Boolean> features = Collections.emptyMap();
            Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/dtd/internal-dtd-test.xml",features, properties);
            if (!faults.isEmpty()){
                faults.iterator().next().printStackTrace();
            }
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXNotSupportedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } catch (SAXNotRecognizedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }
    
    @Override
    protected Map<String, String> getResourceMap() {
        return RESOURCE_LOCATIONS;
    }
    
    protected Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile,Map<String,Boolean> features, Map<String,Object> properties) throws SAXNotSupportedException, SAXNotRecognizedException {
        TestContentHandler contentHandler = new TestContentHandler(getResourceMap());
        HandlerConstructionCallback callback = new HandlerConstructionCallback();
        TestDeclHandler declHandler=new TestDeclHandler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(INTRINSIC_NS_URI);
        if (schemaFactory == null) {
            fail("Should have found the intrinsic factory");
        }
        
        applyFeaturesAndProperties(schemaFactory, features, properties);
        schemaFactory.setProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, callback);
        try {
            Schema schema = schemaFactory.newSchema();
            //ValidatorHandler handler =schema.newValidatorHandler();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setSchema(schema);
            assertNull("Handler should not have been constructed yet!",callback.getHandlerProxy());
            SAXParser parser = parserFactory.newSAXParser();
            assertNotNull("Handler should have been constructed by now!",callback.getHandlerProxy());
            FeaturePropertyProvider handlerProxy = callback.getHandlerProxy();
            parser.getXMLReader().setProperty("http://xml.org/sax/properties/declaration-handler", handlerProxy.getProperty(ValidationConstants.PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER));
            handlerProxy.setProperty(ValidationConstants.PROPERTY_DECLARATION_HANDLER, declHandler);
            InputStream is = getClass().getResourceAsStream(testFile);
            parser.parse(is, contentHandler);
            
            assertEquals("DeclHandler not called: incorrect last Element Name","img",declHandler.lastElementName);
            assertEquals("DeclHandler not called: incorrect last Element Model","ANY",declHandler.lastElementModel);
            assertEquals("DeclHandler not called: incorrect last Attribute Element Name","img",declHandler.lastAttributeEName);
            assertEquals("DeclHandler not called: incorrect last Attribute Name","type",declHandler.lastAttributeAName);
            assertEquals("DeclHandler not called: incorrect last Attribute Type","NOTATION (type-image-svg|type-image-gif)",declHandler.lastAttributeType);
            assertEquals("DeclHandler not called: incorrect last Attribute Mode","#IMPLIED",declHandler.lastAttributeMode);
            assertNull("DeclHandler not called: incorrect last Attribute Value",declHandler.lastAttributeValue);
            assertEquals("DeclHandler not called: incorrect last Internal Entity Name","example1GIFTitle",declHandler.lastInternalEntityDeclName);
            assertEquals("DeclHandler not called: incorrect last Internal Entity Value","Title of example1.gif",declHandler.lastInternalEntityDeclValue);
            assertEquals("DeclHandler not called: incorrect last External Entity Name","example1SVG",declHandler.lastExternalEntityDeclName);
            assertNull("DeclHandler not called: incorrect last External Entity Public Id",declHandler.lastExternalEntityDeclPublicId);
            assertNotNull("DeclHandler not called: incorrect last External Entity System Id",declHandler.lastExternalEntityDeclSystemId);
            
            assertTrue("DeclHandler not called: incorrect last External Entity System Id",declHandler.lastExternalEntityDeclSystemId.endsWith("/jxvc/jxvc/src/test/resources/dtd/example1.svg"));
        } catch (SAXException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an SAXException creating schema");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            fail("Should not have thrown an ParserConfigurationException creating schema");
        } catch (IOException io) {
            io.printStackTrace();
            fail("Should not have thrown an IOException creating schema");
        }
        return contentHandler.getFaults();
    }

    class HandlerConstructionCallback implements ValidatorHandlerConstructionCallback{
        private FeaturePropertyProvider handlerProxy;

        @Override
        public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
            this.handlerProxy=instrinsicValidatorHandlerProxy;
        }

        public FeaturePropertyProvider getHandlerProxy() {
            return handlerProxy;
        }
        
    }
    
    class TestDeclHandler implements DeclHandler{
        private String lastElementName;
        private String lastElementModel;
        private String lastAttributeEName;
        private String lastAttributeAName;
        private String lastAttributeType;
        private String lastAttributeValue;
        private String lastAttributeMode;
        private String lastInternalEntityDeclName;
        private String lastInternalEntityDeclValue;
        private String lastExternalEntityDeclName;
        private String lastExternalEntityDeclPublicId;
        private String lastExternalEntityDeclSystemId;

        @Override
        public void elementDecl(String name, String model) throws SAXException {
            this.lastElementName=name;
            this.lastElementModel=model;
        }

        @Override
        public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
            this.lastAttributeEName=eName;
            this.lastAttributeAName=aName;
            this.lastAttributeType=type;
            this.lastAttributeValue=value;
            this.lastAttributeMode=mode;
        }

        @Override
        public void internalEntityDecl(String name, String value) throws SAXException {
            this.lastInternalEntityDeclName=name;
            this.lastInternalEntityDeclValue=value;
        }

        @Override
        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
            this.lastExternalEntityDeclName=name;
            this.lastExternalEntityDeclPublicId=publicId;
            this.lastExternalEntityDeclSystemId=systemId;
        }
        
        
    }

}
