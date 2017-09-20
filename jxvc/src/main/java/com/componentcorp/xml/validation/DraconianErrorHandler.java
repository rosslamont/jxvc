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

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
class DraconianErrorHandler {
    static final DraconianErrorHandler DEFAULT_ERROR_HANDLER=new DraconianErrorHandler();
    
    public void fatalError( SAXParseException e ) throws SAXException {
        throw e;
    }
    public void error( SAXParseException e ) throws SAXException {
        throw e;
    }
    public void warning( SAXParseException e ) throws SAXException {
        // noop
    }
    
}
