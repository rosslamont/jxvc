/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.dtd;

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import com.componentcorp.xml.validation.base.LifecycleSchemaFactory;
import com.componentcorp.xml.validation.base.ValidationConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author rlamont
 */
public class DTDSchemaFactory extends LifecycleSchemaFactory  implements FeaturePropertyProvider{
    private ErrorHandler errorHandler=null;
    private LSResourceResolver resourceResolver=null;

    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        if (ValidationConstants.DTD_SCHEMA_TYPE==schemaLanguage){
            return true;
        }
        return false;
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

    
    @Override
    protected Schema newSchemaInternal(Source[] schemas) throws SAXException {
        if (schemas==null || schemas.length<1){
            return newSchemaInternal();
        }
        return new DTDSchema(this,schemas);
    }

    @Override
    protected Schema newSchemaInternal() throws SAXException {
        return new DTDSchema(this);
    }

    
}
