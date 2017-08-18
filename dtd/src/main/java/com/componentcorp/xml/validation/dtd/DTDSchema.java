/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componentcorp.xml.validation.dtd;

import com.componentcorp.xml.validation.base.LifecycleSchema;
import com.componentcorp.xml.validation.base.LifecycleSchemaFactory;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/**
 *
 * @author rlamont
 */
public class DTDSchema extends LifecycleSchema{

    public DTDSchema(LifecycleSchemaFactory parent) {
        super(parent);
    }

    public DTDSchema(LifecycleSchemaFactory parent,Source[] schemas) {
        super(parent);
        throw new UnsupportedOperationException("Not implemented yet");
    }



    @Override
    public Validator newValidator() {
        return new DTDValidator();
    }

    @Override
    protected ValidatorHandler newValidatorHandlerInternal() {
        return new DTDValidatorHandler();
    }

    
}
