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
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * A SchemaFactory which creates a Proxy Schema which is able to determine
 * the actual schema from information intrinsic to the XML Document. In particular,
 * Schemas produced by this schema factory can extract XML Schema instances
 * from XML Schema Instance information embedded in the root tag of a document,
 * or arbitrary schema information designated by an xml-model processing
 * instruction in accordance with ISO/IEC 19757-11.
 * @author rlamont
 */
public class IntrinsicSchemaFactory extends SchemaFactory implements FeaturePropertyProvider {

    private ErrorHandler errorHandler=null;
    private LSResourceResolver resourceResolver=null;
    private final FeaturePropertyProviderInternal featuresAndProperties;

    public IntrinsicSchemaFactory() {
        FeaturePropertyProviderImpl fAndP=new FeaturePropertyProviderImpl();
        fAndP.addAllowedFeature(ValidationConstants.FEATURE_IGNORE_MISSING_VALIDATION_LIB,FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE);
        try{fAndP.setFeature(ValidationConstants.FEATURE_IGNORE_MISSING_VALIDATION_LIB,false);} catch (SAXException ignore){}
        fAndP.addAllowedProperty(ValidationConstants.PROPERTY_DEFAULT_VALIDATOR,FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE);
        try{fAndP.setProperty(ValidationConstants.PROPERTY_DEFAULT_VALIDATOR, XMLConstants.W3C_XML_SCHEMA_NS_URI);}catch(SAXException ignore){}
        //fAndP.addAllowedProperty(ValidationConstants.PROPERTY_VALIDATION_DISABLED);
        //fAndP.addAllowedProperty(ValidationConstants.PROPERTY_XML_MODEL_GROUPS);
        fAndP.addAllowedProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, FeaturePropertyProviderInternal.ReadWriteable.WRITE_ONLY);
        featuresAndProperties=fAndP;
    }
    
    
    
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
     * @throws UnsupportedOperationException always 
     */
    @Override
    public Schema newSchema(Source[] schemas) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schema newSchema() throws SAXException {
        return new IntrinsicSchema(this,new FeaturePropertyProviderImpl(featuresAndProperties));
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return featuresAndProperties.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK.equals(name)){
            try{
                ValidatorHandlerConstructionCallback callback = (ValidatorHandlerConstructionCallback) object;
                ThreadLocal<ValidatorHandlerConstructionCallback> threadLocal= featuresAndProperties.getWriteOnlyProperty(name);
                if (threadLocal==null){
                    threadLocal = new ThreadLocal<ValidatorHandlerConstructionCallback>();
                    featuresAndProperties.setProperty(name, threadLocal);
                }
                threadLocal.set(callback);
                return ;
            }
            catch (ClassCastException cce){
                SAXNotSupportedException snse = new SAXNotSupportedException(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK+" property must be an instance of ValidatorHandlerConstructionCallback");
                snse.initCause(snse);
                throw snse;
            }
        }
        featuresAndProperties.setProperty(name, object);
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        featuresAndProperties.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return featuresAndProperties.getFeature(name);
    }


    ValidatorHandlerConstructionCallback getValidatorHandlerCallback() {
        try {
            ThreadLocal<ValidatorHandlerConstructionCallback> threadLocal= featuresAndProperties.getWriteOnlyProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK);
            if (threadLocal!=null){
                return threadLocal.get();
            }
        } catch (SAXNotRecognizedException ex) {
        } catch (SAXNotSupportedException ex) {
        }
        return null;
    }
    
    
    
}
