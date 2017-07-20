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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author rlamont
 */
class FeaturePropertyProviderImpl implements FeaturePropertyProviderInternal{
    private  final Map<String,Boolean> featureMap=new HashMap<String, Boolean>();
    private  final Map<String,Object> propertyMap=new HashMap<String, Object>();
    private  final Set<String> supportedFeatures=new HashSet<String>();
    private  final Set<String> supportedProperties=new HashSet<String>();
    private final Set<String> unmodifiableSupportedFeatures = Collections.unmodifiableSet(supportedFeatures);
    private final Set<String> unmodifiableSupportedProperties = Collections.unmodifiableSet(supportedProperties);
    

    public FeaturePropertyProviderImpl() {
    }
    
    public FeaturePropertyProviderImpl(FeaturePropertyProviderInternal copy) {
        supportedFeatures.addAll(copy.getSupportedFeatures());
        supportedProperties.addAll(copy.getSupportedProperties());
        for (String feature:copy.getSupportedFeatures()){
            try{
                featureMap.put(feature, copy.getFeature(feature));
            }
            catch (SAXException ignore){}
        }
        for (String property:copy.getSupportedProperties()){
            try{
                propertyMap.put(property, copy.getProperty(property));
            }
            catch (SAXException ignore){}
        }
    }
    
    public void addAllowedFeature(String name){
        supportedFeatures.add(name);
    }
    
    public void addAllowedProperty(String name){
        supportedProperties.add(name);
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!supportedFeatures.contains(name)){
            throw new SAXNotRecognizedException();
        }
        Boolean b= featureMap.get(name);
        if (b==null){
            return false;
        }
        return b;
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!supportedProperties.contains(name)){
            throw new SAXNotRecognizedException();
        }
        return propertyMap.get(name);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!supportedFeatures.contains(name)){
            throw new SAXNotRecognizedException();
        }
        featureMap.put(name, value);
    }

    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!supportedProperties.contains(name)){
            throw new SAXNotRecognizedException();
        }
        propertyMap.put(name, object);
    }

    public boolean isPropertySupported(String name) {
        return supportedProperties.contains(name);
    }

    public boolean isFeatureSupported(String name) {
        return supportedFeatures.contains(name);
    }

    public Collection<String> getSupportedProperties() {
        return unmodifiableSupportedProperties;
    }

    public Collection<String> getSupportedFeatures() {
        return unmodifiableSupportedFeatures;
    }
    
}
