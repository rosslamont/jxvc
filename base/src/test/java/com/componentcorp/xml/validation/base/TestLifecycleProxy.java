/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author rlamont
 */
public class TestLifecycleProxy {
    public static final String SIMPLE_ROOT_SYSTEM_ID="http://componentcorp.com/schema/jxvc/test/simpleRootSchema.xsd";
    public static final String SIMPLE_ROOT_LOCATION="/schema/simpleRootSchema.xsd";
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    static{
        RESOURCE_LOCATIONS.put(SIMPLE_ROOT_SYSTEM_ID, SIMPLE_ROOT_LOCATION);
    }
    
    
    @Test
    public void testSimpleProxyParse() throws Exception
    {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        assertNotNull("Should have found factory",factory);
        SchemaFactory wrapper=new LifecycleProxySchemaFactory(factory);
        Callback callback = new Callback();
        wrapper.setProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, callback);
        InputStream is = this.getClass().getResourceAsStream("/schema/simpleRootSchema.xsd");
        assertNotNull(is);
        StreamSource source = new StreamSource(is, SIMPLE_ROOT_SYSTEM_ID);
        
        
        Schema schema = wrapper.newSchema(source);
        assertFalse("Callback should not yet have been made",callback.wasCalled);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        parserFactory.setSchema(schema);
        SAXParser parser=parserFactory.newSAXParser();
        is = this.getClass().getResourceAsStream("/xsi/simpleRoot.xml");
        assertNotNull(is);
        parser.parse(is, new DefaultHandler2());
        assertTrue("Callback should have been made",callback.wasCalled);
    }
    
    private class Callback implements ValidatorHandlerConstructionCallback{
        boolean wasCalled=false;
        @Override
        public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
            wasCalled=true;
        }
        
    }
}
