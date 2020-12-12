![CLAVIN-NERD LOGO](https://github.com/Novetta/CLAVIN-NERD/blob/develop/img/clavinLogo.png?raw=true)

![CLAVIN-NERD Master](https://github.com/Novetta/CLAVIN-NERD/workflows/MasterCI/badge.svg?branch=master)

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)



# CLAVIN-NERD
--------------
CLAVIN-NERD is a GPL-licensed "wrapper project" that connects the Apache-licensed [CLAVIN](https://github.com/Novetta/CLAVIN) geoparser with the GPL-licensed [Stanford CoreNLP NER](http://nlp.stanford.edu/software/corenlp.shtml) entity extractor.

Using CLAVIN with Stanford NER (i.e., the CLAVIN-NERD distribution) results in significantly higher accuracy than with the default Apache OpenNLP NameFinder entity extractor. We recommend using CLAVIN-NERD or Novetta's [AdaptNLP](https://github.com/Novetta/adaptnlp) over OpenNLP. Stanford NER is not included in the standard CLAVIN release because Stanford NER is GPL-licensed and we are committed to distributing CLAVIN itself via the Apache License. Thus, the GPL-licensed CLAVIN-NERD distribution makes CLAVIN available for use with Stanford NER while preserving the freedom of the core CLAVIN source code under the terms of the Apache License.

## Breaking changes

This release includes breaking changes in the form of an update to all namespaces.  The namespaces have been changed from com.bericotech to com.novetta which reflects a change in corporate ownership, and re-alignment to our new domain. 

## How to build and use CLAVIN-NERD:

CLAVIN-NERD relies on CLAVIN to build its lucene index.  You can refer to the [instructions for getting started with CLAVIN](https://github.com/Berico-Technologies/CLAVIN) before attempting to work with CLAVIN-NERD. Here are the instructions for building the index using CLAVIN-NERD:

1. Check out a copy of the source code:

```
git clone https://github.com/Novetta/CLAVIN-NERD.git
```

2. Move into the newly-created CLAVIN-NERD directory:

```	
cd CLAVIN-NERD
```

3. Download the latest version of allCountries.zip gazetteer file from GeoNames.org:

```
curl -O http://download.geonames.org/export/dump/allCountries.zip
```

4. Unzip the GeoNames gazetteer file:

```
unzip allCountries.zip
```

5. Package the source code:

```
mvn clean package
```

6. Create the Lucene Index (this one-time process will take several minutes):

```
MAVEN_OPTS="-Xmx4g" mvn exec:java -Dexec.mainClass="com.novetta.clavin.index.IndexDirectoryBuilder"
```

7. Run the example program:

Once you've used CLAVIN to build the required Lucene index with the GeoNames.org gazetteer, consult `WorkflowDemoNERD.java` for multiple examples of different ways to use CLAVIN-NERD. You can run the CLAVIN-NERD demo from the command line with the following command: 

```
MAVEN_OPTS="-Xmx2g" mvn exec:java -Dexec.mainClass="com.novetta.clavin.nerd.WorkflowDemoNERD"	
```

The main difference between using CLAVIN and CLAVIN-NERD is in the arguments passed to the `GeoParserFactory` class to instantiate a `GeoParser` object. With CLAVIN-NERD, we need to specify that we want to use the `StanfordExtractor` to extract location names from text.

Here's an example call to `GeoParserFactory` where we specify that the `StanfordExtractor` should be used, as seen in the `WorkflowDemoNERD` class:

    GeoParserFactory.getDefault("./IndexDirectory", new StanfordExtractor(), 1, 1, false);

**Don't forget:** Loading the worldwide gazetteer uses a non-trivial amount of memory. When using CLAVIN-NERD in your own programs, if you encounter `Java heap space` errors, bump up the maximum heap size for your JVM. Allocating 2GB (e.g., `-Xmx2g`) is a good place to start.

### Get it from Maven Central:


```xml
<dependency>
    <groupId>com.novetta</groupId>
    <artifactId>CLAVIN-nerd</artifactId>
    <version>3.0.0</version>
</dependency>
```

## License:

Since the Stanford CoreNLP NER library is licensed via the GPL, CLAVIN-NERD is as well. However, CLAVIN itself remains under the Apache License, version 2.

-------------------

CLAVIN-NERD
Copyright (C) 2012-2020 Novetta

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
