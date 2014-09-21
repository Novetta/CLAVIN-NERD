package com.bericotech.clavin.nerd;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import com.bericotech.clavin.resolver.ClavinLocationResolver;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.bericotech.clavin.util.TextUtils;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.bericotech.clavin.nerd.StanfordExtractor.convertNERtoCLAVIN;

/*#####################################################################
 * 
 * CLAVIN-NERD
 * -----------
 * 
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 * 
 * ====================================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * ====================================================================
 * 
 * WorkflowDemoNERD.java
 * 
 *###################################################################*/

/**
 * Quick example showing how to use CLAVIN's capabilities.
 * 
 */
public class WorkflowDemoNERD {

    /**
     * Run this after installing & configuring CLAVIN to get a sense of
     * how to use it in a few different ways.
     * 
     * @param args              not used
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
    	getparseArticle();
    	geoparseUppercaseArticle();
        resolveStanfordEntities();
    	
        // And we're done...
        System.out.println("\n\"That's all folks!\"");
    }

    /**
     * Standard usage of CLAVIN. Instantiate a default GeoParser, give
     * it some text, check out the locations it extracts & resolves.
     *
     * @throws Exception
     */
    private static void getparseArticle() throws Exception {
        // Instantiate a CLAVIN GeoParser using the StanfordExtractor
        GeoParser parser = GeoParserFactory.getDefault("./IndexDirectory", new StanfordExtractor(), 1, 1, false);
        
        // Unstructured text file about Somalia to be geoparsed
        File inputFile = new File("src/test/resources/sample-docs/Somalia-doc.txt");
        
        // Grab the contents of the text file as a String
        String inputString = TextUtils.fileToString(inputFile);
        
        // Parse location names in the text into geographic entities
        List<ResolvedLocation> resolvedLocations = parser.parse(inputString);
        
        // Display the ResolvedLocations found for the location names
        for (ResolvedLocation resolvedLocation : resolvedLocations)
            System.out.println(resolvedLocation);
        
    }

    /**
     * Demonstrates usage of CLAVIN with non-default NER model -- in
     * this instance, a case-insensitive model to help us perform
     * geoparsing on a text document IN ALL CAPS.
     *
     * @throws Exception
     */
    private static void geoparseUppercaseArticle() throws Exception {
        // Instantiate a CLAVIN GeoParser using the StanfordExtractor with "caseless" models
        GeoParser parser = GeoParserFactory.getDefault("./IndexDirectory", new StanfordExtractor("english.all.3class.caseless.distsim.crf.ser.gz", "english.all.3class.caseless.distsim.prop"), 1, 1, false);
        
        // Unstructured uppercase text file about Somalia to be geoparsed
        File inputFile = new File("src/test/resources/sample-docs/Somalia-doc-uppercase.txt");
        
        // Grab the contents of the text file as a String
        String inputString = TextUtils.fileToString(inputFile);
        
        // Parse location names in the text into geographic entities
        List<ResolvedLocation> resolvedLocations = parser.parse(inputString);
        
        // Display the ResolvedLocations found for the location names
        for (ResolvedLocation resolvedLocation : resolvedLocations)
            System.out.println(resolvedLocation);
    }

    /**
     * Sometimes, you might already be using Stanford NER elsewhere in
     * your application, and you'd like to just pass the output from
     * Stanford NER directly into CLAVIN, without having to re-run the
     * input through Stanford NER just to use CLAVIN. This example
     * shows you how to very easily do exactly that.
     *
     * @throws IOException
     * @throws ClavinException
     */
    private static void resolveStanfordEntities() throws IOException, ClavinException {

        /*#####################################################################
         *
         * Start with Stanford NER -- no need to get CLAVIN involved for now.
         *
         *###################################################################*/

        // instantiate Stanford NER entity extractor
        InputStream mpis = WorkflowDemoNERD.class.getClassLoader().getResourceAsStream("models/english.all.3class.distsim.prop");
        Properties mp = new Properties();
        mp.load(mpis);
        AbstractSequenceClassifier<CoreMap> namedEntityRecognizer =
                CRFClassifier.getJarClassifier("/models/english.all.3class.distsim.crf.ser.gz", mp);

        // Unstructured text file about Somalia to be geoparsed
        File inputFile = new File("src/test/resources/sample-docs/Somalia-doc.txt");

        // Grab the contents of the text file as a String
        String inputString = TextUtils.fileToString(inputFile);

        // extract entities from input text using Stanford NER
        List<Triple<String, Integer, Integer>> entitiesFromNER = namedEntityRecognizer.classifyToCharacterOffsets(inputString);

        /*#####################################################################
         *
         * Now, CLAVIN comes into play...
         *
         *###################################################################*/

        // convert Stanford NER output to ClavinLocationResolver input
        List<LocationOccurrence> locationsForCLAVIN = convertNERtoCLAVIN(entitiesFromNER, inputString);

        // instantiate the CLAVIN location resolver
        ClavinLocationResolver clavinLocationResolver = new ClavinLocationResolver(new LuceneGazetteer(new File("./IndexDirectory")));

        // resolve location entities extracted from input text
        List<ResolvedLocation> resolvedLocations = clavinLocationResolver.resolveLocations(locationsForCLAVIN, 1, 1, false);

        // Display the ResolvedLocations found for the location names
        for (ResolvedLocation resolvedLocation : resolvedLocations)
            System.out.println(resolvedLocation);
    }
}
