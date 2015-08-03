/* WriteGraphInputStream.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright Ⓒ 2015 Universiteit Gent
 * 
 * This file is part of the Equi application
 * 
 * Corresponding author (see also file AUTHORS)
 * 
 * Kris Coolsaet
 * Department of Applied Mathematics, Computer Science and Statistics
 * Ghent University 
 * Krijgslaan 281-S9
 * B-9000 GENT Belgium
 * 
 * The Equi application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Equi Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Equi Application (file LICENSE in the distribution).  If not,
 * see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.io;

import be.ugent.caagt.equi.EmbeddedPlanarGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Graph input stream which can read write graph formats
 */
public class WriteGraphInputStream extends GraphInputStream {

    private BufferedReader reader;

    private int dimension;

    /**
     * Create a graph input stream backed up by the given input stream
     */
    public WriteGraphInputStream(InputStream in) throws IOException {
        super(in);
        try {
            this.reader = new BufferedReader(new InputStreamReader(in, "US-ASCII"));
            String headerLine = reader.readLine();
            if (headerLine == null ||
                    ! headerLine.startsWith(">>writegraph") ||
                    ! headerLine.endsWith("<<")) {
                throw new IOException("Missing or incorrect file header");
            }
            String typeString = headerLine.substring(12, 14);
            if (typeString.equals("2d")) {
                this.dimension = 2;
            } else if (typeString.equals("3d")) {
                this.dimension = 3;
            } else {
               throw new IOException("File header should specify 2d or 3d");
            }
        } catch (UnsupportedEncodingException e) {
            // this should not happen
            Logger.getLogger("be.ugent.caagt.equi").log(Level.SEVERE, "US-ASCII encoding not supported by JRE", e);
        }

    }
    private static class Info {
        public double[] coordinates ;
        public int[] neighbours;
        public Info (int dimension, int size) {
            coordinates = new double[dimension];
            neighbours = new int[size];
        }
    }

    @Override
    public EmbeddedPlanarGraph readGraph() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }

        // read next graph into list of Info objects
        List<Info> list = new ArrayList<> ();
        int count = 0;
        String parts[] = line.trim().split("\\s+");
        while (!parts[0].equals("0")) {
            if (Integer.parseInt(parts[0]) != count + 1) {
                throw new IOException ("Unexpected vertex number in file");
            }
            Info info = new Info(dimension, parts.length - dimension - 1);
            for (int i=0; i < dimension; i++) {
               info.coordinates[i] = Double.parseDouble(parts[i+1]);
            }
            for (int i=dimension+1; i < parts.length; i++) {
                info.neighbours[i-dimension-1] = Integer.parseInt(parts[i])-1;
            }
            list.add (info);
            count ++;
            line = reader.readLine();
            if (line == null) {
                throw new IOException("Premature end of file");
            }
            parts = line.trim().split("\\s+");
        }

        // convert to embedded graph
        double[][] coordinates = new double[count][];
        int[][] neighbours = new int[count][];
        for (int i = 0; i < count; i++) {
            coordinates[i] = list.get(i).coordinates;
            neighbours[i] = list.get(i).neighbours;
        }
        return new EmbeddedPlanarGraph(neighbours, coordinates);
    }
}
