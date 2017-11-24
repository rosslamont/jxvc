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
package com.componentcorp.xml.validation.test.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author rlamont
 */
public class TestContentHandler extends DefaultHandler2 implements LSResourceResolver{

    private final Collection<SAXParseException> faults=new ArrayList<SAXParseException>();
    private final Map<String,String> entityResolverMap;

    public TestContentHandler(Map<String,String> entityResolverMap) {
        this.entityResolverMap = entityResolverMap;
    }
    
    
    
    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        return null;
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseuri, String systemId) throws SAXException, IOException {
        InputStream input =null;
        String resource=entityResolverMap.get(systemId);
        if (resource!=null){
            input=this.getClass().getResourceAsStream(resource);
        }
        else if (baseuri!=null){
            try {
                URI possibleRelativeURI = new URI(systemId);
                if (!possibleRelativeURI.isAbsolute()){
                    URI baseURI = new URI(baseuri);
                    if (baseURI.isAbsolute() && "file".equals(baseURI.getScheme())){
                        URI finalURI= baseURI.resolve(possibleRelativeURI);
                        if (finalURI.isAbsolute()){
                            input=finalURI.toURL().openStream();
                        }
                    }
                }
            } catch (URISyntaxException ex) {
                
            }
            
        }
        InputSource source = new InputSource(input);
        
        source.setPublicId(publicId);
        source.setSystemId(systemId);
        return source;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        faults.add(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        faults.add(e);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        faults.add(e);
    }
    
    public Collection<SAXParseException> getFaults(){
        return faults;
    }

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, final String baseURI){
        LSInput ret = null;
        try {
            final InputSource source = resolveEntity(type, publicId, baseURI, systemId);
            if (source!=null){
                ret = new LSInput() {
                    public Reader getCharacterStream() {
                        return null;
                    }

                    public void setCharacterStream(Reader characterStream) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public InputStream getByteStream() {
                        return source.getByteStream();
                    }

                    public void setByteStream(InputStream byteStream) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getStringData() {
                        return null;
                    }

                    public void setStringData(String stringData) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getSystemId() {
                        return source.getSystemId();
                    }

                    public void setSystemId(String systemId) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getPublicId() {
                        return source.getPublicId();
                    }

                    public void setPublicId(String publicId) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getBaseURI() {
                        return baseURI;
                    }

                    public void setBaseURI(String baseURI) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getEncoding() {
                        return source.getEncoding();
                    }

                    public void setEncoding(String encoding) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean getCertifiedText() {
                        return false;
                    }

                    public void setCertifiedText(boolean certifiedText) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
            }
        } catch (SAXException ex) {
            
        } catch (IOException ex) {
            
        }
        return ret;
    }
    
}
