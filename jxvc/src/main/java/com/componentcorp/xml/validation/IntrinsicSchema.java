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

import com.componentcorp.xml.validation.base.ValidatorHandlerConstructionCallback;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/**
 *
 * @author rlamont
 */
public class IntrinsicSchema extends Schema{
    private final FeaturePropertyProviderInternal featuresAndProperties;
    private final IntrinsicSchemaFactory parent;
    
    IntrinsicSchema(IntrinsicSchemaFactory parent,FeaturePropertyProviderInternal featuresAndProperties){
        this.featuresAndProperties=featuresAndProperties;
        this.featuresAndProperties.addAllowedProperty(ValidationConstants.PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK, FeaturePropertyProviderInternal.ReadWriteable.UNSUPPORTED);
        this.parent=parent;
    }

    @Override
    public Validator newValidator() {
        return new IntrinsicValidator(parent,new FeaturePropertyProviderImpl(featuresAndProperties));
    }

    @Override
    public ValidatorHandler newValidatorHandler() {
        ValidatorHandlerConstructionCallback callback = parent.getValidatorHandlerCallback();
        ValidatorHandler handler= new IntrinsicValidatorHandler(new FeaturePropertyProviderImpl(featuresAndProperties));
        if (callback!=null){
            callback.onConstruction(new ValidatorHandlerFeaturesAndPropertiesProxy(handler));
        }
        return handler;
    }
    
}
