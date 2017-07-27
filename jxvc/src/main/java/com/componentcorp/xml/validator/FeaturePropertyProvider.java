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
package com.componentcorp.xml.validator;

import java.util.Collection;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author rlamont
 */
public interface FeaturePropertyProvider {

    boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException;

    <T> T getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException;
    
}

interface FeaturePropertyProviderInternal extends FeaturePropertyProvider{

    public enum ReadWriteable{
        UNSUPPORTED,
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE
    } ;
    
    ReadWriteable getPropertySupported(String name);
    ReadWriteable getFeatureSupported(String name);
    Collection<String> getSupportedProperties();
    Collection<String> getSupportedFeatures();

    void addAllowedFeature(String name,ReadWriteable supportedState);

    void addAllowedProperty(String name,ReadWriteable supportedState);
    
    void setReadOnlyFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setReadOnlyProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException;
    
    <T> T getWriteOnlyProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException;
    
    boolean getWriteOnlyFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException;
}
