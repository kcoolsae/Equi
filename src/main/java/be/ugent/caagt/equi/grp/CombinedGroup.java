/* CombinedGroup.java
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

package be.ugent.caagt.equi.grp;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of extended permutations representing the combination of a point group and its
 * permutation action on a set of points.
 */
public class CombinedGroup {

    private String caption;

    /**
     * Name of the corresponding point group
     */
    public String getCaption() {
        return caption;
    }

    private ExtendedPerm[] elements;

    /**
     * Trivial group
     */
    public CombinedGroup(String caption) {
        this.elements = new ExtendedPerm[0];
        this.caption = caption;
    }

    /**
     * Create the group generated by the given extended permutations
     */
    public CombinedGroup(String caption, int order, int degree, Iterable<ExtendedPerm> generators) {
        this.elements = new ExtendedPerm[order];
        this.caption = caption;

        // generate group
        elements = new ExtendedPerm[order];
        elements[0] = new ExtendedPerm();
        int head = 0;
        int tail = 1;
        while (tail > head) {  // queue not empty
            ExtendedPerm x = elements[head];
            head++;
            for (ExtendedPerm g : generators) {
                ExtendedPerm y = x.mul(g);
                // check whether already in elements array
                int pos = 0;
                while (pos < tail && !elements[pos].sameAs(y)) {
                    pos++;
                }
                if (pos == tail) {
                    elements[tail] = y;
                    tail++;
                }
            }
        }

        // compute point orbit sizes and representatives
        this.pointRepresentatives = new ArrayList<>();
        boolean[] visited = new boolean[degree];
        for (int i = 0; i < degree; i++) {
            if (!visited[i]) {
                pointRepresentatives.add(i);
                int count = 0;
                for (ExtendedPerm element : elements) {
                    int j = element.perm.image(i);
                    if (!visited[j]) {
                        visited[j] = true;
                        count ++;
                    }
                }
            }
        }

    }

    private List<Integer> pointRepresentatives;

    /**
     * Compute the coordinates of the list of points in the orbit of the given point, even if the original
     * coordinate is not stabilized by the stabilizer of the given point. Assumes the list contains
     * zero coordinates at the orbit point positions.
     */
    private void symmetrize(int pt, double[] coord) {

        // compute reference coordinate for this point
        double[] ref = new double[3];
        for (ExtendedPerm el : elements) {
            int from = el.invPerm.image(pt);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ref[j] += coord[3*from+i] * el.mat.getMatrix()[i][j];
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            ref[i] /= elements.length;
        }

        // extend to entire orbit
        for (ExtendedPerm el : elements) {
            int pos = el.perm.image(pt);
            for (int j = 0; j < 3; j++) {
                coord[3*pos + j] = 0;
                for (int i = 0; i < 3; i++) {
                    coord[3*pos+j] += ref[i]*el.mat.getMatrix()[i][j];
                }
            }
        }
    }

    /**
     * Takes a list of coordinates and makes it symmetric with respect to this group. Coordinates
     * are given in the order x0,y0,z0, x1, y1, z1, ,,,
     */
    public void symmetrize(double[] coordinates) {
        if (elements.length <= 1) {
            return; // trivial group
        }
        for (int pointRepresentative : pointRepresentatives) {
            symmetrize(pointRepresentative, coordinates);
        }
    }

}
