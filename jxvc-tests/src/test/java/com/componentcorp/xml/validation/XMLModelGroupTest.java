/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation;

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import com.componentcorp.xml.validation.base.ValidatorHandlerConstructionCallback;
import com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public class XMLModelGroupTest extends BaseXMLValidationTest {
    
    public static final String SIMPLE_RNC_SYSTEM_ID="http://componentcorp/schema/jxvc/test/simpletest.rnc";
    public static final String SIMPLE_RNC_LOCATION="/schema/simpletest.rnc";
    public static final String SIMPLE_XSD_SYSTEM_ID="http://componentcorp/schema/jxvc/test/simpleRootSchema.xsd";
    public static final String SIMPLE_XSD_LOCATION="/schema/simpleRootSchema.xsd";
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    static{
        RESOURCE_LOCATIONS.put(SIMPLE_RNC_SYSTEM_ID, SIMPLE_RNC_LOCATION);
        RESOURCE_LOCATIONS.put(SIMPLE_XSD_SYSTEM_ID, SIMPLE_XSD_LOCATION);
    }
    
    private ValidatorHandlerConstructionCallback callback;
    
    public XMLModelGroupTest() {
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
    public void defaultGroupTest() throws SAXException{
        callback=new ValidatorHandlerConstructionCallback() {
            @Override
            public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
                try {
                    instrinsicValidatorHandlerProxy.setProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS, null);
                } catch (SAXNotRecognizedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                } catch (SAXNotSupportedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                }
            }
        };
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/group-test-default.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void default2GroupTest() throws SAXException{
        callback=new ValidatorHandlerConstructionCallback() {
            @Override
            public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
                try {
                    instrinsicValidatorHandlerProxy.setProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS, null);
                } catch (SAXNotRecognizedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                } catch (SAXNotSupportedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                }
            }
        };
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/group-test-default-2.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void default3GroupTest() throws SAXException{
        callback=new ValidatorHandlerConstructionCallback() {
            @Override
            public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
                try {
                    instrinsicValidatorHandlerProxy.setProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS, "");
                } catch (SAXNotRecognizedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                } catch (SAXNotSupportedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                }
            }
        };
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/group-test-default.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void default4GroupTest() throws SAXException{
        callback=new ValidatorHandlerConstructionCallback() {
            @Override
            public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
                try {
                    instrinsicValidatorHandlerProxy.setProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS, "");
                } catch (SAXNotRecognizedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                } catch (SAXNotSupportedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                }
            }
        };
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/group-test-default-2.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void alternateGroupTest() throws SAXException{
        callback=new ValidatorHandlerConstructionCallback() {
            @Override
            public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
                try {
                    instrinsicValidatorHandlerProxy.setProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS, "active");
                } catch (SAXNotRecognizedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                } catch (SAXNotSupportedException ex) {
                    fail("Should have been able to set ValidationConstants.PROPERTY_XML_MODEL_GROUPS");
                }
            }
        };
        Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/group-test-alternate.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    

    @Override
    protected Map<String, String> getResourceMap() {
        return RESOURCE_LOCATIONS;
    }

    @Override
    protected void handlerFeatureSetupCallback(FeaturePropertyProvider featuresAndProperties) {
        super.handlerFeatureSetupCallback(featuresAndProperties); 
        if (callback!=null){
            callback.onConstruction(featuresAndProperties);
        }
    }
    
    
    
}
