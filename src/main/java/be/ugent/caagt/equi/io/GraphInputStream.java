/* GraphInputStream.java
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a (list of) embedded planar graphs. In some cases the embedding may be trivial, i.e., every vertex has zero coordinates.
 */
public abstract class GraphInputStream extends FilterInputStream {

    /**
     * Create a graph input stream backed up by the given input stream
     */
    protected GraphInputStream(InputStream in) {
        super(in);
    }

    /**
     * Returns the next graph in the stream, or null when end of file was reached.
     */
    public abstract EmbeddedPlanarGraph readGraph() throws IOException;
}
