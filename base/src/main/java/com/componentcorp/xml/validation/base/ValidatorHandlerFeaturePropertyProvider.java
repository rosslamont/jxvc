/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Simple wrapper around a ValidatorHandler to provide the FeaturePropertyProvider interface
 * to existing ValidatorHandlers
 * @author rlamont
 */
public class ValidatorHandlerFeaturePropertyProvider implements FeaturePropertyProvider{

    private final ValidatorHandler wrapped;

    public ValidatorHandlerFeaturePropertyProvider(ValidatorHandler wrapped) {
        this.wrapped = wrapped;
    }
    
    
    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return wrapped.getFeature(name);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return wrapped.getProperty(name);
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        wrapped.setFeature(name, value);
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        wrapped.setProperty(name, object);
    }
    
}
