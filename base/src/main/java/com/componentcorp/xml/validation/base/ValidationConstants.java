/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/**
 *
 * @author rlamont
 */
public interface ValidationConstants {
    
    /**
     * The namespace of the {@link SchemaFactory} used to validate documents according to the xml-model spec.  See the documentation on
     * {@link SchemaFactory}.
     */
    public static final String INTRINSIC_NS_URI="http://componentcorp.com/xml/ns/xml-model/1.0";


    
    /**
     * Property on {@link Validator} or {@link ValidatorHandler} which enables namespace support in the underlying SAX processor.  Defaults to 'true'.
     */
    public static final String FEATURE_NAMESPACE_AWARE="http://xml.org/sax/features/namespaces";

    
    /**
     * When using JAXP techniques to validate a document, any ValidatorHandler will be instantiated in an opaque manner.  This means under normal
     * circumstances it is impossible to access the ValidatorHandler instance or subordinate instances in the case of the <i>IntrinsicValidator</i>.  
     * Many ValidatorHandlers will require further configuration (via {@link ValidatorHandler#setFeature(java.lang.String, boolean)} or 
     * {@link ValidatorHandler#setProperty(java.lang.String, java.lang.Object)} ).
     * 
     * To achieve this, set this write-only property on the LifecycleSchemaFactory to a class implementing {@link ValidatorHandlerConstructionCallback}.
     * When an {@link LifecycleSchema} constructs a {@link ValidatorHandler}, it will call this callback passing you a proxy {@link FeaturePropertyProvider}
     * which enables you to access and modify features and properties of that ValidatorHandler.
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
     * Read-only property on {@link ValidatorHandler} which can be used to retrieve a proxy {@link org.xml.sax.ext.DeclHandler} which wraps the ValidatorHandler.
     * 
     * A ValidatorHandler may implement {@link org.xml.sax.ext.DeclHandler} so they can delegate DTD Structure validation to a DTD Validator.  This proxy can then be 
     * passed to a {@link javax.xml.parsers.SAXParser} (see {@link org.xml.sax.ext.DeclHandler}) to enable the Handler to receive these events.
     * 
     * Note that JAXP style {@link org.xml.sax.ext.DeclHandler}s can not be made to work with DocumentBuilder at this time.
     */
    public static final String PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER="http://com.componentcorp.xml.validator.ValidationConstants/property/validator-as-declaration-handler";
    
    /**
     * This property can be applied to a {@link ValidatorHandler} to declare that the handler wraps a user defined {@link org.xml.sax.ext.DeclHandler}.  To use this feature,
     * you must also use the {@link #PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER} property to retrieve a proxy DeclHandler which can then be set on a {@link javax.xml.parsers.SAXParser}.
     * 
     * Note that this property is the same as that defined in the documentation of {@link org.xml.sax.ext.DeclHandler}.
     * 
     * Note that JAXP style {@link org.xml.sax.ext.DeclHandler}s can not be made to work with DocumentBuilder at this time.
     */
    public static final  String PROPERTY_DECLARATION_HANDLER="http://xml.org/sax/properties/declaration-handler";
    
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
     * to this schema type.  
     */
    public static final String DTD_SCHEMA_TYPE="[dtd]";
    
    /**
     * Suggested namespace name for Relax NG Compact Schema.  This name corresponds to the validator provided by Jing. 
     */
    public static final String RELAX_NG_COMPACT_SCHEMA_TYPE="http://www.iana.org/assignments/media-types/application/relax-ng-compact-syntax";
    
    /**
     * Suggested namespace name for Schematron Schema Language.  This name corresponds to Annex B of ISO/IEC 19757-11:2011. 
     */
    public static final String SCHEMATRON_SCHEMA_TYPE="http://purl.oclc.org/dsdl/schematron";
    
    /**
     * Suggested namespace name for NVDL Schema Language.  This name corresponds to Annex B of ISO/IEC 19757-11:2011. 
     */
    public static final String NVDL_SCHEMA_TYPE="http://purl.oclc.org/dsdl/nvdl/ns/structure/1.0";
}
