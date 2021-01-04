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
 * StanfordExtractor.java
 *
 *###################################################################*/

package com.novetta.clavin.nerd;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.novetta.clavin.extractor.LocationExtractor;
import com.novetta.clavin.extractor.LocationOccurrence;

/**
 * Extracts location names from unstructured text documents using a
 * named entity recognizer (Stanford CoreNLP NER).
 *
 */
public class StanfordExtractor implements LocationExtractor {
    // the actual named entity recognizer (NER) object
    private AbstractSequenceClassifier<CoreMap> namedEntityRecognizer;
    
    /**
     * Default constructor. Instantiates a {@link StanfordExtractor}
     * with the standard English language model
     * 
     * @throws ClassCastException			Error by contract
     * @throws IOException					Error by contract
     * @throws ClassNotFoundException		Error by contract
     */
    public StanfordExtractor() throws IOException, ClassNotFoundException {
        this("english.all.3class.distsim.crf.ser.gz", "english.all.3class.distsim.prop" );
    }
    
    /**
     * Builds a {@link StanfordExtractor} by instantiating the 
     * Stanford NER named entity recognizer with a specified 
     * language model.
     * 
     * @param NERmodel                      path to Stanford NER language model
     * @param NERprop						path to property file for Stanford NER language model
     * @throws IOException 					Error by contract
     * @throws ClassNotFoundException 		Error by contract
     * @throws ClassCastException 			Error by contract
     */
    //@SuppressWarnings("unchecked")
    public StanfordExtractor(String NERmodel, String NERprop) throws IOException, ClassNotFoundException {
    	
    	InputStream mpis = this.getClass().getClassLoader().getResourceAsStream("models/" + NERprop);
    	Properties mp = new Properties();
    	mp.load(mpis);
    	namedEntityRecognizer = CRFClassifier.getClassifier("models/" + NERmodel, mp);
    }

    /**
     * Get extracted locations from a plain-text body.
     * 
     * @param text      Text content to perform extraction on.
     * @return          List of extracted Location Occurrences.
     */
    public List<LocationOccurrence> extractLocationNames(String text) {
        if (text == null)
            throw new IllegalArgumentException("text input to extractLocationNames should not be null");

        // extract entities as <Entity Type, Start Index, Stop Index>
        return convertNERtoCLAVIN(namedEntityRecognizer.classifyToCharacterOffsets(text), text);
    }

    /**
     * Converts output from Stanford NER to input required by CLAVIN resolver.
     *
     * @param entities  A List&lt;Triple&lt;String, Integer, Integer&gt;&gt; from Stanford NER
     * @param text      text content processed by Stanford NER + CLAVIN resolver
     * @return          List&lt;LocationOccurrence&gt; used by CLAVIN resolver
     */
    public static List<LocationOccurrence> convertNERtoCLAVIN
            (List<Triple<String, Integer, Integer>> entities, String text) {

        List<LocationOccurrence> locations = new ArrayList<>();

        if (entities != null) {
            // iterate over each entity Triple
            for (Triple<String, Integer, Integer> entity : entities) {
                // check if the entity is a "Location"
                if (entity.first.equalsIgnoreCase("LOCATION")) {
                    // build a LocationOccurrence object
                    locations.add(new LocationOccurrence(text.substring(entity.second, entity.third), entity.second, entity.third));
                }
            }
        }

        return locations;
    }
}
