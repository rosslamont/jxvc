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

import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 *
 * @author rlamont
 */
class IntrinsicValidatorHandler extends ValidatorHandler implements  DeclHandler,FeaturePropertyProvider{
    
    private Sax2DefaultHandlerWrapper contentHandler=new Sax2DefaultHandlerWrapper(null, null,false);
    private ErrorHandler errorHandler;
    private LSResourceResolver resourceResolver;
    private ValidationConstants.ConflictResolution propertyConflictResolutionMethod=ValidationConstants.ConflictResolution.MODEL_FIRST;
    private final Map<String,ValidatorHandlerProxy> discoveredValidatorsForFeaturesMap=new HashMap<String, ValidatorHandlerProxy>();
    private final Map<String,Sax2DefaultHandlerWrapper> validatorCache=new WeakHashMap<String, Sax2DefaultHandlerWrapper>();
    private final List<Sax2DefaultHandlerWrapper> currentOrderedValidators=new ArrayList<Sax2DefaultHandlerWrapper>();
    private boolean firstElementPassed=false;
    private final Collection<DeferredAction> deferredActions=new ArrayList();
    private Locator locator;
    //private final Set<String> foundNamespaces=new HashSet<String>();    //Used to manage Section 4.3.2.4 of https://www.w3.org/TR/xmlschema-1
    private final FeaturePropertyProviderInternal featuresAndProperties;
    private ContentHandler firstContentHandler=null;
    private DeclHandler firstDeclHandler=null;
    
    
    
    private static final String  XML_MODEL="xml-model";
    
    
    IntrinsicValidatorHandler(FeaturePropertyProviderInternal factory){
        featuresAndProperties=factory;
        featuresAndProperties.addAllowedProperty(ValidationConstants.PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER, FeaturePropertyProviderInternal.ReadWriteable.READ_ONLY);
        featuresAndProperties.addAllowedProperty(ValidationConstants.PROPERTY_DECLARATION_HANDLER, FeaturePropertyProviderInternal.ReadWriteable.READ_WRITE);
    }

    @Override
    public void setContentHandler(ContentHandler receiver) {
        //TODO: There needs to be property to check to see if we should use it as a DeclHandler or not
        this.contentHandler=new Sax2DefaultHandlerWrapper(receiver, null,true);
    }

    @Override
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    @Override
    public void setErrorHandler(final ErrorHandler errorHandler) {
        this.errorHandler=errorHandler;
        setErrorHandlerInternal(errorHandler);
        if(!firstElementPassed){
            deferredActions.add(new DeferredAction() {
                public void perform() throws SAXException {
                    setErrorHandlerInternal(errorHandler);
                }
            });
        }
    }
    
    private void setErrorHandlerInternal(ErrorHandler errorHandler){
        for(Sax2DefaultHandlerWrapper wrapper:currentOrderedValidators){
            wrapper.setErrorHandler(errorHandler);
        }
        
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setResourceResolver(final LSResourceResolver resourceResolver) {
        this.resourceResolver=resourceResolver;
        setResourceResolverInternal(resourceResolver);
        if(!firstElementPassed){
            deferredActions.add(new DeferredAction() {
                public void perform() throws SAXException {
                    setResourceResolverInternal(resourceResolver);
                }
            });
        }
    }

    private void setResourceResolverInternal(LSResourceResolver resourceResolver) {
        for(Sax2DefaultHandlerWrapper wrapper:currentOrderedValidators){
            wrapper.setResourceResolver( resourceResolver) ;
        }
    }
    
    @Override
    public LSResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    @Override
    public TypeInfoProvider getTypeInfoProvider() {
        //TODO: Somehow, we need to merge all type info providers from subordinate validators.
        return null;
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setDocumentLocator(final Locator locator) {
        this.locator=locator;
        if (firstElementPassed){
            setDocumentLocatorInternal(locator);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform() {
                    setDocumentLocatorInternal(locator);
                }
            });
        }
    }
    
    private void setDocumentLocatorInternal(final Locator locator){
        for(Sax2DefaultHandlerWrapper wrapper:currentOrderedValidators){
            wrapper.setDocumentLocator(locator);
        }
        contentHandler.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
        reset();
        deferredActions.add(new DeferredAction() {
            public void perform() throws SAXException{
                startDocumentInternal();
            }
        });
    }
    
