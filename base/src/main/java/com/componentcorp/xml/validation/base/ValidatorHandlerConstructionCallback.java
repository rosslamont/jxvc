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

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

/**
 * Callback interface to be notified upon construction of a ValidatorHandler.
 * 
 * As construction of ValidatorHandlers during SAX and DOM parsing is opaque, 
 * it becomes impossible to set features and properties on those ValidatorHandlers.
 * 
 * {@link SchemaFactory} providers are encouraged to provide a means of setting 
 * this callback (typically as a property).  The callback is typically called by
 * a {@link Schema} after it has instantiated a {@link ValidatorHandler}.
 * @author rlamont
 */
public interface ValidatorHandlerConstructionCallback {
    
    void onConstruction(FeaturePropertyProvider instrinsicValidatorHandlerProxy);
}
