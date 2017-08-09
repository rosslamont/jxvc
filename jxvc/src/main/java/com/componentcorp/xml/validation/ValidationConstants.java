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

import com.componentcorp.xml.validation.base.FeaturePropertyProvider;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Utility class to contain standard constants, particularly standard 
 * property and feature identifiers.
 * @author rlamont
 */
public interface ValidationConstants extends com.componentcorp.xml.validation.base.ValidationConstants{
    
    
    /** 
     * Enumeration allowing you to specify the processing order where DTD, XSI and xml-model elements are simultaneously present.
     * Use this property along with the various features to disable DTD, XSI or xml-model processing.  This is not normally needed,
     * but some validation techniques can augment the xml information set, and this may be relevant for downstream validators.
     */
    public static  enum ProcessingOrder{
        /**
         * Validations will be ordered according to the order discovered in the XML Document
         */
        NATIVE_ORDER,
        /**
         * DTD will be processed first, then XSI derived XML Schema, then xml-model elements.
         */
        DTD_XSI_MODEL,
        /**
         * XSI derived XML Schema will be processed first, then DTD, then xml-model elements.
         */
        XSI_DTD_MODEL,
        /**
         * DTD will be processed first, then xml-model elements, then XSI derived XML Schema.
         */
        DTD_MODEL_XSI,
        /**
         * XSI derived XML Schema will be processed first, then xml-model elements, then DTD.
         */
        XSI_MODEL_DTD,
        /**
         * xml-model elements will be processed first, then DTD, then XSI derived XML Schema.
         */
        MODEL_DTD_XSI,
        /**
         * xml-model elements will be processed first, then XSI derived XML Schema, then DTD.
         */
        MODEL_XSI_DTD
    } ;
    
    /**
     * In some cases different styles of validation can coexist in a document.  This property allows you to specify the order of processing
     * undertaken by the intrinsic property.  This can work hand in hand with features which disable various styles of processing.
     */
    public static final String PROPERTY_PROCESSING_ORDER="http://com.componentcorp.xml.validator.ValidationConstants/property/processing-order";
    
    /**
     * If set true, any xml-model processing instructions will be ignored.
     */
    public static final String FEATURE_DISABLE_XML_MODEL="http://com.componentcorp.xml.validator.ValidationConstants/feature/disable-xml-model-processing";
    
    /**
     * If set true, any xsi (XML Schema Instance) attributes on the root tag of the document will be ignored.  XSD processing may still be invoked through any
     * xsd files identified in xml-model processing instructions.
     */
    public static final String FEATURE_DISABLE_XSI_BASED_XSD="http://com.componentcorp.xml.validator.ValidationConstants/feature/disable-xsi-based-xsd-processing";
    
    /**
     * If set true, DTD validation is not performed.  Note that most SAX parsers will still process DTD declared entities.
     */
    public static final String FEATURE_DISABLE_DTD_STRUCTURE="http://com.componentcorp.xml.validator.ValidationConstants/feature/disable-dtd-structure-processing";
    
    /**
     * Feature which defines what should happen if multiple validation sources are found and no validator implementation is available for some (but not all) libs.
     */
    public static final String FEATURE_IGNORE_MISSING_VALIDATION_LIB="http://com.componentcorp.xml.validator.ValidationConstants/feature/ignore-missing-validation-lib";
    
    
    /**
     * Property identifying a {@link java.util.Set} of validators to disable. Validators are identified by their unique namespace URI as setout in {@link javax.xml.validation.SchemaFactory}.
     * Any such identified validator will not partake in the validation process.  For the purposes of {@link #FEATURE_IGNORE_MISSING_VALIDATION_LIB} the validator will be considered \
     * as available but silently accepting the document as valid.
     */
    public static final String PROPERTY_DISABLED_SUBORDINATE_VALIDATORS="http://com.componentcorp.xml.validator.ValidationConstants/property/disabled-subordinate-validators";
    
    /**
     * If this feature is set to true, the xml-model processor will ignore the group pseudo-attribute in all xml-model processing instructions.  In other words, 
     * the xml-model processor will validate against all the schemas provided by xml-model processing instructions.  The default value is false for this feature.
     */
    public static final String FEATURE_IGNORE_ALL_XML_MODEL_GROUPS="http://com.componentcorp.xml.validator.ValidationConstants/feature/ignore-all-xml-model-groups";
    
    /**
     * Property which causes xml-model processing to only apply schema marked as belonging to the same named group.  Provide a String or Collection of Strings
     * to indicate active groups.  See...
     */
    public static final String PROPERTY_XML_MODEL_GROUPS="http://com.componentcorp.xml.validator.ValidationConstants/property/xml-model-groups";
    
    
    /**
     * If no validator is discovered from the document before the first element, then load the validator from the validation factory identified by this 
     * URI.  The default value is {@link javax.xml.XMLConstants#W3C_XML_SCHEMA_NS_URI}.  Can be set to null to disable default validation.
     */
    public static final String PROPERTY_DEFAULT_VALIDATOR="http://com.componentcorp.xml.validator.ValidationConstants/property/default-validator";
    


