package com.componentcorp.xml.validation.example;

import static com.componentcorp.xml.validation.test.helpers.BaseXMLValidationTest.INTRINSIC_NS_URI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;


public class Sample1 {
    public Result validate(File f,ErrorHandler errorHandler, LSResourceResolver resourceResolver) throws SAXException, FileNotFoundException, IOException
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(INTRINSIC_NS_URI);
        Schema schema = schemaFactory.newSchema();
        final Validator validator = schema.newValidator();
        validator.setErrorHandler(errorHandler);
        validator.setResourceResolver(resourceResolver);
        InputStream is = new FileInputStream(f);
        Source source = new StreamSource(is);
        Result result = new StreamResult();
        validator.validate(source, result);
        return result;
    }
}
