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

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * A SchemaFactory which creates a Proxy Schema which is able to determine
 * the actual schema from information intrinsic to the XML Document. In particular,
 * Schemas produced by this schema factory can extract XML Schema instances
 * from XML Schema Instance information embedded in the root tag of a document,
 * or arbitrary schema information designated by an xml-model processing
 * instruction in accordance with ISO/IEC 19757-11.
 * @author rlamont
 */
public class IntrinsicSchemaFactory extends SchemaFactory {

    private ErrorHandler errorHandler=null;
    private LSResourceResolver resourceResolver=null;
    /**
     * This schema factory supports the xml-model pseudo language. The
     * URI for this language can be found at {@link ValidationConstants#INTRINSIC_NS_URI}
     * @param schemaLanguage a schema language to test
     * @return true if the schema language is {@link ValidationConstants#INTRINSIC_NS_URI}
     */
    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        return ValidationConstants.INTRINSIC_NS_URI.equals(schemaLanguage);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler=errorHandler;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }


    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        this.resourceResolver=resourceResolver;
    }

    @Override
    public LSResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    
    /**
     * Creating a schema from known sources does not make sense for this schema.
     * @param schemas
     * @return
     * @throws SAXException 
     */
    @Override
    public Schema newSchema(Source[] schemas) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schema newSchema() throws SAXException {
        return new IntrinsicSchema();
    }
    
}
