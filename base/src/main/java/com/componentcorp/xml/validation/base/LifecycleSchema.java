/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * A {@link Schema} which provides Lifecycle events, especially notification
 * of construction of {@link ValidatorHandler}.  See {@link ValidationConstants#PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK}
 * for more details.
 * 
 * @author rlamont
 */
public abstract class LifecycleSchema extends Schema{
    
    private final LifecycleSchemaFactory parent;

    public LifecycleSchema(LifecycleSchemaFactory parent) {
        this.parent = parent;
    }
    
    

    /**
     * <p>This method has been made final because LifecycleSchema needs to 
     * intercept the creation of {@link ValidatorHandler} objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newValidatorHandlerInternal()} </p>
     * 
     */
    @Override
    public final ValidatorHandler newValidatorHandler() {
        ValidatorHandlerConstructionCallback callback = parent.getValidatorHandlerCallback();
        ValidatorHandler handler= newValidatorHandlerInternal();
        if (callback!=null){
            callback.onConstruction(newFeaturePropertyProxy(handler));
        }
        return handler;
    }
    
    /**
     * Implement this method instead of {@link #newValidatorHandler() }
     */
    protected abstract ValidatorHandler newValidatorHandlerInternal();
    
    
    /**
     * Override this method if you want to implement your own {@link FeaturePropertyProvider} to proxy
     * your ValidatorHandler.
     * 
     * For safety it is good practice to obscure the ValidatorHandler behind a FeaturePropertyProvider
     * 
     * @param handler the newly constructed handler.
     * @return a FeaturePropertyProvider which is typically a proxy to your ValidatorHandler.  
     */
    protected FeaturePropertyProvider newFeaturePropertyProxy(final ValidatorHandler handler){
        return new FeaturePropertyProvider() {
            @Override
            public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
                return handler.getFeature(name);
            }

            @Override
            public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
                return handler.getProperty(name);
            }

            @Override
            public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
                handler.setFeature(name, value);
            }

            @Override
            public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
                handler.setProperty(name, object);
            }
        };
    }
    
}
