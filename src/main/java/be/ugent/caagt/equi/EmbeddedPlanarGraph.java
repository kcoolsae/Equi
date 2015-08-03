/* EmbeddedPlanarGraph.java
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

package be.ugent.caagt.equi;

/**
 * Planar graph with an added embedding.
 */
public class EmbeddedPlanarGraph extends PlanarGraph {

    private double[][] coordinates;

    private static double[][] deepCopy (double[][] original) {
        double[][] result = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    // utility routine for use in constructors
    private void cloneCoordinates(double[][] coordinates) {
        for (double[] coordinate : coordinates) {
            if (coordinate.length != coordinates[0].length) {
                throw new IllegalArgumentException("Embedding has inconsistent dimension");
            }
        }
        this.coordinates = deepCopy(coordinates);
    }

    /**
     * Create a new planar graph with the given embedding.
     */
    public EmbeddedPlanarGraph(int[][] neighbours, double[][] coordinates) {
        super(neighbours);
        if (coordinates.length != neighbours.length) {
            throw new IllegalArgumentException("Embedding has incorrect number of coordinate entries");
        }
        cloneCoordinates(coordinates);

    }


    /**
     * Create a new planar graph with the given embedding.
     */
    public EmbeddedPlanarGraph(PlanarGraph original, double[][] coordinates) {
        super(original);
        if (coordinates.length != neighbours.length) {
            throw new IllegalArgumentException("Embedding has incorrect number of coordinate entries");
        }
        cloneCoordinates(coordinates);
    }

    /**
     * The dimension of the embedding.
     */
    public int getDimension() {
        return coordinates[0].length;
    }

    /**
     * Returns the coordinates of the vertex with given index. The result must be treated as immutable.
     */
    public double[] getCoordinates(int vertex) {
        return coordinates[vertex];
    }
}



