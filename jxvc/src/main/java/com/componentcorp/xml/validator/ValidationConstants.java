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

/**
 *
 * @author rlamont
 */
public interface ValidationConstants {
    
    public static final String INTRINSIC_NS_URI="http://componentcorp.com/xml/ns/xml-model/1.0";
    
    public static  enum ConflictResolution{
        MODEL_ONLY,
        MODEL_FIRST,
        MODEL_FIRST_IGNORE_XSD_CONFLICT,
        XSI_FIRST,
        XSI_FIRST_IGNORE_XSD_CONFLICT,
        XSI_ONLY
    } ;
    public static final String PROPERTY_CONFLICT_RESOLUTION="http://com.componentcorp.xml.validator.ValidationConstants/property/conflict-resolution";
    
    
    /**
     * Feature which defines what should happen if multiple validation sources are found and no validator implementation is available for some (but not all) libs.
     */
    public static final String FEATURE_IGNORE_MISSING_VALIDATION_LIB="http://com.componentcorp.xml.validator.ValidationConstants/feature/ignore-missing-validation-lib";
    
    
    /**
     * Property identifying a {@link java.util.Set} of validators to disable. Validators are identified by their unique namespace URI as setout in {@link javax.xml.validation.SchemaFactory}.
     * Any such identified validator will not partake in the validation process.  For the purposes of {@link #FEATURE_IGNORE_MISSING_VALIDATION_LIB} the validator will be considered \
     * as available but silently accepting the document as valid.
     */
    public static final String PROPERTY_VALIDATION_DISABLED="http://com.componentcorp.xml.validator.ValidationConstants/property/validation-disabled";
    
    /**
     * Property which causes xml-model processing to only apply schema marked as belonging to the same named group.  Provide a String or Collection of Strings
     * to indicate active groups.  See...
     */
    public static final String PROPERTY_XML_MODEL_GROUPS="http://com.componentcorp.xml.validator.ValidationConstants/feature/xml-model-groups";
    
    
    /**
     * Property on {@link IntrinsicValidator}  which enables namespace support in the underlying SAX processor.  Defaults to 'true'.
     */
    public static final String FEATURE_NAMESPACE_AWARE="http://xml.org/sax/features/namespaces";
    
    /**
     * If no validator is discovered from the document before the first element, then load the validator from the validation factory identified by this 
     * URI.  The default value is {@link javax.xml.XMLConstants#W3C_XML_SCHEMA_NS_URI}.  Can be set to null to disable default validation.
     */
    public static final String PROPERTY_DEFAULT_VALIDATOR="http://com.componentcorp.xml.validator.ValidationConstants/feature/xml-model-group";
}
