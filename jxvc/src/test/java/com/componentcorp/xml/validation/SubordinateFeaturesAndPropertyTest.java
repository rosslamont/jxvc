/*
 * Copyright 2017 rlamont.
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

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
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
public class SubordinateFeaturesAndPropertyTest extends BaseXMLValidationTest {
    
    public static final String SIMPLE_ROOT_SYSTEM_ID="http://componentcorp.com/schema/jxvc/test/simpleRootSchema.xsd";
    
    public static final String SIMPLE_ROOT_LOCATION="/schema/simpleRootSchema.xsd";
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    static{
        RESOURCE_LOCATIONS.put(SIMPLE_ROOT_SYSTEM_ID, SIMPLE_ROOT_LOCATION);
    }
    
    private Map<String,Boolean> setFeatureMap;
    private Map<String,Object> setPropertyMap;
    private String languageOrSchema;
    @Before
    public void setup(){
        languageOrSchema=null;
        setFeatureMap=null;
        setPropertyMap=null;
    }
    
    public SubordinateFeaturesAndPropertyTest() {
    }
    

    @Test
    public void testSettingValidSubordinateFeatureForXSDLanguage(){
        try{
            languageOrSchema = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            setFeatureMap = new HashMap<String, Boolean>();
            setFeatureMap.put(XMLConstants.FEATURE_SECURE_PROCESSING,false);
            Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } 
    }

    @Test
    public void testSettingInvalidSubordinatePropertyForXSDLanguage(){
        try{
            languageOrSchema = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            setPropertyMap = new HashMap<String, Object>();
            setPropertyMap.put(ValidationConstants.SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME, "phase");
            Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXNotSupportedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } catch (SAXNotRecognizedException ex) {
            //should throw this exception
        } catch(SAXException ex){
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void testSettingValidSubordinateFeatureForSchema(){
        try{
            languageOrSchema=SIMPLE_ROOT_SYSTEM_ID;
            setFeatureMap = new HashMap<String, Boolean>();
            setFeatureMap.put(XMLConstants.FEATURE_SECURE_PROCESSING,false);
            Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } 
    }

    @Test
    public void testSettingInvalidSubordinatePropertyForSchema(){
        try{
            languageOrSchema=SIMPLE_ROOT_SYSTEM_ID;
            setPropertyMap = new HashMap<String, Object>();
            setPropertyMap.put(ValidationConstants.SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME, "phase");
            Collection<SAXParseException> faults=performSAXValidatorHandlerTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXNotSupportedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } catch (SAXNotRecognizedException ex) {
            //should throw this exception
        } catch(SAXException ex){
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }
    
    
    @Test
    public void testSettingValidSubordinateFeatureUsingValidator(){
        try{
            languageOrSchema=XMLConstants.W3C_XML_SCHEMA_NS_URI;
            setFeatureMap = new HashMap<String, Boolean>();
            setFeatureMap.put(XMLConstants.FEATURE_SECURE_PROCESSING,false);
            Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } 
    }

    @Test
    public void testSettingInvalidSubordinateUsingValidator(){
        try{
            languageOrSchema=XMLConstants.W3C_XML_SCHEMA_NS_URI;
            setPropertyMap = new HashMap<String, Object>();
            setPropertyMap.put(ValidationConstants.SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME, "phase");
            Collection<SAXParseException> faults=performSAXValidatorTest("/xml-model/simpleRoot.xml");
            assertEquals("Should have been no validation errors",0,faults.size());
        } catch (SAXNotSupportedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } catch (SAXNotRecognizedException ex) {
            //should throw this exception
        } catch(SAXException ex){
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }
    
    
    

    @Override
    protected Map<String, String> getResourceMap() {
        return RESOURCE_LOCATIONS;
    }

    @Override
    protected void featureSetupCallback(FeaturePropertyProvider featuresAndProperties) {
        try {
            super.featureSetupCallback(featuresAndProperties);
            Map<String,FeaturePropertyProvider> subordinateFeaturesMap = (Map<String,FeaturePropertyProvider>) featuresAndProperties.getProperty(ValidationConstants.PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES);
            FeaturePropertyProvider xsdHandler = subordinateFeaturesMap.get(languageOrSchema);
            if (setFeatureMap!=null){
                for (Map.Entry<String,Boolean> featureEntry:setFeatureMap.entrySet()){
                    xsdHandler.setFeature(featureEntry.getKey(), featureEntry.getValue());
                }
            }
            if (setPropertyMap!=null){
                for(Map.Entry<String,Object> propertyEntry: setPropertyMap.entrySet()){
                    xsdHandler.setProperty(propertyEntry.getKey(), propertyEntry.getValue());
                }
            }
        } catch (SAXNotRecognizedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        } catch (SAXNotSupportedException ex) {
            ex.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }
    
    
}
