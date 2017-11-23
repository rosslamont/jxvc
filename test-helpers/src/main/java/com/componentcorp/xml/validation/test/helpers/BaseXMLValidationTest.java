/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.test.helpers;

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import com.componentcorp.xml.validation.base.ValidatorFeaturePropertyProvider;
import com.componentcorp.xml.validation.base.ValidatorHandlerConstructionCallback;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.Assert;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public abstract class BaseXMLValidationTest extends Assert{

    private static final Map<String,Boolean> EMPTY_FEATURES = Collections.emptyMap();
    private static final Map<String,Object> EMPTY_PROPERTIES = Collections.emptyMap();
        
    public static final String INTRINSIC_NS_URI="http://componentcorp.com/xml/ns/xml-model/1.0";
    
    
    
    protected Collection<SAXParseException> performDOMValidatorTest(String testFile) throws SAXNotSupportedException, SAXNotRecognizedException {
        return performDOMValidatorTest(testFile, EMPTY_FEATURES, EMPTY_PROPERTIES,null);
    }    
    
    protected Collection<SAXParseException> performDOMValidatorTest(String testFile,Map<String,Boolean> features,Map<String,Object> properties) throws SAXNotSupportedException, SAXNotRecognizedException {
        return performDOMValidatorTest(testFile, features, properties,null);
        
    }
    
    protected Collection<SAXParseException> performDOMValidatorTest(String testFile,DOMValidatorHandlerPreExecutionCallback callback) throws SAXNotSupportedException, SAXNotRecognizedException {
        return performDOMValidatorTest(testFile, EMPTY_FEATURES, EMPTY_PROPERTIES,callback);
    }
    protected Collection<SAXParseException> performDOMValidatorTest(String testFile,Map<String,Boolean> features,Map<String,Object> properties,DOMValidatorHandlerPreExecutionCallback callback) throws SAXNotSupportedException, SAXNotRecognizedException {
        final TestContentHandler contentHandler = new TestContentHandler(getResourceMap());
        SchemaFactory schemaFactory = SchemaFactory.newInstance(INTRINSIC_NS_URI);
        schemaFactoryCreationCallback(schemaFactory);
        applyFeaturesAndProperties(schemaFactory, features, properties);
        if (schemaFactory == null) {
            fail("Should have found the intrinsic factory");
        }
        try {
            ValidatorHandlerCallback validatorHandlerCallback = new ValidatorHandlerCallback();
            schemaFactory.setProperty("http://com.componentcorp.xml.validator.ValidationConstants/property/validator-handler-construction-callback", validatorHandlerCallback);
            Schema schema = schemaFactory.newSchema();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            docFactory.setSchema(schema);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            InputStream is = getClass().getResourceAsStream(testFile);
            docBuilder.setEntityResolver(contentHandler);
            docBuilder.setErrorHandler(contentHandler);
            if (callback!=null){
                callback.preExecute(docBuilder, validatorHandlerCallback.validatorHandlerProxy);
            }
            docBuilder.parse(is);
        } catch (SAXException ex) {
            fail("Should not have thrown an SAXException creating schema");
        } catch (IOException io) {
            fail("Should not have thrown an IOException creating schema");
        } catch (ParserConfigurationException ex) {
            fail("Should not have thrown an ParserConfigurationException creating schema");
        }
        return contentHandler.getFaults();
    }

    protected Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile) throws SAXException {
        return this.performSAXValidatorHandlerTest(testFile,  EMPTY_FEATURES, EMPTY_PROPERTIES,null);
    
    }
    
    protected Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile,Map<String,Boolean> features, Map<String,Object> properties) throws SAXException {
        return this.performSAXValidatorHandlerTest(testFile,  features, properties,null);
    
    }
    
    protected Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile,SAXValidatorHandlerPreExecutionCallback callback) throws SAXException {
        return this.performSAXValidatorHandlerTest(testFile,  EMPTY_FEATURES, EMPTY_PROPERTIES,callback);
    
    }
    
    protected void applyFeaturesAndProperties(SchemaFactory schemaFactory,Map<String,Boolean> features, Map<String,Object> properties) throws SAXNotSupportedException, SAXNotRecognizedException{
        
        for (Map.Entry<String,Boolean> feature:features.entrySet()){
            Boolean b = feature.getValue();
            if (b==null){
                b=Boolean.FALSE;
            }
            schemaFactory.setFeature(feature.getKey(), b);
        }
        for (Map.Entry<String,Object> property:properties.entrySet()){
            schemaFactory.setProperty(property.getKey(), property.getValue());
        }
    }
    
    protected Collection<SAXParseException> performSAXValidatorHandlerTest(String testFile,Map<String,Boolean> features, Map<String,Object> properties,SAXValidatorHandlerPreExecutionCallback callback) throws SAXException {
        TestContentHandler contentHandler = new TestContentHandler(getResourceMap());
        SchemaFactory schemaFactory = SchemaFactory.newInstance(INTRINSIC_NS_URI);
        schemaFactoryCreationCallback(schemaFactory);
        applyFeaturesAndProperties(schemaFactory, features, properties);
        if (schemaFactory == null) {
            fail("Should have found the intrinsic factory");
        }
        try {
            ValidatorHandlerCallback validatorHandlerCallback=new ValidatorHandlerCallback();
            schemaFactory.setProperty("http://com.componentcorp.xml.validator.ValidationConstants/property/validator-handler-construction-callback", validatorHandlerCallback);
            Schema schema = schemaFactory.newSchema();
            //ValidatorHandler handler =schema.newValidatorHandler();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setSchema(schema);
            SAXParser parser = parserFactory.newSAXParser();
//            
//            parser.getXMLReader().setProperty("http://xml.org/sax/properties/declaration-handler", contentHandler);
            InputStream is = getClass().getResourceAsStream(testFile);
            InputSource source = new InputSource(is);
            source.setSystemId(getClass().getResource(testFile).toString());
            
            if (callback!=null){
                callback.preExecute(parser, validatorHandlerCallback.validatorHandlerProxy);
            }
            parser.parse(source, contentHandler);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            fail("Should not have thrown an ParserConfigurationException creating schema");
        } catch (IOException io) {
            io.printStackTrace();
            fail("Should not have thrown an IOException creating schema");
        }
        return contentHandler.getFaults();
    }

    protected Collection<SAXParseException> performSAXValidatorTest(String testFile) throws SAXException {
        return performSAXValidatorTest(testFile, null);
    }
    
    protected Collection<SAXParseException> performSAXValidatorTest(String testFile,ValidatorPreExecutionCallback callback) throws SAXException {
        final TestContentHandler contentHandler = new TestContentHandler(getResourceMap());
        SchemaFactory schemaFactory = SchemaFactory.newInstance(INTRINSIC_NS_URI);
        schemaFactoryCreationCallback(schemaFactory);
        if (schemaFactory == null) {
            fail("Should have found the intrinsic factory");
        }
        try {
            Schema schema = schemaFactory.newSchema();
            //ValidatorHandler handler =schema.newValidatorHandler();
            final Validator validator = schema.newValidator();
            //validator.setFeature(ValidationConstants.FEATURE_NAMESPACE_AWARE, true); //just test the feature is working even though its default
            validator.setErrorHandler(contentHandler);
            validator.setResourceResolver(contentHandler);
            InputStream is = getClass().getResourceAsStream(testFile);
            Source source = new StreamSource(is);
            Result result = new StreamResult();
            if (callback!=null){
                callback.preExecute(validator,new ValidatorFeaturePropertyProvider(validator));
            }
            validator.validate(source, result);
        } catch (IOException io) {
            fail("Should not have thrown an IOException creating schema");
        }
        return contentHandler.getFaults();
    }
    
    abstract protected Map<String,String> getResourceMap();
    
    protected void schemaFactoryCreationCallback(SchemaFactory factory){
        
    }

    private class ValidatorHandlerCallback implements ValidatorHandlerConstructionCallback{
        private FeaturePropertyProvider validatorHandlerProxy;

        @Override
        public void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy) {
            this.validatorHandlerProxy=instrinsicValidatorHandlerProxy;
        }
        
    }
    
    protected interface ValidatorPreExecutionCallback{
        void preExecute(Validator validator,FeaturePropertyProvider validatorHandlerFAndP);
    }
    
    protected interface SAXValidatorHandlerPreExecutionCallback{
        void preExecute(SAXParser parser, FeaturePropertyProvider validatorHandlerFAndP);
    }

    protected interface DOMValidatorHandlerPreExecutionCallback{
        void preExecute(DocumentBuilder parser, FeaturePropertyProvider validatorHandlerFAndP);
    }
    
}
