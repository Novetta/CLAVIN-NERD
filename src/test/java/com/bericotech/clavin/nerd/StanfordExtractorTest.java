package com.bericotech.clavin.nerd;

import static com.bericotech.clavin.nerd.StanfordExtractor.convertNERtoCLAVIN;
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

import com.bericotech.clavin.extractor.LocationOccurrence;

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
        assertEquals("incorrect entity extracted", "Bolivia", results.get(0).getText());
        assertEquals("wrong position of entity", text.indexOf("Bolivia"), results.get(0).getPosition());
    }

    /**
     * Checks conversion of Stanford NER output format into
     * {@link com.bericotech.clavin.resolver.ClavinLocationResolver}
     * input format.
     *
     * @throws IOException
     */
    @Test
    public void testConvertNERtoCLAVIN() throws IOException {
        InputStream mpis = this.getClass().getClassLoader().getResourceAsStream("models/english.all.3class.distsim.prop");
        Properties mp = new Properties();
        mp.load(mpis);
        AbstractSequenceClassifier<CoreMap> namedEntityRecognizer =
                CRFClassifier.getJarClassifier("/models/english.all.3class.distsim.crf.ser.gz", mp);

        String text = "I was born in Springfield and grew up in Boston.";
        List<Triple<String, Integer, Integer>> entitiesFromNER = namedEntityRecognizer.classifyToCharacterOffsets(text);

        List<LocationOccurrence> locationsForCLAVIN = convertNERtoCLAVIN(entitiesFromNER, text);
        assertEquals("wrong number of entities", 2, locationsForCLAVIN.size());
        assertEquals("wrong text for first entity", "Springfield", locationsForCLAVIN.get(0).getText());
        assertEquals("wrong position for first entity", 14, locationsForCLAVIN.get(0).getPosition());
        assertEquals("wrong text for second entity", "Boston", locationsForCLAVIN.get(1).getText());
        assertEquals("wrong position for second entity", 41, locationsForCLAVIN.get(1).getPosition());
    }

}
