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

import com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public class XMLSchemaValidationTest extends BaseXMLValidationTest{
    
    public static final String SIMPLE_ROOT_SYSTEM_ID="http://componentcorp/schema/jxvc/test/simpleRootSchema.xsd";
    public static final String SIMPLE_ROOT_LOCATION="/schema/simpleRootSchema.xsd";
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    static{
        RESOURCE_LOCATIONS.put(SIMPLE_ROOT_SYSTEM_ID, SIMPLE_ROOT_LOCATION);
    }
    
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

    @Override
    protected Map<String, String> getResourceMap() {
        return RESOURCE_LOCATIONS;
    }
    




}
