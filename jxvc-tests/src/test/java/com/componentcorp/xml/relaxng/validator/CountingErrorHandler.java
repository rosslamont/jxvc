/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.relaxng.validator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author rlamont
 */
public class CountingErrorHandler implements ErrorHandler{
    
    private int warningCount=0;
    private int errorCount=0;
    private int fatalErrorCount=0;

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        Logger.getLogger(CountingErrorHandler.class.getName()).log(Level.WARNING, "warning exception ",exception);
        warningCount++;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        Logger.getLogger(CountingErrorHandler.class.getName()).log(Level.SEVERE, "error exception ",exception);
        errorCount++;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        Logger.getLogger(CountingErrorHandler.class.getName()).log(Level.SEVERE, "fatalError exception ",exception);
        fatalErrorCount++;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getFatalErrorCount() {
        return fatalErrorCount;
    }
    
    public void reset(){
        warningCount=0;
        errorCount=0;
        fatalErrorCount=0;

    }
}
