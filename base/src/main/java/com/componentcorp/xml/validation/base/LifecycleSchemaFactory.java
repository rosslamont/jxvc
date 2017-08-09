/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import java.io.File;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * A {@link SchemaFactory} which provides Lifecycle events, particularly support for notification
 * of construction of {@link ValidatorHandler}.  See {@link ValidationConstants#PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK}
 * for more details.
 * 
 * @author rlamont
 */
public abstract class LifecycleSchemaFactory extends SchemaFactory implements FeaturePropertyProvider{
    
    private final ThreadLocal<ValidatorHandlerConstructionCallback> validatorHandlerConstructionCallback=new ThreadLocal<ValidatorHandlerConstructionCallback>();
    
    
    /**
     * This method has been made final because LifecycleSchemaFactory needs to 
     * intercept the creation of Schema objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newSchemaInternal(javax.xml.transform.Source[]) }
     * 
     */
    @Override
    public final Schema newSchema(Source[] schemas) throws SAXException {
        return wrapSchemaIfNecessary(newSchemaInternal(schemas));
    }

    /**
     * This method has been made final because LifecycleSchemaFactory needs to 
     * intercept the creation of Schema objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newSchemaInternal() }
     * 
     */
    @Override
    public final Schema newSchema() throws SAXException {
        return wrapSchemaIfNecessary(newSchemaInternal());
    }

    /**
     * This method has been made final because LifecycleSchemaFactory needs to 
     * intercept the creation of Schema objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newSchemaInternal(java.net.URL)  }
     * 
     */
    @Override
    public final Schema newSchema(URL schema) throws SAXException {
        return wrapSchemaIfNecessary(newSchemaInternal(schema));
    }

    /**
     * This method has been made final because LifecycleSchemaFactory needs to 
     * intercept the creation of Schema objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newSchemaInternal(java.io.File) ) }
     * 
     */
    @Override
    public final Schema newSchema(File schema) throws SAXException {
        return wrapSchemaIfNecessary(newSchemaInternal(schema));
    }

    /**
     * This method has been made final because LifecycleSchemaFactory needs to 
     * intercept the creation of Schema objects so as to be able to generate 
     * lifecycle events.  Instead, you should implement {@link #newSchemaInternal(javax.xml.transform.Source) ) }
     * 
     */
    @Override
    public final Schema newSchema(Source schema) throws SAXException {
        return wrapSchemaIfNecessary(newSchemaInternal(schema));
    }
    
    /**
     * Override this method to instantiate a {@link Schema} object from {@link Source[]}
     * objects.  It is semantically identical to {@link SchemaFactory#newSchema(javax.xml.transform.Source[]) }.
     * See those methods for parameter, return and throws documentation.
     */
    protected abstract Schema newSchemaInternal(Source[] schemas) throws SAXException ;

    /**
     * Override this method to instantiate a special {@link Schema} object.  It is 
     * semantically identical to {@link SchemaFactory#newSchema() }
     * See those methods for parameter, return and throws documentation.
     */
    protected abstract Schema newSchemaInternal() throws SAXException ;

    
   
    /**
     * <p>This is a convenience method for {@link #newSchemaInternal(javax.xml.transform.Source[]) }.
     * It can be safely overridden if desired in the same way that 
     * {@link SchemaFactory#newSchema(java.net.URL)} can be.</p>
     * See those methods for parameter, return and throws documentation.
     */
    protected  Schema newSchemaInternal(URL schema) throws SAXException {
        return newSchemaInternal(new StreamSource(schema.toExternalForm()));
    }

    /**
     * <p>This is a convenience method for {@link #newSchemaInternal(File schema)}.
     * It can be safely overridden if desired in the same way that 
     * {@link SchemaFactory#newSchema(java.io.File) } can be.</p>
     */
    protected  Schema newSchemaInternal(File schema) throws SAXException {
        return newSchemaInternal(new StreamSource(schema));
    }

    /**
     * <p>This is a convenience method for {@link #newSchemaInternal(javax.xml.transform.Source) }.
     * It can be safely overridden if desired in the same way that 
     * {@link SchemaFactory#newSchema(javax.xml.transform.Source) }can be.</p>
     */
    protected  Schema newSchemaInternal(Source schema) throws SAXException {
        return newSchemaInternal(new Source[]{schema});
    }
    
    

    
    ValidatorHandlerConstructionCallback getValidatorHandlerCallback() {
        if (validatorHandlerConstructionCallback!=null){
            return validatorHandlerConstructionCallback.get();
        }
        return null;
    }

    
    /**
     * This class supports the {@link ValidationConstants#PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK}
     * write-only property.  It will throw an exception for any other property,
     * as documented in {@link SchemaFactory#setProperty(java.lang.String, java.lang.Object) }
     * @param name {@inheritDoc }
     * @param object {@inheritDoc }
     * @throws SAXNotRecognizedException {@inheritDoc }
     * @throws SAXNotSupportedException  {@inheritDoc }
     */
    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK.equals(name)){
            try{
                ValidatorHandlerConstructionCallback callback = (ValidatorHandlerConstructionCallback) object;
                validatorHandlerConstructionCallback.set(callback);
                return ;
            }
            catch (ClassCastException cce){
                SAXNotSupportedException snse = new SAXNotSupportedException(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK+" property must be an instance of ValidatorHandlerConstructionCallback");
                snse.initCause(snse);
                throw snse;
            }
        }
        else if (name==null){
            throw new NullPointerException();
        }
        throw new SAXNotRecognizedException(name);
    }

    private Schema wrapSchemaIfNecessary(Schema schema) {
        if (schema instanceof LifecycleSchema){
            return schema;
        }
        return new LifecycleProxySchema(schema, this);
    }
    
    
    
}
