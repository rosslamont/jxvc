/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.base;

import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/**
 *
 * <p>Together with {@link LifecycleProxySchemaFactory}, you can wrap a Schema object
 * with a LifecycleProxySchema to get access to the lifecycle events.  This is 
 * particulary useful during SAX parsing.</p>
 * 
 * <p>Generally, you do not need to instantiate this class directly, as it is 
 * normally constructed by {@link LifecycleProxySchemaFactory}</p>
 * @author rlamont
 */
public class LifecycleProxySchema extends LifecycleSchema{

    private final Schema wrapped;
    
    protected LifecycleProxySchema(Schema wrapped,LifecycleSchemaFactory parent) {
        super(parent);
        this.wrapped=wrapped;
    }

    @Override
    protected ValidatorHandler newValidatorHandlerInternal() {
        return wrapped.newValidatorHandler();
    }

    @Override
    public Validator newValidator() {
        return wrapped.newValidator();
    }
    
}
