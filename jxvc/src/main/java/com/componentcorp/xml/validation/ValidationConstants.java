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

import javax.xml.validation.ValidatorHandler;

/**
 * Utility class to contain standard constants, particularly including standard 
 * property and feature identifiers.
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
     * If this feature is set to true, the xml-model processor will ignore the group pseudo-attribute in all xml-model processing instructions.  In other words, 
     * the xml-model processor will validate against all the schemas provided by xml-model processing instructions.  The default value is false for this feature.
     */
    public static final String FEATURE_IGNORE_XML_MODEL_GROUPS="http://com.componentcorp.xml.validator.ValidationConstants/feature/ignore-xml-model-groups";
    
    /**
     * Property which causes xml-model processing to only apply schema marked as belonging to the same named group.  Provide a String or Collection of Strings
     * to indicate active groups.  See...
     */
    public static final String PROPERTY_XML_MODEL_GROUPS="http://com.componentcorp.xml.validator.ValidationConstants/property/xml-model-groups";
    
    
    /**
     * Property on {@link IntrinsicValidator}  which enables namespace support in the underlying SAX processor.  Defaults to 'true'.
     */
    public static final String FEATURE_NAMESPACE_AWARE="http://xml.org/sax/features/namespaces";
    
    /**
     * If no validator is discovered from the document before the first element, then load the validator from the validation factory identified by this 
     * URI.  The default value is {@link javax.xml.XMLConstants#W3C_XML_SCHEMA_NS_URI}.  Can be set to null to disable default validation.
     */
    public static final String PROPERTY_DEFAULT_VALIDATOR="http://com.componentcorp.xml.validator.ValidationConstants/property/xml-model-group";
    
    
    /**
     * When using JAXP techniques to validate a document, any ValidatorHandler will be instantiated in an opaque manner.  This means under normal
     * circumstances it is impossible to access the ValidatorHandler instance or subordinate instances in the case of the IntrinsicValidator.  
     * Many ValidatorHandlers will require further configuration (via {@link ValidatorHandler#setFeature(java.lang.String, boolean)} or 
     * {@link ValidatorHandler#setProperty(java.lang.String, java.lang.Object)} ).
     * 
     * To achieve this, set this write-only property on the IntrinsicSchemaFactory to a class implementing {@link com.componentcorp.xml.validation.base.ValidatorHandlerConstructionCallback}.
     * When an {@link IntrinsicSchema} constructs an {@link IntrinsicValidatorHandler}, it will call this callback passing you a proxy {@link com.componentcorp.xml.validation.base.FeaturePropertyProvider}
     * which enables you to access and modify features and properties of the IntrinsicValidatorHandler.
     * 
     * Note that the callback has thread scope.  i.e. it is stored in a {@link java.lang.ThreadLocal}.  When using multiple {@link javax.xml.validation.Schema}, 
     * {@link javax.xml.parsers.SAXParser} or {@link javax.xml.parsers.DocumentBuilder} objects in the same thread, it can be very 
     * easy to create unexpected behaviors, as the Schemas will share the same Callback.  To avoid problems, set this property just before creating
     * a {@link javax.xml.parsers.SAXParser} or {@link javax.xml.parsers.DocumentBuilder} and do not create another 
     * {@link javax.xml.parsers.SAXParser} or {@link javax.xml.parsers.DocumentBuilder} until the callback has been called.  Typically the callback
     * will be called immediately upon creation of these objects, but it may be that you have to wait until the first parse.
     */
    public static final String PROPERTY_VALIDATOR_HANDLER_CONSTRUCTION_CALLBACK="http://com.componentcorp.xml.validator.ValidationConstants/property/validator-handler-construction-callback";


    /**
     * Read-only property on {@link IntrinsicValidatorHandler} which can be used to retrieve a proxy {@link org.xml.sax.ext.DeclHandler} which wraps the IntrinsicValidatorHandler.
     * 
     * IntrinsicValidatorHandlers implement {@link org.xml.sax.ext.DeclHandler} so they can delegate DTD Structure validation to a DTD Validator.  This proxy can then be 
     * passed to a {@link javax.xml.parsers.SAXParser} (see {@link org.xml.sax.ext.DeclHandler}) to enable the Handler to receive these events.
     * 
     * Note that JAXP style {@link org.xml.sax.ext.DeclHandler}s can not be made to work with DocumentBuilder at this time.
     */
    public static final String PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER="http://com.componentcorp.xml.validator.ValidationConstants/property/validator-as-declaration-handler";
    
    /**
     * This property can be applied to a {@link IntrinsicValidatorHandler} to declare that the handler wraps a user defined {@link org.xml.sax.ext.DeclHandler}.  To use this feature,
     * you must also use the {@link #PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER} property to retrieve a proxy DeclHandler which can then be set on a {@link javax.xml.parsers.SAXParser}.
     * TODO: How does DocumentBuilder work?
     * 
     * Note that this property is the same as that defined in the documentation of {@link org.xml.sax.ext.DeclHandler}.
     * 
     * Note that JAXP style {@link org.xml.sax.ext.DeclHandler}s can not be made to work with DocumentBuilder at this time.
     */
    public static final  String PROPERTY_DECLARATION_HANDLER="http://xml.org/sax/properties/declaration-handler";
    
    public static final String PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES="http://com.componentcorp.xml.validator.ValidationConstants/property/subordinate-features-and-properties";

    public static final String SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME="http://com.componentcorp.xml.validator.ValidationConstants/subordinate/property/subordinate-phase-property-name";
    
    public static final String SUBORDINATE_PROPERTY_PHASE_OVERRIDE="http://com.componentcorp.xml.validator.ValidationConstants/subordinate/property/subordinate-phase-override";

    public static final String PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP="http://com.componentcorp.xml.validator.ValidationConstants/property/mime-type-to-schematypens-map";

    public static final String FEATURE_TREAT_INVALID_SUBORDINATE_FEATURES_AS_ERRORS="http://com.componentcorp.xml.validator.ValidationConstants/feature/treat-invalid-subordinate-features-as-errors";

    public static final String DTD_MIME_TYPE="application/xml-dtd";
    public static final String RELAX_NG_COMPACT_MIME_TYPE="application/relax-ng-compact-syntax";
    public static final String DTD_SCHEMA_TYPE="[dtd]";
    public static final String RELAX_NG_COMPACT_SCHEMA_TYPE="http://www.iana.org/assignments/media-types/application/relax-ng-compact-syntax";
    public static final String SCHEMATRON_SCHEMA_TYPE="http://purl.oclc.org/dsdl/schematron";
    public static final String NVDL_SCHEMA_TYPE="http://purl.oclc.org/dsdl/nvdl/ns/structure/1.0";
}
