/* SpinputOutputStream.java
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
 * The Degage Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Degage Web Application (file LICENSE in the
 * distribution).  If not, see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.io;

import be.ugent.caagt.equi.EmbeddedPlanarGraph;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Outputs a planar graph in spinput format
 */
public class SpinputOutputStream extends GraphOutputStream {

    // Code based on SpinputWriter of in Cage - Nico Van Cleemput

    private PrintWriter writer;

    private int atomNumber;

    private String atomName;

    private double scaleFactor;

    public SpinputOutputStream(OutputStream out) throws IOException {
        super(out);

        this.writer = new PrintWriter(new OutputStreamWriter(out, "US-ASCII"));
        atomNumber = 2;
        atomName = "H"; // TODO: retrieve from table
        scaleFactor = 1.4;

    }

    @Override
    public void writeGraph(EmbeddedPlanarGraph graph) throws IOException {
        writer.print("\n\n0 1 \n");
        // vertices
        int order = graph.getOrder();
        for (int i = 0; i < order; i++) {
            writer.printf("\t%d", atomNumber);
            for (double c : graph.getCoordinates(i)) {
                writer.printf(Locale.US, "\t%9.7f", c*scaleFactor);
            }
            writer.println();
        }
        // labels
        writer.print("ENDCART\nATOMLABELS\n");
        for (int i = 0; i < order; i++) {
            writer.printf("\"%s%d\"\n", atomName, i + 1);
        }
        writer.print("ENDATOMLABELS\nHESSIAN\n");
        for (int i = 1; i <= order; i++) {
            writer.print("\t0");
            if (i % 12 == 0) {
                writer.println();
            }
        }
        if (graph.getSize() % 12 != 0) {
            writer.println();
        }
        // edges
        for (int i = 0; i < order; i++) {
            for (int n : graph.getNeighbours(i)) {
                if (i < n) {
                    writer.printf("\t%d\t%d\t1\n", i+1, n+1);
                }
            }
        }
        writer.printf("ENDHESS\n");
    }

    @Override
    public void close() throws IOException {
        writer.close();
        out.close();
    }
}
