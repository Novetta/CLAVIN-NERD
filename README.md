# CLAVIN-NERD
--------------
CLAVIN-NERD is a GPL-licensed "wrapper project" that connects the Apache-licensed [CLAVIN](https://github.com/Berico-Technologies/CLAVIN) geoparser with the GPL-licensed [Stanford CoreNLP NER](http://nlp.stanford.edu/software/corenlp.shtml) entity extractor.

Using CLAVIN with Stanford NER (i.e., the CLAVIN-NERD distribution) results in significantly higher accuracy than with the default Apache OpenNLP NameFinder entity extractor. We recommend using CLAVIN-NERD for any production applications. Stanford NER is not included as the default entity extractor in the standard CLAVIN release because Stanford NER is GPL-licensed, while we are committed to distributing CLAVIN itself via the Apache License. Thus, the GPL-licensed CLAVIN-NERD distribution makes CLAVIN available for use in its optimal form (that is, working with Stanford NER) while preserving the freedom of the core CLAVIN source code under the terms of the Apache License.

## How to use CLAVIN-NERD:

Please refer to the [instructions for getting started with CLAVIN](https://github.com/Berico-Technologies/CLAVIN) before attempting to work with CLAVIN-NERD. In particular, you must download the [GeoNames.org](http://www.geonames.org/) gazetteer and build the corresponding Lucene index for CLAVIN before using CLAVIN-NERD.

Once you've used CLAVIN to build the required Lucene index from the GeoNames.org gazetteer, consult `WorkflowDemoNERD.java` for an example of how to use CLAVIN-NERD. The main difference between using CLAVIN and CLAVIN-NERD is in the arguments passed to the `GeoParserFactory` class to instantiate a `GeoParser` object. With CLAVIN-NERD, we need to specify that we want to use the `StanfordExtractor` to extract location names from text.

Here's an example call to `GeoParserFactory` where we specify that the `StanfordExtractor` should be used, as seen in the `WorkflowDemoNERD` class:

    GeoParserFactory.getDefault("./IndexDirectory", new StanfordExtractor(), 1, 1, false);

**Don't forget:** Loading the worldwide gazetteer uses a non-trivial amount of memory. When using CLAVIN-NERD in your own programs, if you encounter `Java heap space` errors, bump up the maximum heap size for your JVM. Allocating 2GB (e.g., `-Xmx2g`) is a good place to start.

### Get it from Maven Central:


```xml
<dependency>
    <groupId>com.bericotech</groupId>
    <artifactId>clavin-nerd</artifactId>
    <version>1.0.0</version>
</dependency>
```

## License:

Since the Stanford CoreNLP NER library is licensed via the GPL, CLAVIN-NERD is as well. However, CLAVIN itself remains under the Apache License, version 2.

-------------------

CLAVIN-NERD
Copyright (C) 2012-2013 Berico Technologies

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
