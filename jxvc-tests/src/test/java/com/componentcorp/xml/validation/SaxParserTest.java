/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation;

import com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public class SaxParserTest extends BaseXMLValidationTest {
    
    public static final String SIMPLE_ROOT_SYSTEM_ID="http://componentcorp/schema/jxvc/test/simpletest.rnc";
    public static final String SIMPLE_ROOT_LOCATION="/schema/simpletest.rnc";
    private static final Map<String,String> RESOURCE_LOCATIONS=new HashMap<String, String>();
    static{
        RESOURCE_LOCATIONS.put(SIMPLE_ROOT_SYSTEM_ID, SIMPLE_ROOT_LOCATION);
    }
    
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
    
    @Test
    public void simpleSAXRootRNCSchemaTypeValidator() throws SAXException{
        Collection<SAXParseException> faults=performSAXValidatorTest("/relaxNG/simple_xml-model.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void simpleSAXRootRNCTypeValidator() throws SAXException{
        Collection<SAXParseException> faults=performSAXValidatorTest("/relaxNG/simple_xml-model-2.xml");
        assertEquals("Should have been no validation errors",0,faults.size());
    }
    
    @Test
    public void simpleSAXRootXSDValidatorFail() throws SAXException{
        Collection<SAXParseException> faults=performSAXValidatorTest("/relaxNG/simple_xml-model_fail.xml");
        assertEquals("Should have been 1 validation error",1,faults.size());
        SAXException se = faults.iterator().next();
        assertTrue("Exception thrown should have been a SAX ParseException",se instanceof SAXParseException);
        SAXParseException spe = (SAXParseException) se;
        final int FAIL_LINE_NO=4;
        final int FAIL_COL_NO=24;
        assertEquals("Wrong exception - wrong line no",FAIL_LINE_NO,spe.getLineNumber());
        assertEquals("Wrong exception - wrong column no",FAIL_COL_NO,spe.getColumnNumber());
    }
    

    @Override
    protected Map<String, String> getResourceMap() {
        return RESOURCE_LOCATIONS;
    }
    
    
    
}
