package com.bericotech.clavin.nerd;

import java.io.File;
import java.util.List;

import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.bericotech.clavin.util.TextUtils;

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
     * how to use it.
     * 
     * @param args              not used
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
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
        
        // And we're done...
        System.out.println("\n\"That's all folks!\"");
    }
}
