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
package com.componentcorp.xml.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author rlamont
 */
class FeaturePropertyProviderImpl implements FeaturePropertyProviderInternal {
    
    private  final Map<String,Boolean> featureMap=new HashMap<String, Boolean>();
    private  final Map<String,Object> propertyMap=new HashMap<String, Object>();
    private  final Map<String,ReadWriteable> supportedFeatures=new HashMap<String,ReadWriteable>();
    private  final Map<String,ReadWriteable>  supportedProperties=new HashMap<String,ReadWriteable>();
    private final Map<String,ReadWriteable> unmodifiableSupportedFeatures = Collections.unmodifiableMap(supportedFeatures);
    private final Map<String,ReadWriteable> unmodifiableSupportedProperties = Collections.unmodifiableMap(supportedProperties);
    

    public FeaturePropertyProviderImpl() {
    }
    
    public FeaturePropertyProviderImpl(FeaturePropertyProviderInternal copy) {
        if (copy instanceof FeaturePropertyProviderImpl){
            supportedFeatures.putAll(((FeaturePropertyProviderImpl) copy).supportedFeatures);
            supportedProperties.putAll(((FeaturePropertyProviderImpl) copy).supportedProperties);
        }
        else{
            for (String feature:copy.getSupportedFeatures()){
                supportedFeatures.put(feature, copy.getFeatureSupported(feature));
            }
            for (String property:copy.getSupportedProperties()){
                supportedProperties.put(property, copy.getPropertySupported(property));
            }
        }
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
    
    public void addAllowedFeature(String name,ReadWriteable supportedState){
        if (supportedState==ReadWriteable.UNSUPPORTED){
            supportedFeatures.remove(name);
        }
        else{
            supportedFeatures.put(name,supportedState);
        }
    }
    
    public void addAllowedProperty(String name,ReadWriteable supportedState){
        if (supportedState==ReadWriteable.UNSUPPORTED){
            supportedProperties.remove(name);
        }
        else{
            supportedProperties.put(name, supportedState);
        }
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedFeatures.get(name);
        if (rw==null || rw==ReadWriteable.WRITE_ONLY){
            throw new SAXNotRecognizedException();
        }
        Boolean b= featureMap.get(name);
        if (b==null){
            return false;
        }
        return b;
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedProperties.get(name);
        if (rw==null || rw==ReadWriteable.WRITE_ONLY){
            throw new SAXNotRecognizedException();
        }
        return propertyMap.get(name);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedFeatures.get(name);
        if (rw==null || rw==ReadWriteable.READ_ONLY){
            throw new SAXNotRecognizedException();
        }
        featureMap.put(name, value);
    }

    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedProperties.get(name);
        if (rw==null || rw==ReadWriteable.READ_ONLY){
            throw new SAXNotRecognizedException();
        }
        setReadOnlyProperty(name, object);//call this so we can centralised some group handling
    }

    @Override
    public void setReadOnlyFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedFeatures.get(name);
        if (rw==null ){
            throw new SAXNotRecognizedException();
        }
        featureMap.put(name, value);
    }

    @Override
    public void setReadOnlyProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedProperties.get(name);
        if (rw==null ){
            throw new SAXNotRecognizedException();
        }
        if (ValidationConstants.PROPERTY_XML_MODEL_GROUPS.equals(name)&& object!=null){
            Set<String> groupSet=new HashSet<String>();
            if (object instanceof Collection){
                groupSet.addAll((Collection<? extends String>) object);
            }
            else if (object instanceof String){
                groupSet.add((String) object);
            }
            else {
                throw new SAXNotSupportedException("Property "+ValidationConstants.PROPERTY_XML_MODEL_GROUPS+ " only supports strings or collections of strings");
            }
            object = Collections.unmodifiableSet(groupSet);
        }
        propertyMap.put(name, object);
    }

    @Override
    public Object getWriteOnlyProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        ReadWriteable rw=supportedProperties.get(name);
        if (rw==null){
            throw new SAXNotRecognizedException();
        }
        return propertyMap.get(name);
    }

    @Override
    public boolean getWriteOnlyFeature(String name)  throws SAXNotRecognizedException, SAXNotSupportedException{
        ReadWriteable rw=supportedFeatures.get(name);
        if (rw==null ){
            throw new SAXNotRecognizedException();
        }
        Boolean b= featureMap.get(name);
        if (b==null){
            return false;
        }
        return b;
    }
    
    

    public ReadWriteable getPropertySupported(String name) {
        ReadWriteable property = supportedProperties.get(name);
        return property==null?ReadWriteable.UNSUPPORTED:property;
    }

    public ReadWriteable getFeatureSupported(String name) {
        ReadWriteable feature = supportedFeatures.get(name);
        return feature==null?ReadWriteable.UNSUPPORTED:feature;
    }

    public Collection<String> getSupportedProperties() {
        return unmodifiableSupportedProperties.keySet();
    }

    public Collection<String> getSupportedFeatures() {
        return unmodifiableSupportedFeatures.keySet();
    }
    
}
