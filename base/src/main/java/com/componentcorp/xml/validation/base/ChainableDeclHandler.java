/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import org.xml.sax.ext.DeclHandler;

/**
 * Implementers of this interface are DeclHandlers which can pipe events along a 
 * chain of DeclHandlers. This means that DeclHandlers can augment results
 * as they pass them along.
 * @author rlamont
 */
public interface ChainableDeclHandler extends DeclHandler {
    
    /**
     * Set the next DeclHandler in the chain.  Any {@link org.xml.sax.ext.DeclHandler}
     * methods invoked should be passed along to {@code  declHandler} after 
     * processing.
     * @param declHandler the next {@link org.xml.sax.ext.DeclHandler} in the chain.
     */
    void setNextInChain(DeclHandler declHandler);
    
    /**
     * Retrieves the next {@link org.xml.sax.ext.DeclHandler} in the chain.
     * @return the next {@link org.xml.sax.ext.DeclHandler} in the chain.
     */
    DeclHandler getNextInChain();
    
}
