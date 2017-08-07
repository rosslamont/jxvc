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

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.EntityResolver2;

/**
 *
 * @author rlamont
 */
public class IntrinsicValidator extends Validator implements FeaturePropertyProvider{
    
    
    private SAXParseException firstFatalError=null;
    private ErrorHandler errorHandler;
    private LSResourceResolver resourceResolver;
    private final FeaturePropertyProviderInternal featuresAndProperties;
    private final IntrinsicSchemaFactory parent;
    private final IntrinsicValidatorHandler validatorHandler;
    private final FakeIntrinsicSchema schema = new FakeIntrinsicSchema();

    IntrinsicValidator(IntrinsicSchemaFactory parent,FeaturePropertyProviderInternal featuresAndProperties) {
        this.featuresAndProperties=featuresAndProperties;
        this.featuresAndProperties.addAllowedFeature(ValidationConstants.FEATURE_NAMESPACE_AWARE,FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE);
        try {this.featuresAndProperties.setFeature(ValidationConstants.FEATURE_NAMESPACE_AWARE, true);} catch (SAXException ignore){}
        featuresAndProperties.addAllowedProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, FeaturePropertyProviderInternal.ReadWriteable.UNSUPPORTED);
        try{featuresAndProperties.setReadOnlyProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, null);} catch(SAXException ignore){}
        this.parent=parent;
        validatorHandler=new IntrinsicValidatorHandler(featuresAndProperties);
    }

    @Override
    public void reset() {
        firstFatalError=null;
    }

    @Override
    public void validate(Source source, Result result) throws SAXException, IOException {
        try {
            firstFatalError=null;
            if (source ==null){
                throw new NullPointerException("Null source is not permitted");
            }
            if (result!=null){
                if ((source instanceof StreamSource && !(result instanceof StreamResult)) ||
                        (source instanceof SAXSource && !(result instanceof SAXResult)) ||
                        (source instanceof DOMSource && !(result instanceof DOMResult)) ||
                        (source instanceof StAXSource && ! (result instanceof StAXResult)))
                {
                    throw new IllegalArgumentException("source and result types do not match.  See javax.xml.validation.Validator specification for more details");
                }
            }
            InputSource inputSource=null;
            if (source instanceof SAXSource){
                SAXSource saxSource = (SAXSource) source;
                inputSource=saxSource.getInputSource();
            }
            else if (source instanceof StreamSource){
                StreamSource streamSource=(StreamSource) source;
                inputSource=new InputSource(streamSource.getInputStream());
                inputSource.setSystemId(source.getSystemId());
                inputSource.setPublicId(streamSource.getPublicId());
            }
            else if (source instanceof StAXSource){
                throw new UnsupportedOperationException("IntrinsicValidator does not currently support "+source.getClass().getName()+ " source.");
            }
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(featuresAndProperties.getFeature(ValidationConstants.FEATURE_NAMESPACE_AWARE));
            
            
            parserFactory.setSchema(schema);
            //ValidatorHandler intrinsicValidator = schema.newValidatorHandler();
            SAXParser parser = parserFactory.newSAXParser();
            
            
            XMLReader xmlReader = parser.getXMLReader();
//            xmlReader.setContentHandler(intrinsicValidator);
//            xmlReader.setDTDHandler(intrinsicValidator);
            xmlReader.setEntityResolver(new LSResourceResolverWrapper(resourceResolver));
            xmlReader.setErrorHandler(new MonitoringErrorHandler(errorHandler));
            xmlReader.parse(inputSource);
            if (firstFatalError!=null){
                throw firstFatalError;
            }
        } catch (ParserConfigurationException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler=errorHandler;
//        intrinsicValidator.setErrorHandler(errorHandler);
    }

    @Override
    public ErrorHandler getErrorHandler() {
        
        return errorHandler;
    }

    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        this.resourceResolver=resourceResolver;
//        intrinsicValidator.setResourceResolver(resourceResolver);
    }

    @Override
    public LSResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        featuresAndProperties.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return featuresAndProperties.getFeature(name);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        //we share features with our validator handler, but we reject certain things not appropriate for a simple
        //validator
        filterUnwantedProperties(name);
        return featuresAndProperties.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        filterUnwantedProperties(name);
        featuresAndProperties.setProperty(name, object);
    }
    
    private void filterUnwantedProperties(String name) throws SAXNotRecognizedException{
        if(ValidationConstants.PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER.equals(name)|| ValidationConstants.PROPERTY_DECLARATION_HANDLER.equals(name)){
            throw new SAXNotRecognizedException(name+" property is not available on IntrinsicValidator");
        }
    }
    
    
    
    private final class LSResourceResolverWrapper implements EntityResolver2{
        private final LSResourceResolver wrapped;

        public LSResourceResolverWrapper(LSResourceResolver wrapped) {
            this.wrapped = wrapped;
        }

        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            return null;
        }

        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
            LSInput input= wrapped.resolveResource("http://www.w3.org/TR/REC-xml", name, publicId, systemId, baseURI);
            if (input !=null){
                InputSource result = new InputSource();
                result.setByteStream(input.getByteStream());
                result.setPublicId(input.getPublicId());
                result.setSystemId(input.getSystemId());
                result.setEncoding(input.getEncoding());
                return result;
            }
            return null;
        }

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            LSInput input= wrapped.resolveResource("http://www.w3.org/TR/REC-xml", null, publicId, systemId, null);
            if (input !=null){
                InputSource result = new InputSource();
                result.setByteStream(input.getByteStream());
                result.setPublicId(input.getPublicId());
                result.setSystemId(input.getSystemId());
                result.setEncoding(input.getEncoding());
                return result;
            }
            return null;
        }
        
        
    }
    
    
    private final class MonitoringErrorHandler implements ErrorHandler{
        final ErrorHandler wrappedHandler;

        public MonitoringErrorHandler(ErrorHandler wrappedHandler) {
            this.wrappedHandler = wrappedHandler;
        }

        public void warning(SAXParseException exception) throws SAXException {
            if (wrappedHandler!=null){
                wrappedHandler.warning(exception);
            }
        }

        public void error(SAXParseException exception) throws SAXException {
            if (wrappedHandler!=null){
                wrappedHandler.error(exception);
            }
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            if (firstFatalError==null){
                firstFatalError = exception;
            }
            if (wrappedHandler!=null){
                wrappedHandler.fatalError(exception);
            }
        }
        
        
    }
    
    private class FakeIntrinsicSchema extends Schema {

        @Override
        public Validator newValidator() {
            throw new UnsupportedOperationException("It should not be possible to call this"); 
        }

        @Override
        public ValidatorHandler newValidatorHandler() {
            return validatorHandler;
        }
        
    }
}