    private void startDocumentInternal() throws SAXException {
        firstContentHandler.startDocument();
    }

    public void endDocument() throws SAXException {
        //end is truly the end, so we don't defer anything as there is nothing 
        //to defer to.  Instead we flush out any deferred actions and then
        //just end.
        performDeferredActions();
        firstContentHandler.endDocument();
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if (firstElementPassed){
            startPrefixMappingInternal(prefix, uri);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform() throws SAXException {
                    startPrefixMappingInternal(prefix, uri);
                }
            });
        }
    }
    
    

    private void startPrefixMappingInternal(String prefix, String uri) throws SAXException {
        firstContentHandler.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        if (firstElementPassed){
            endPrefixMappingInternal(prefix);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    endPrefixMappingInternal(prefix);
                }
            });
        }
    }

    
    
    private void endPrefixMappingInternal(String prefix) throws SAXException {
        firstContentHandler.endPrefixMapping(prefix);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        handleElement(uri, localName, qName, atts);
        if (!firstElementPassed){
            handleRootElement(uri,localName,qName,atts);
        }
        firstContentHandler.startElement(uri, localName, qName, atts);
    }

    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        firstContentHandler.endElement(uri, localName, qName);
    }

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (firstElementPassed){
            charactersInternal(ch, start, length);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    charactersInternal(ch, start, length);
                }
            });
        }
    }

    
    private void charactersInternal(char[] ch, int start, int length) throws SAXException {
        firstContentHandler.characters(ch, start, length);
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        if (firstElementPassed){
            ignorableWhitespaceInternal(ch, start, length);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform() throws SAXException {
                    ignorableWhitespaceInternal(ch, start, length);
                }
            });
        }
    }

    private void ignorableWhitespaceInternal(char[] ch, int start, int length) throws SAXException {
        firstContentHandler.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        if (!firstElementPassed && XML_MODEL.equals(target)){
            createXMLModelBasedValidator(data);
        }
        if (firstElementPassed){
            processingInstructionInternal(target, data);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    processingInstructionInternal(target, data);
                }
            });
        }
    }
    
    private void processingInstructionInternal(String target, String data) throws SAXException {
        firstContentHandler.processingInstruction(target, data);
    }

    public void skippedEntity(final String name) throws SAXException {
        if (firstElementPassed){
            skippedEntityInternal(name);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform() throws SAXException {
                    skippedEntityInternal(name);
                }
            });
        }
    }

    private void skippedEntityInternal(String name) throws SAXException {
        firstContentHandler.skippedEntity(name);
    }
    
    /**
     * The following features are supported: TODO
     * 
     * @param name
     * @param value
     * @throws SAXNotRecognizedException
     * @throws SAXNotSupportedException 
     */
    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        featuresAndProperties.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return featuresAndProperties.getFeature(name);
    }
    
    

    /**
     * The following properties are supported: TODO
     * 
     * @param name
     * @param object
     * @throws SAXNotRecognizedException
     * @throws SAXNotSupportedException 
     */
    
    @Override
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        featuresAndProperties.setProperty(name, object);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        Object ret= featuresAndProperties.getProperty(name);
        if (ret ==null && ValidationConstants.PROPERTY_VALIDATOR_AS_DECLARATION_HANDLER.equals(name)){
            ret = new AsDeclHandler();
            featuresAndProperties.setReadOnlyProperty(name, ret);
        }
        return ret;
    }
    
    
    
    
    

    void reset() {
        for (ValidatorHandlerProxy proxy:discoveredValidatorsForFeaturesMap.values()){
            proxy.clear();
        }
        firstElementPassed=false;
        deferredActions.clear();
//        foundNamespaces.clear();
        //forcably reset the error handler so that deferred actions are created.
        setErrorHandler(errorHandler);
        setResourceResolver(resourceResolver);
        setDocumentLocator(locator);
    }

    private void createXMLModelBasedValidator(String data) throws SAXException{
        final String HREF="href";
        final String TYPE="type";
        final String SCHEMATYPENS="schematypens";
        final String CHARSET="charset";
        final String TITLE="title";
        final String GROUP="group";
        final String PHASE="phase";
        Schema schema=null;
        Map<String,String> xmlModelPseudoAttributes = ProcessingInstructionParser.parseData(data);
        String href=xmlModelPseudoAttributes.get(HREF);
        String type=xmlModelPseudoAttributes.get(TYPE);
        String schematypens=xmlModelPseudoAttributes.get(SCHEMATYPENS);
        String charset=xmlModelPseudoAttributes.get(CHARSET);
        String title=xmlModelPseudoAttributes.get(TITLE);
        String group=xmlModelPseudoAttributes.get(GROUP);
        String phase=xmlModelPseudoAttributes.get(PHASE);
        SchemaFactory factory=null;
        if (href==null){
            throw new SAXParseException("'href' must be provided in xml-model processing instructions",locator);
        }
        if (charset!=null){
            Charset checkCharset=Charset.forName(charset);
            if (checkCharset==null){
                throw new SAXParseException(charset+" is not a known charset in xml-model processing instruction", locator);
            }
        }
        //lookup cache
        Sax2DefaultHandlerWrapper wrapper = validatorCache.get(href);
        if (wrapper!=null){
            //discard the wrapper if the encoding differs
            
            if (wrapper.getInputSource().getEncoding()==null && charset!=null){//encoding is always set
                wrapper=null;
            }
            if (charset==null && wrapper.getInputSource().getEncoding()!=null){
                wrapper=null;
            }
            if ((charset!=null && !charset.equals(wrapper.getInputSource().getEncoding()))){
                wrapper=null;
            }
            
        }
        if (wrapper==null){
            if (schematypens !=null){
                factory = SchemaFactory.newInstance(schematypens);
                if (factory ==null){
                    throw new SAXParseException("Invalid schema factory",locator);
                }

            }
            LSInput input = getResourceResolver().resolveResource(type, schematypens, href, href, null);
            if (input!=null){
                if (charset!=null){
                    input.setEncoding(charset);
                }
                Source source=null;
                InputStream stream= input.getByteStream();
                if (stream!=null){
                    source = new StreamSource(stream);
                }
                else{
                    Reader reader=input.getCharacterStream();
                    if (reader!=null){
                        source = new StreamSource(reader);
                    }
                }
                if (source!=null&& factory!=null){
                    schema = factory.newSchema(source);
                    InputSource inputSource = new InputSource(input.getSystemId());
                    inputSource.setPublicId(input.getPublicId());
                    inputSource.setEncoding(charset);
                    wrapper=new Sax2DefaultHandlerWrapper(schema.newValidatorHandler(), inputSource,true);
                    validatorCache.put(href,wrapper);
                }
            }
        }
        if (wrapper ==null){
            throw new SAXParseException("Could not load validation source", locator);
        }
        if (wrapper!=null){
            addNewValidator(wrapper);
        }
    }

    private void performDeferredActions() throws SAXException {
        Iterator<DeferredAction> deferredActionIterator = deferredActions.iterator();
        while(deferredActionIterator.hasNext()){
            DeferredAction action = deferredActionIterator.next();
            deferredActionIterator.remove();
            action.perform();
        }
    }
    
    /**
     * Identify any xsi attributes to pull in XSD validation if required.
     * @param uri
     * @param localName
     * @param qName
     * @param atts
     * @throws SAXException 
     */
    private void handleElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        
        //TODO: xsi attributes can be on any element, so in the future we might attempt to identify 
        //the xsd schema as we go through the document, but this poses all sorts of replay problems
        //where a late found  xsd processor needs to be setup after first element.  At the moment the 
        //solution is to use a default processor.
        String schemaLocation;
        String noNamespaceSchemaLocation;
        for (int i=0;i< atts.getLength();i++){
            String attName=atts.getLocalName(i);
            String attQName = atts.getQName(i);
            String attUri = atts.getURI(i);
            int k=atts.getIndex(attQName);  //discard this line
        }
        
    }

    private void handleRootElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        firstElementPassed=true;
        SAXException lateThrow=null;
        String defaultValidatorUri=(String) featuresAndProperties.getProperty(ValidationConstants.PROPERTY_DEFAULT_VALIDATOR);
        if (this.currentOrderedValidators.isEmpty() && defaultValidatorUri!=null){
            Sax2DefaultHandlerWrapper wrapper = validatorCache.get(defaultValidatorUri);
            if (wrapper==null){
                SchemaFactory schemaFactory = SchemaFactory.newInstance(defaultValidatorUri);
                if (schemaFactory==null){
                    lateThrow = new SAXParseException("Unable to load default schema factory", locator);
                }
                try{
                    Schema schema = schemaFactory.newSchema();
                    InputSource inputSource = new InputSource();
                    wrapper=new Sax2DefaultHandlerWrapper(schema.newValidatorHandler(), inputSource,true);
                    validatorCache.put(defaultValidatorUri,wrapper);
                }
                catch(UnsupportedOperationException uoe){
                    lateThrow = new SAXNotSupportedException("The chosen default SchemaFactory does not support zero argument newSchema()");
                }
            }
            if (wrapper !=null){
                addNewValidator(wrapper);
            }
            
        }
        createValidatorChain();
        performDeferredActions();
        if (lateThrow !=null){
            throw lateThrow;
        }
    }

    
    private void createValidatorChain() throws SAXNotRecognizedException, SAXNotSupportedException {
        firstContentHandler = contentHandler;
        firstDeclHandler = featuresAndProperties.getProperty(ValidationConstants.PROPERTY_DECLARATION_HANDLER);
        ListIterator<Sax2DefaultHandlerWrapper> wrapperIterator = currentOrderedValidators.listIterator(currentOrderedValidators.size());
        while(wrapperIterator.hasPrevious()){
            Sax2DefaultHandlerWrapper wrapper=wrapperIterator.previous();
            wrapper.asValidatorHandler.setContentHandler(firstContentHandler);
            firstContentHandler=wrapper;
        }
    }
    
    private void addNewValidator(Sax2DefaultHandlerWrapper wrapper)
    {
                currentOrderedValidators.add(wrapper);
                //TODO: Tie in the ValidatorHandlerProxy
    }

    //DeclHandler methods
    
    @Override
    public void elementDecl(final String name, final String model) throws SAXException {
        if (firstElementPassed){
            elementDeclInternal(name, model);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    elementDeclInternal(name, model);
                }
            });
        }
    }
    public void elementDeclInternal(final String name, final String model) throws SAXException {
        if (firstDeclHandler!=null){
            firstDeclHandler.elementDecl(name, model);
        }
    }

    @Override
    public void attributeDecl(final String eName, final String aName, final String type, final String mode, final String value) throws SAXException {
        if (firstElementPassed){
            attributeDeclInternal(eName, aName, type, mode, value);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    attributeDeclInternal(eName, aName, type, mode, value);
                }
            });
        }
    }
    public void attributeDeclInternal(String eName, String aName, String type, String mode, String value) throws SAXException {
        if (firstDeclHandler!=null){
            firstDeclHandler.attributeDecl(eName, aName, type, mode, value);
        }
    }

    @Override
    public void internalEntityDecl(final String name, final String value) throws SAXException {
        if (firstElementPassed){
            internalEntityDeclInternal(name, value);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    internalEntityDeclInternal(name, value);
                }
            });
        }
    }
    public void internalEntityDeclInternal(String name, String value) throws SAXException {
        if (firstDeclHandler!=null){
            firstDeclHandler.internalEntityDecl(name, value);
        }
    }

    @Override
    public void externalEntityDecl(final String name, final String publicId, final String systemId) throws SAXException {
        if (firstElementPassed){
            externalEntityDeclInternal(name, publicId, systemId);
        }
        else{
            deferredActions.add(new DeferredAction() {
                public void perform()  throws SAXException{
                    externalEntityDeclInternal(name, publicId, systemId);
                }
            });
        }
    }
    public void externalEntityDeclInternal(String name, String publicId, String systemId) throws SAXException {
        if (firstDeclHandler!=null){
            firstDeclHandler.externalEntityDecl(name, publicId, systemId);
        }
    }

    
    /**
     * Proxy which wraps a collection of ValidatorHandlers for a particular
     * type of validator, principally so that a client of the API can 
     * apply type specific properties and features to that type of 
     * ValidatorHandler.
     */
    private final class ValidatorHandlerProxy extends ValidatorHandler{

        //TODO - consider whether more agressive retention of the wrapped handler should take
        //place.
        //TODO - potentially multiple schema documents could be validated for the one type of 
        //validator.
        
        private final Collection<WeakReference<Sax2DefaultHandlerWrapper>> wrappedCollection=new ArrayList<WeakReference<Sax2DefaultHandlerWrapper>>();
        private final ReferenceQueue<Sax2DefaultHandlerWrapper> wrappedRefQueue=new ReferenceQueue<Sax2DefaultHandlerWrapper>();
        private final Map<String,Boolean> deferredFeatures=new HashMap<String, Boolean>();
        private final Map<String,Object> deferredProperties=new HashMap<String, Object>();

        public ValidatorHandlerProxy() {
        }

        public ValidatorHandlerProxy(Sax2DefaultHandlerWrapper wrapped) {
            this.wrappedCollection .add(new WeakReference<Sax2DefaultHandlerWrapper>(wrapped,wrappedRefQueue));
        }
        
        
        void wrapHandler(Sax2DefaultHandlerWrapper handler){
            if (wrappedCollection!=null){
                throw new IllegalStateException("This proxy already has a delegate.");
            }
            this.wrappedCollection .add(new WeakReference<Sax2DefaultHandlerWrapper>(handler,wrappedRefQueue));;
            
        }
        
        private boolean isWrapping(){
            return wrappedCollection!=null;
        }
        
        private void clear(){
            wrappedCollection.clear();
        }
        
        @Override
        public void setContentHandler(ContentHandler receiver) {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public ContentHandler getContentHandler() {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public void setErrorHandler(ErrorHandler errorHandler) {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public ErrorHandler getErrorHandler() {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used.");
        }

        @Override
        public void setResourceResolver(LSResourceResolver resourceResolver) {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public LSResourceResolver getResourceResolver() {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public TypeInfoProvider getTypeInfoProvider() {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void setDocumentLocator(Locator locator) {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void startDocument() throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void endDocument() throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void processingInstruction(String target, String data) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        public void skippedEntity(String name) throws SAXException {
            throw new UnsupportedOperationException("This ValidatorHandler cannot be directly used."); 
        }

        @Override
        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            Iterator<WeakReference<Sax2DefaultHandlerWrapper>> iterator = wrappedCollection.iterator();
            while(iterator.hasNext()){
                WeakReference<Sax2DefaultHandlerWrapper> ref = iterator.next();
                if (ref!=null){
                    Sax2DefaultHandlerWrapper handler=ref.get();
                    if (handler!=null){
                        return handler.getAsValidatorHandler().getProperty(name);
                    }
                }
            }
            Object o = deferredProperties.get(name);
            if (o==null){
                throw new SAXNotSupportedException("The proxy has not been initialised and no value has yet been set for this property");
            }
            return o;
        }

        @Override
        public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
            Iterator<WeakReference<Sax2DefaultHandlerWrapper>> iterator = wrappedCollection.iterator();
            while(iterator.hasNext()){
                WeakReference<Sax2DefaultHandlerWrapper> ref = iterator.next();
                if (ref!=null){
                    Sax2DefaultHandlerWrapper handler=ref.get();
                    if (handler!=null){
                        handler.getAsValidatorHandler().setProperty(name, object); //may throw an exception if not appropriate
                    }
                }
            
            }
            deferredProperties.put(name, object);
        }

        @Override
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            Iterator<WeakReference<Sax2DefaultHandlerWrapper>> iterator = wrappedCollection.iterator();
            while(iterator.hasNext()){
                WeakReference<Sax2DefaultHandlerWrapper> ref = iterator.next();
                if (ref!=null){
                    Sax2DefaultHandlerWrapper handler=ref.get();
                    if (handler!=null){
                        handler.getAsValidatorHandler().setFeature(name, value); //may throw an exception if not appropriate
                    }
                }
            }
            deferredFeatures.put(name, value);
        }

        @Override
        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            Iterator<WeakReference<Sax2DefaultHandlerWrapper>> iterator = wrappedCollection.iterator();
            while(iterator.hasNext()){
                WeakReference<Sax2DefaultHandlerWrapper> ref = iterator.next();
                if (ref!=null){
                    Sax2DefaultHandlerWrapper handler=ref.get();
                    if (handler!=null){
                        return handler.getAsValidatorHandler().getFeature(name);
                    }
                }
            }
            Boolean b = deferredFeatures.get(name);
            if (b==null){
                throw new SAXNotSupportedException("The proxy has not been initialised and no value has yet been set for this feature");
            }
            return b;
        }
        
        
        
    }
    
    /**
     * Ideally a lambda, but I guess we have to have at least java 6 support
     */
    private interface DeferredAction{
        void perform() throws SAXException;
    }
    
    /**
     * Wrapper which implements all Sax2 standard and extension handlers.  The main
     * purpose of this class is to optimize out instanceof operations to construction
     * time.  
     * <p>
     * SecondaryPurpose is to associate the InputSource so that when used as a 
     * validatorHandler it can be stored in a map by systemId;
     * <p>
     * <i>Note:</i>Could probably become a package class
     * 
     */
    private final class Sax2DefaultHandlerWrapper implements ContentHandler,ErrorHandler,DeclHandler{
        private final ContentHandler asContentHandler;
        private final DeclHandler asDeclHandler;
        private ErrorHandler errorHandler;
        private final ValidatorHandler asValidatorHandler;
        private final InputSource inputSource;

        public Sax2DefaultHandlerWrapper(Object handler, InputSource inputSource,boolean useAsDeclHandlerIfPossible) {
            this.inputSource=inputSource;
            asContentHandler=(ContentHandler) (handler instanceof ContentHandler?handler:null);
            errorHandler=(ErrorHandler) (handler instanceof ErrorHandler?handler:null);
            asValidatorHandler=(ValidatorHandler) (handler instanceof ValidatorHandler?handler:null);
            
            asDeclHandler = (DeclHandler) (useAsDeclHandlerIfPossible && handler instanceof DeclHandler?handler:null);
        }
        
        //ContentHandler methods

        public void setDocumentLocator(Locator locator) {
            if (asContentHandler!=null){
                asContentHandler.setDocumentLocator(locator);
            }
        }

        public void startDocument() throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.startDocument();
            }
        }

        public void endDocument() throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.endDocument();
            }
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.startPrefixMapping(prefix, uri);
            }
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.endPrefixMapping(prefix);
            }
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.startElement(uri, localName, qName, atts);
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.endElement(uri, localName, qName);
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.characters(ch, start, length);
            }
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.ignorableWhitespace(ch, start, length);
            }
        }

        public void processingInstruction(String target, String data) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.processingInstruction(target, data);
            }
        }

        public void skippedEntity(String name) throws SAXException {
            if (asContentHandler!=null){
                asContentHandler.skippedEntity(name);
            }
        }
        
        
        //ErrorHandler Methods

        public void warning(SAXParseException exception) throws SAXException {
            if (errorHandler!=null){
                errorHandler.warning(exception);
            }
        }

        public void error(SAXParseException exception) throws SAXException {
            if (errorHandler!=null){
                errorHandler.error(exception);
            }
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            if (errorHandler!=null){
                errorHandler.fatalError(exception);
            }
        }
        
        public ValidatorHandler getAsValidatorHandler() {
            return asValidatorHandler;
        }

        public InputSource getInputSource() {
            return inputSource;
        }

        private void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler=errorHandler;
            if (asValidatorHandler!=null){
                asValidatorHandler.setErrorHandler(errorHandler);
            }
        }

        private void setResourceResolver(LSResourceResolver resourceResolver) {
            if (asValidatorHandler!=null){
                asValidatorHandler.setResourceResolver(resourceResolver);
            }
        }

        @Override
        public void elementDecl(String name, String model) throws SAXException {
            if (asDeclHandler!=null){
                asDeclHandler.elementDecl(name, model);
            }
        }

        @Override
        public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
            if (asDeclHandler!=null){
                asDeclHandler.attributeDecl(eName, aName, type, mode, value);
            }
        }

        @Override
        public void internalEntityDecl(String name, String value) throws SAXException {
            if (asDeclHandler!=null){
                asDeclHandler.internalEntityDecl(name, value);
            }
        }

        @Override
        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
            if (asDeclHandler!=null){
                asDeclHandler.externalEntityDecl(name, publicId, systemId);
            }
        }
        
        
    }

    private class AsDeclHandler implements DeclHandler{

        @Override
        public void elementDecl(String name, String model) throws SAXException {
            IntrinsicValidatorHandler.this.elementDecl(name, model);
        }

        @Override
        public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
            IntrinsicValidatorHandler.this.attributeDecl(eName, aName, type, mode, value);
        }

        @Override
        public void internalEntityDecl(String name, String value) throws SAXException {
            IntrinsicValidatorHandler.this.internalEntityDecl(name, value);
        }

        @Override
        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
            IntrinsicValidatorHandler.this.externalEntityDecl(name, publicId, systemId);
        }

    }
}
