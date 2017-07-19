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

package com.componentcorp.xml.validator.external;

import java.io.File;
import java.lang.reflect.AnnotatedType;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
abstract class WrappingSchemaFactory extends SchemaFactory {

    
    private SchemaFactory wrapped=null;
    private ErrorHandler errorHandler=null;
    
    public WrappingSchemaFactory() {
        DynamicClass classNameAnnotation=this.getClass().getAnnotation(DynamicClass.class);
        if (classNameAnnotation!=null){
            try {
                wrapped=(SchemaFactory)this.getClass().forName(classNameAnnotation.className()).newInstance();
            } catch (InstantiationException ex) {
            } catch (IllegalAccessException ex) {
            } catch (ClassNotFoundException ex) {
            }
        }
    }

    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        if (wrapped==null){
            if (errorHandler!=null){
                try {
                    errorHandler.warning(new SAXParseException("No Jing SchemaFactory could be found.  Ensure jing library is in your class path", null));
                } catch (SAXException ex) {
                    Logger.getLogger(WrappingSchemaFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                DynamicClass classNameAnnotation=this.getClass().getAnnotation(DynamicClass.class);
                String className="SchemaFactories";
                if (classNameAnnotation!=null){
                    className=classNameAnnotation.className();
                }
                Logger.getLogger(WrappingSchemaFactory.class.getName()).warning("Jing library is not loaded, Jing "+className+" cannot be instantiated");
                
            }
        }
        return wrapped==null?false:wrapped.isSchemaLanguageSupported(schemaLanguage);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (wrapped!=null){
            wrapped.setErrorHandler(errorHandler);
        }
        this.errorHandler=errorHandler;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        if (wrapped!=null){
            return wrapped.getErrorHandler();
        }
        return this.errorHandler;
    }

    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        if (wrapped!=null){
            wrapped.setResourceResolver(resourceResolver);
        }
    }

    @Override
    public LSResourceResolver getResourceResolver() {
        if (wrapped!=null){
            return wrapped.getResourceResolver();
        }
        return null;
    }

    @Override
    public Schema newSchema(Source[] schemas) throws SAXException {
        if (wrapped!=null){
            return wrapped.newSchema(schemas);
        }
        return null;
    }

    @Override
    public Schema newSchema() throws SAXException {
        if (wrapped!=null){
            return wrapped.newSchema();
        }
        return null;
    }

    @Override
    public Schema newSchema(URL schema) throws SAXException {
        if (wrapped!=null){
            return wrapped.newSchema(schema);
        }
        return null;
    }

    @Override
    public Schema newSchema(File schema) throws SAXException {
        if (wrapped!=null){
            return wrapped.newSchema(schema);
        }
        return null;
    }

    @Override
    public Schema newSchema(Source schema) throws SAXException {
        if (wrapped!=null){
            return wrapped.newSchema(schema);
        }
        return null;
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (wrapped!=null){
            return wrapped.getProperty(name);
        }
        return super.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (wrapped!=null){
            wrapped.setProperty(name, object);
        }
        else{
            super.setProperty(name, object);
        }
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (wrapped!=null){
            wrapped.setFeature(name, value);
        }
        else{
            super.setFeature(name, value);
        }
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (wrapped!=null){
            return wrapped.getFeature(name);
        }
        return super.getFeature(name);
    }
    
    
    
}
