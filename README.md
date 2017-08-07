# jxvc

A collection of JAXP compliant validators for different XML Validation languages.  The following validation situations are supported:

* Uber validator (IntrinsicSchemaFactory) - automatically detects the following situations:
  * xml-model processing instruction (compliant with ISO/IEC 19757-11:2011)
  * xsi annotated XML Schema
  * DTD (coming soon)
* Relax NG XML Syntax (jing wrapper)
* Relax NG Compact Syntax
* (To be implemented) Schematron
* (To be implemented) NVDL

In addition, a small library of functionality is provided to support other validation implementations.

### Installing

All artifacts are available in the maven central repository.  There are a number of artifacts provided to provide flexibility and to permit developers to substitue their own validator implementations if desired.

The artifacts provided are as follows:

#### IntrinsicSchemaFactory uber validator

```xml
<dependency>
    <groupId>com.componentcorp.xml.validation</groupId>
    <artifactId>jxvc</artifactId>
    <version>0.9.0</version>
</dependency>
```

#### RelaxNG XML Syntax validator

```xml
<dependency>
    <groupId>com.componentcorp.xml.validation</groupId>
    <artifactId>relaxng</artifactId>
    <version>0.9.0</version>
</dependency>
```

#### RelaxNG Compact Syntax validator

```xml
<dependency>
    <groupId>com.componentcorp.xml.validation</groupId>
    <artifactId>relaxng-compact</artifactId>
    <version>0.9.0</version>
</dependency>
```

#### Utility Classes

```xml
<dependency>
    <groupId>com.componentcorp.xml.validation</groupId>
    <artifactId>base</artifactId>
    <version>0.9.0</version>
</dependency>
```

## Usage

Various usage examples(https://github.com/rosslamont/jxvc/wiki/Usage-examples)

## Javadoc

Javadoc for each project is available at:

* [IntrinsicSchemaFactory uber validator](http://javadoc.io/doc/com.componentcorp.xml.validation/jxvc)
* [Relax NG XML Syntax validator](http://javadoc.io/doc/com.componentcorp.xml.validation/relaxng)
* [Relax NG Compact Syntax validator](http://javadoc.io/doc/com.componentcorp.xml.validation/relaxng-compact)
* [Utility Classes](http://javadoc.io/doc/com.componentcorp.xml.validation/base)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Ross Lamont** - *Initial work* 

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License with some variations - see the [LICENSE](LICENSE) file for details

