package com.bericotech.clavin.nerd;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

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
        assertEquals("incorrect entity extracted", "Bolivia", results.get(0).text);
        assertEquals("wrong position of entity", text.indexOf("Bolivia"), results.get(0).position);
    }

}
