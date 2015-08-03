/* WriteGraphOutputStream.java
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
 * Graph output stream which can write embedded graphs in 'write graph' format, either 2d or 3d
 */
public class WriteGraphOutputStream extends GraphOutputStream {

    private PrintWriter writer;

    public WriteGraphOutputStream(OutputStream out, int dimension) throws IOException {
        super(out);
        this.writer = new PrintWriter(new OutputStreamWriter(out, "US-ASCII"));
        writer.printf(">>writegraph%dd<<\n", dimension);
    }

    @Override
    public void writeGraph(EmbeddedPlanarGraph graph) throws IOException {
        int order = graph.getOrder();
        for (int i = 0; i < order; i++) {
            writer.printf("%4d", i+1);
            for (double c : graph.getCoordinates(i)) {
                writer.printf(Locale.US, " %9.7f", c);
            }
            for (int n : graph.getNeighbours(i)) {
                writer.printf(Locale.US, " %4d", n+1);
            }
            writer.println();
        }
        writer.println("0");
    }

    @Override
    public void close() throws IOException {
        writer.close();
        out.close();
    }
}
