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



## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Ross Lamont** - *Initial work* 

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