    /**
     * This property provides access to features and properties of ValidatorHandlers that are loaded by the IntrinsicValidator or IntrinsicValidatorHandler, so-called 
     * "Subordinate Validators".  The property is read-only, providing you with a read-only Map of {@link FeaturePropertyProvider} proxy objects which can receive features and 
     * properties of subordinate ValidatorHandler objects.
     * 
     * There are 2 types of key used to retrieve a Subordinate Validator:
     * <ul>
     *  <li><b>Language Level</b>The {@link FeaturePropertyProvider} retrieved with a Language Level key will allow you to set features and properties for all validators created
     * for a certain schema validation language, eg XML Schema or Relax NG.  In this case, the key corresponds to the namespace name of the relevant SchemaFactory.  A common example 
     * would be {@code http://www.w3.org/2001/XMLSchema}.
     *  </li>
     *  <li><b>Schema Level</b>An {@link FeaturePropertyProvider} retrieved wit a Schema Level key will allow you to set features and properties for the validator created for 
     * a particular schema.  For a particular {@code xml-model} processing instruction, this corresponds to the {@code href} element in that processing instruction.</li>
     * </ul>
     * 
     * As the subordinate ValidatorHandlers are generally constructed after validation commences, this property allows you to preset features and properties for a subordinate validator.
     * These presets will be applied when the IntrinsicValidatorHandler constructs the subordinate validator.  Be warned that setting an illegal feature or property on a subordinate handler
     * can result in a {@link SAXNotRecognizedException} or {@link SAXNotSupportedException} being thrown when the IntrinsicValidatorHandler accesses a subordinate handler.  How these exceptions
     * are handled can be controlled by the {@link #FEATURE_TREAT_INVALID_SUBORDINATE_FEATURES_AS_ERRORS} feature.
     * 
     * Note that there are 2 additional properties available on every {@link FeaturePropertyProvider} in the subordinate validator Map.  See {@link #SUBORDINATE_PROPERTY_PHASE_OVERRIDE} and 
     * {@link #SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME}.
     */
    public static final String PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES="http://com.componentcorp.xml.validator.ValidationConstants/property/subordinate-features-and-properties";

    /**
     * The {@link #PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES} property allows you to preset properties and features of validators as they are discovered during xml parsing.  Because of this,
     * it is possible to set a non-existent feature or property, without any warning or error.  The errors will eventually be thrown when the presets are bound to the validator.  If this feature
     * is set to true, then this will result in an error being added to the error handler.  The default is false, in which case the exceptions are recorded as warnings.
     */
    public static final String FEATURE_TREAT_INVALID_SUBORDINATE_FEATURES_AS_ERRORS="http://com.componentcorp.xml.validator.ValidationConstants/feature/treat-invalid-subordinate-features-as-errors";


    /**
     * This property can only be set on subordinate validators (see {@link #PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES} for a description). Some subordinate validators (particularly
     * Schematron) will support the concepts of phase.  The IntrinsicValidator will need to be able to tell such a subordinate validator which phase to validate.  This subordinate property
     * identifies the name of a property on the subordinate validator which can be used to force the subordinate validator to validate a particular phase.  Note that the subordinate validator
     * must support this property.  If it does not an error or warning will be created during parsing of the xml-model processing instruction for this subordinate.
     */
    public static final String SUBORDINATE_PROPERTY_PHASE_PROPERTY_NAME="http://com.componentcorp.xml.validator.ValidationConstants/subordinate/property/subordinate-phase-property-name";
    
    /**
     * By default, the Intrinsic Schema Validator will map xml-model specified mime-types to Schema Type Namespaces according to the mapping set out in Annex B of ISO/IEC 19757-11:2011.
     * This property will allow you to specify a different mapping between mime-type and schema type namespace.  The key is the mime type, and the value is the schema type namespace.
     */
    public static final String PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP="http://com.componentcorp.xml.validator.ValidationConstants/property/mime-type-to-schematypens-map";
    /**
     * This property can only be set on subordinate validators (see {@link #PROPERTY_SUBORDINATE_FEATURES_AND_PROPERTIES} for a description). Some subordinate validators (particularly
     * Schematron) will support the concepts of phase.  Setting this property on the subordinate will force that subordinate to validate a particular phase, overriding whatever phase
     * is identified in the corresponding xml-model tag.
     */
    public static final String SUBORDINATE_PROPERTY_PHASE_OVERRIDE="http://com.componentcorp.xml.validator.ValidationConstants/subordinate/property/subordinate-phase-override";

}
