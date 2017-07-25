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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

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
    
}
