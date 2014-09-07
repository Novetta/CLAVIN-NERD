package com.bericotech.clavin.nerd;

import com.bericotech.clavin.extractor.LocationExtractor;
import com.bericotech.clavin.extractor.LocationOccurrence;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
     * @throws ClassCastException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public StanfordExtractor() throws ClassCastException, IOException, ClassNotFoundException {
        this("english.all.3class.distsim.crf.ser.gz", "english.all.3class.distsim.prop" );
    }
    
    /**
     * Builds a {@link StanfordExtractor} by instantiating the 
     * Stanford NER named entity recognizer with a specified 
     * language model.
     * 
     * @param NERmodel                      path to Stanford NER language model
     * @param NERprop						path to property file for Stanford NER language model
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws ClassCastException 
     */
    //@SuppressWarnings("unchecked")
    public StanfordExtractor(String NERmodel, String NERprop) throws IOException, ClassCastException, ClassNotFoundException {
    	
    	InputStream mpis = this.getClass().getClassLoader().getResourceAsStream("models/" + NERprop);
    	Properties mp = new Properties();
    	mp.load(mpis);
       	
    	namedEntityRecognizer = CRFClassifier.getJarClassifier("/models/" + NERmodel, mp);
    }

    /**
     * Get extracted locations from a plain-text body.
     * 
     * @param text      Text content to perform extraction on.
     * @return          List of Location Occurrences.
     */
    public List<LocationOccurrence> extractLocationNames(String text) {
        if (text == null)
            throw new IllegalArgumentException("text input to extractLocationNames should not be null");

        List<LocationOccurrence> extractedLocations = new ArrayList<LocationOccurrence>();

        // extract entities as <Entity Type, Start Index, Stop Index>
        List<Triple<String, Integer, Integer>> extractedEntities = 
                namedEntityRecognizer.classifyToCharacterOffsets(text);

        if (extractedEntities != null) {
            // iterate over each entity Triple
            for (Triple<String, Integer, Integer> extractedEntity : extractedEntities) {
                // check if the entity is a "Location"
                if (extractedEntity.first.equalsIgnoreCase("LOCATION")) {
                    // build a LocationOccurrence object
                    extractedLocations.add(new LocationOccurrence(
                            text.substring(extractedEntity.second(), extractedEntity.third()),
                            extractedEntity.second()));
                }
            }
        }

        return extractedLocations;
    }
}
