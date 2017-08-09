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
package com.componentcorp.xml.validation.base;

import java.util.Collection;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Abstraction of feature and property capabilities found in {@link SchemaFactory},
 * {@link Validator} and {@link ValidatorHandler}.  
 * @author rlamont
 */
public interface FeaturePropertyProvider {

    /**
     * @see SchemaFactory#getFeature(java.lang.String) 
     */
    boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException;

    /**
     * @see SchemaFactory#getProperty(java.lang.String) 
     */
    Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException;

    /**
     * @see SchemaFactory#setFeature(java.lang.String, boolean) 
     */
    void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException;

    /**
     * @see SchemaFactory#setProperty(java.lang.String, java.lang.Object) 
     */
    void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException;
    
}
