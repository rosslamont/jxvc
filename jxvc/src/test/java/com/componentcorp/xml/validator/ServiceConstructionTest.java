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

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import junit.framework.TestCase;
import org.junit.experimental.categories.Category;
import com.componentcorp.xml.validator.other.IntegrationTest;

/**
 *
 * @author rlamont
 */
@Category(IntegrationTest.class)
public class ServiceConstructionTest extends TestCase {
    public static final String RELAXNG_COMPACT_URI = "http://www.iana.org/assignments/media-types/application/relax-ng-compact-syntax";
    public static final String RELAXNG_XML_URI = "http://relaxng.org/ns/structure/1.0";
    
    public ServiceConstructionTest(String testName) {
        super(testName);
    }

    public void testNewCompactSyntaxFactoryFail(){
        try{
            SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_COMPACT_URI);
            fail("Should throw IllegalArgumentException");
        }
        catch(IllegalArgumentException ie){}
    }

    public void testNewXMLSyntaxFactoryFail(){
        try{
            SchemaFactory factory = SchemaFactory.newInstance(RELAXNG_XML_URI);
            fail("Should throw IllegalArgumentException");
        }
        catch(IllegalArgumentException ie){}
    }

    public void testNewXMLSyntaxFactory2Fail(){
        try{
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            fail("Should throw IllegalArgumentException");
        }
        catch(IllegalArgumentException ie){}
    }
    

}
