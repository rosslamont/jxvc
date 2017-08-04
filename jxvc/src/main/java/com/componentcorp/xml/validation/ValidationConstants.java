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
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Utility class to contain standard constants, particularly standard 
 * property and feature identifiers.
 * @author rlamont
 */
public interface ValidationConstants {
    
    /**
     * The namespace of the {@link SchemaFactory} used to validate documents according to the xml-model spec.  See the documentation on
     * {@link SchemaFactory}.
     */
    public static final String INTRINSIC_NS_URI="http://componentcorp.com/xml/ns/xml-model/1.0";
    
//    public static  enum ConflictResolution{
//        MODEL_ONLY,
//        MODEL_FIRST,
//        MODEL_FIRST_IGNORE_XSD_CONFLICT,
//        XSI_FIRST,
//        XSI_FIRST_IGNORE_XSD_CONFLICT,
//        XSI_ONLY
//    } ;
//    public static final String PROPERTY_CONFLICT_RESOLUTION="http://com.componentcorp.xml.validator.ValidationConstants/property/conflict-resolution";
    
    
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
     * Property on {@link IntrinsicValidator}  which enables namespace support in the underlying SAX processor.  Defaults to 'true'.
     */
    public static final String FEATURE_NAMESPACE_AWARE="http://xml.org/sax/features/namespaces";
    
    /**
     * If no validator is discovered from the document before the first element, then load the validator from the validation factory identified by this 
     * URI.  The default value is {@link javax.xml.XMLConstants#W3C_XML_SCHEMA_NS_URI}.  Can be set to null to disable default validation.
     */
    public static final String PROPERTY_DEFAULT_VALIDATOR="http://com.componentcorp.xml.validator.ValidationConstants/property/default-validator";
    
    
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

    /**
     * Standard mime-type used to identify dtd documents.
     */
    public static final String DTD_MIME_TYPE="application/xml-dtd";
    
    /**
     * Standard mime-type used to identify compact relax NG syntax documents.
     */
    public static final String RELAX_NG_COMPACT_MIME_TYPE="application/relax-ng-compact-syntax";
    
    /**
     * Schema Type Namespace for dtd documents (non-standard).  As there is no official schema type namespace for DTDs, IntrinsicValidator will by default map the {@link #DTD_MIME_TYPE}
     * to this schema type.  Provide your own mapping via {@link #PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP}.
     */
    public static final String DTD_SCHEMA_TYPE="[dtd]";
    
    /**
     * Suggested namespace name for Relax NG Compact Schema.  This name corresponds to the validator provided by Jing. 
     * Provide your own mapping via {@link #PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP}.
     */
    public static final String RELAX_NG_COMPACT_SCHEMA_TYPE="http://www.iana.org/assignments/media-types/application/relax-ng-compact-syntax";
    
    /**
     * Suggested namespace name for Schematron Schema Language.  This name corresponds to Annex B of ISO/IEC 19757-11:2011. 
     * Provide your own mapping via {@link #PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP}.
     */
    public static final String SCHEMATRON_SCHEMA_TYPE="http://purl.oclc.org/dsdl/schematron";
    
    /**
     * Suggested namespace name for NVDL Schema Language.  This name corresponds to Annex B of ISO/IEC 19757-11:2011. 
     * Provide your own mapping via {@link #PROPERTY_MIME_TYPE_TO_SCHEMATYPENS_MAP}.
     */
    public static final String NVDL_SCHEMA_TYPE="http://purl.oclc.org/dsdl/nvdl/ns/structure/1.0";
}
