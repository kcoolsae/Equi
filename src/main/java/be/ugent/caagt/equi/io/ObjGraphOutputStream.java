/* ObjGraphOutputStream.java
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Output planar graphs to obj format
 */
public class ObjGraphOutputStream extends GraphOutputStream {


    private PrintWriter writer;

    public ObjGraphOutputStream (OutputStream out) throws IOException {
        super (out);
        this.writer = new PrintWriter(new OutputStreamWriter(out, "US-ASCII"));

    }


    @Override
    public void writeGraph(EmbeddedPlanarGraph graph) throws IOException {
        writer.println("# Created by EQUI\n#\n#Vertex coordinates");

        int order = graph.getOrder();
        for (int i = 0; i < order; i++) {
            writer.print("v ");
            for (double c : graph.getCoordinates(i)) {
                writer.printf(Locale.US, " %17.15f", c);
            }
            writer.println();
        }

        writer.println("# faces");
        graph.sweepFaces( face -> {
                    writer.print("f ");
                    for (int v : face) {
                        writer.printf(" %d", v + 1);
                    }
                    writer.println();
                } );

    }

    @Override
    public void close() throws IOException {
        writer.close();
        out.close();
    }
}
