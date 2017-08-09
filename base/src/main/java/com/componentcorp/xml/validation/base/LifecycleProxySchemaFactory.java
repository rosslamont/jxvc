/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * <p>A {@link SchemaFactory} which allows you to wrap another SchemaFactory
 * and generate lifecycle events for that factory.  This can be particularly 
 * useful of you want to set features and properties on a ValidatorHandler
 * during SAX or DOM parsing using the standard JAXP techniques.</p>
 * 
 * @see ValidationConstants#PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK
 * 
 * @author rlamont
 */
public class LifecycleProxySchemaFactory extends LifecycleSchemaFactory{
    
    private final SchemaFactory wrapped;

    public LifecycleProxySchemaFactory(SchemaFactory proxy) {
        this.wrapped = proxy;
    }
    
    

    @Override
    protected Schema newSchemaInternal(Source[] schemas) throws SAXException {
        return wrapped.newSchema(schemas);
    }

    @Override
    protected Schema newSchemaInternal() throws SAXException {
        return wrapped.newSchema();
    }

    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        return wrapped.isSchemaLanguageSupported(schemaLanguage);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        wrapped.setErrorHandler(errorHandler);
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return wrapped.getErrorHandler();
    }

    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        wrapped.setResourceResolver(resourceResolver);
    }

    @Override
    public LSResourceResolver getResourceResolver() {
        return wrapped.getResourceResolver();
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK.equals(name)){
            super.setProperty(name, object);
        }
        else{
           wrapped.setProperty(name, object);
        }
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return wrapped.getProperty(name); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        wrapped.setFeature(name, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return wrapped.getFeature(name); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
