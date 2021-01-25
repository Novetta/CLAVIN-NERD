package com.novetta.clavin.nerd;

import static com.novetta.clavin.nerd.StanfordExtractor.convertNERtoCLAVIN;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;
import org.junit.Test;

import com.novetta.clavin.extractor.LocationOccurrence;

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
 * StanfordExtractorTest.java
 * 
 *###################################################################*/

/**
 * Checks output produced by named entity recognizer (NER), supplied
 * by Stanford CoreNLP NER as the location extractor for CLAVIN.
 * 
 */
public class StanfordExtractorTest {

    /**
     * Ensures we're getting good responses from the
     * {@link StanfordExtractor}.
     * 
     * @throws ClassCastException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testExtractLocationNames() throws ClassCastException, IOException, ClassNotFoundException {
        StanfordExtractor extractor = new StanfordExtractor();
        String text = "I went to Bolivia last week.";
        List<LocationOccurrence> results = extractor.extractLocationNames(text);
        assertEquals("wrong number of entities extracted", 1, results.size());
        testLocationOccurancePosition(text, "Bolivia", results.get(0));
    }

    /**
     * Checks conversion of Stanford NER output format into
     * {@link com.novetta.clavin.resolver.ClavinLocationResolver}
     * input format.
     *
     * @throws IOException
     * @throws ClassNotFoundException 
     * @throws ClassCastException 
     */
    @Test
    public void testConvertNERtoCLAVIN() throws IOException, ClassCastException, ClassNotFoundException {
        InputStream mpis = this.getClass().getClassLoader().getResourceAsStream("models/english.all.3class.distsim.prop");
        Properties mp = new Properties();
        mp.load(mpis);
        AbstractSequenceClassifier<CoreMap> namedEntityRecognizer =
                CRFClassifier.getClassifier("models/english.all.3class.distsim.crf.ser.gz", mp);

        String text = "I was born in Springfield and grew up in Boston.";
        List<Triple<String, Integer, Integer>> entitiesFromNER = namedEntityRecognizer.classifyToCharacterOffsets(text);

        List<LocationOccurrence> locationsForCLAVIN = convertNERtoCLAVIN(entitiesFromNER, text);
        assertEquals("wrong number of entities", 2, locationsForCLAVIN.size());
        testLocationOccurancePosition(text, "Springfield", locationsForCLAVIN.get(0));
        testLocationOccurancePosition(text, "Boston", locationsForCLAVIN.get(1));
    }
    
    
    private void testLocationOccurancePosition(String text, String expectedString, LocationOccurrence match) {
    	int expectedStartOffset = text.indexOf(expectedString);
    	int expectedEndOffset = expectedStartOffset + expectedString.length();
    	assertEquals("incorrect entity extracted", expectedString, match.getText());
    	assertEquals("wrong start position of entity", expectedStartOffset, match.getStartOffset());
    	assertEquals("wrong start position of entity", expectedEndOffset, match.getEndOffset());
    }
}
