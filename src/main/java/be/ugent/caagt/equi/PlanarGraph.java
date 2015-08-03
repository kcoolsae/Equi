/* PlanarGraph.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a planar graph.
 */
public class PlanarGraph {

    protected int[][] neighbours;

    private int[][] edges;

    private int[][] faces;

    private int numberOfEdges;

    private static int[][] deepCopy (int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    /**
     * Create a new planar graph.
     *
     * @param neighbours For every vertex, a clockwise ordered array of all adjacent vertices. Vertices are numbered
     *                   from 0 onwards.
     */
    public PlanarGraph(int[][] neighbours) {

        this.neighbours = deepCopy(neighbours);

        int total = 0;
        for (int[] neighbour : neighbours) {
            total += neighbour.length;
        }
        this.numberOfEdges = total / 2;

        computeEdges ();
        computeFaces();

    }

    /**
     * Create a new planar graph as a (deep) copy of the original.
     */
    public PlanarGraph (PlanarGraph original) {
        numberOfEdges = original.numberOfEdges;
        neighbours = deepCopy(original.neighbours);
        faces = deepCopy (original.faces);
    }

    private void computeFaces() {
        boolean[][] marks = new boolean[getOrder()][getOrder()];
        List<int[]> result = new ArrayList<>();
        for (int vertex = 0; vertex < getOrder(); vertex++) {
            for (int neighbour : neighbours[vertex]) {
                if (!marks[vertex][neighbour]) {
                    // new face
                    List<Integer> vertices = new ArrayList<>();
                    int initial = vertex;
                    do {
                        marks[vertex][neighbour] = true;
                        vertices.add(vertex);
                        // find this edge among the neighbours of 'neighbour'
                        int[] array = neighbours[neighbour];
                        int index = 0;
                        while (index < array.length && array[index] != vertex) {
                            index++;
                        }
                        if (index == array.length) {
                            throw new IllegalArgumentException("Neighbour list not symmetric");
                        }
                        vertex = neighbour;
                        neighbour = array[(index + 1) % array.length];
                    } while (vertex != initial);
                    int[] vertexArray = new int[vertices.size()];
                    for (int i = 0; i < vertexArray.length; i++) {
                        vertexArray[i] = vertices.get(i);
                    }
                    result.add(vertexArray);
                }

            }
        }

        this.faces = result.toArray(new int[result.size()][]);
    }

    private void computeEdges() {
        List<int[]> result = new ArrayList<>();
            for (int vertex = 0; vertex < getOrder(); vertex++) {
            for (int neighbour : neighbours[vertex]) {
                if (vertex < neighbour) {
                    result.add(new int[] {vertex, neighbour});
                }
            }
        }
        this.edges = result.toArray(new int[result.size()][]);
    }

    /**
     * Number of vertices
     */
    public int getOrder() {
        return neighbours.length;
    }

    /**
     * Number of edges
     */
    public int getSize() {
        return numberOfEdges;
    }

    /**
     * Return the edge with the given index. The result should be treated as immutable.
     */
    public int[] getEdge(int index) {
        return edges[index];
    }

    /**
     * Number of faces
     */
    public int getNumberOfFaces() {
        return faces.length;
    }

    /**
     * Return all neighbours of a vertex. The result must be treated as immutable.
     */
    public int[] getNeighbours (int vertex) {
        return neighbours[vertex];
    }

    /**
     * Sweep over all edges.
     * @param consumer Acts on an edge, given as an  array of two vertex indices, the first smaller than the second
     */
    public void sweepEdges(Consumer<int[]> consumer) {
        for (int[] edge : edges) {
             consumer.accept(edge);
        }
    }

    /**
     * Sweep over all faces.
     * @param consumer Acts on a face, given as an ordered array of vertex indices. This array must be treated as
     *                 immutable!
     */
    public void sweepFaces (Consumer<int[]> consumer) {
        for (int[] face : faces) {
            consumer.accept(face);
        }
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
