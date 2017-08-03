/*
 * Copyright 2017 rlamont.
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

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author rlamont
 */
class UncheckedFeaturePropertyProviderImpl implements FeaturePropertyProvider{
    private  final Map<String,Boolean> featureMap=new HashMap<String, Boolean>();
    private  final Map<String,Object> propertyMap=new HashMap<String, Object>();

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        Boolean b= featureMap.get(name);
        if (b==null){
            return false;
        }
        return b;
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return propertyMap.get(name);
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        featureMap.put(name, value);
    }

    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        propertyMap.put(name, object);
    }
    
    Collection<String> getSupportedProperties(){
        return propertyMap.keySet();
    }
    
    Collection<String> getSupportedFeatures()
    {
        return featureMap.keySet();
    }
    public FeaturePropertyProviderInternal.ReadWriteable getPropertySupported(String name) {
        return propertyMap.containsKey(name)?FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE:FeaturePropertyProviderInternal.ReadWriteable.UNSUPPORTED;
        
    }

    public FeaturePropertyProviderInternal.ReadWriteable getFeatureSupported(String name) {
        return featureMap.containsKey(name)?FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE:FeaturePropertyProviderInternal.ReadWriteable.UNSUPPORTED;
    }

}
