/* Symmetries.java
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

import be.ugent.caagt.equi.PlanarGraph;
import be.ugent.caagt.perm.Perm;

import java.util.*;

/**
 * Represents the symmetries of a planar graph.
 */
public class Symmetries {

    public PlanarGraph getGraph() {
        return graph;
    }

    private PlanarGraph graph;

    private int groupOrder;

    private CombinatorialGroup group;

    public CombinatorialGroup getGroup() {
        return group;
    }

    /**
     * Return the order of the symmetry group.
     */
    public int getGroupOrder() {
        return groupOrder;
    }

    public Symmetries(PlanarGraph graph) {
        this.graph = graph;
        // find all labellings
        List<Labelling> labellings = new ArrayList<>();
        graph.sweepEdges(edge -> {
                    labellings.add(new Labelling(edge[0], edge[1], true));
                    labellings.add(new Labelling(edge[0], edge[1], false));
                    labellings.add(new Labelling(edge[1], edge[0], true));
                    labellings.add(new Labelling(edge[1], edge[0], false));
                }
        );

        // find labellings with the same code
        List<Labelling> sameCodes = new ArrayList<>();
        Labelling reference = labellings.get(0);
        for (Labelling labelling : labellings) {
            if (reference.hasSameCodeAs(labelling)) {
                sameCodes.add(labelling);
            }
        }

        groupOrder = sameCodes.size();

        // convert to permutations
        Perm[] permutations = new Perm[groupOrder];
        Perm inv = Perm.create(sameCodes.get(0).labels).inv();
        int pos = 0;
        for (Labelling labelling : sameCodes) {
            //permutations[pos] = inv.mul(Perm.create(labelling.labels));
            permutations[pos] = Perm.create(labelling.labels).mul(inv);
            pos++;
        }

        // compute permutation orders
        int[] orders = new int[permutations.length];
        for (int i = 0; i < permutations.length; i++) {
            orders[i] = permutations[i].order();
        }
        // compute order signature
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int order : orders) {
            if (map.containsKey(order)) {
                map.put(order, map.get(order) + 1);
            } else {
                map.put(order, 1);
            }
        }
        int[] signature = new int[map.size() * 2];
        pos = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            signature[pos] = entry.getKey();
            pos++;
            signature[pos] = entry.getValue();
            pos++;
        }

        group = resolve(graph.getOrder(), signature, permutations);

    }

    // represents a breadth first generated labelling of a graph
    private class Labelling {
        int[] labels; // labels for the various vertices. Labels start a 1. 0 means (as yet) unlabelled
        int[] code; // corresponding 'code'

        /* Construct a labelling of the graph, starting with the given directed edge and proceeding either
           in clockwise or anti-clockwise direction
          */
        public Labelling(int begin, int end, boolean clockwise) {

            int[] queue = new int[2 * graph.getOrder()];
            this.code = new int[2 * graph.getSize() + graph.getOrder()];
            Arrays.fill(code, -1);
            int codePos = 0;
            this.labels = new int[graph.getOrder()];
            Arrays.fill(labels, -1);

            int head = 0;
            queue[0] = begin;
            queue[1] = end;
            int tail = 2;

            labels[begin] = 0;

            while (tail > head) { // queue not empty
                begin = queue[head];
                head++;
                end = queue[head];
                head++;
                // find starting position among neighbours of end
                int neighbours[] = graph.getNeighbours(begin);
                int degree = neighbours.length;
                int p = 0;
                while (neighbours[p] != end) {
                    p++;
                }
                // run through all neighbours
                for (int i = 0; i < degree; i++) {
                    int pos = (clockwise ? p + i : degree + p - i) % degree;
                    int dest = neighbours[pos];
                    // mark and queue if this is a new vertex
                    int c = labels[dest];
                    if (c < 0) {
                        c = tail / 2;
                        labels[dest] = c;
                        queue[tail] = dest;
                        tail++;
                        queue[tail] = begin;
                        tail++; // push onto queue
                    }
                    code[codePos] = c;
                    codePos++;
                }
                codePos++;
            }
        }

        public boolean hasSameCodeAs(Labelling other) {
            return Arrays.equals(code, other.code);
        }
    }

    /* =================================
     * GROUP RESOLUTION
     * ================================= */

    // TODO: move to another class with signature and permutations as fields

    /**
     * Find an element of given order among the given permutations.
     */
    private static Perm elementOfOrder(int order, Perm[] perms) {
        for (Perm perm : perms) {
            if (perm.order() == order) {
                return perm;
            }
        }
        throw new IllegalStateException("Required group element not found");
    }

    /**
     * Find an element p of order n such that q.p has order m
     */
    private static Perm elementOfOrder(int n, Perm q, int m, Perm[] perms) {
        for (Perm p : perms) {
            if (p.order() == n && q.mul(p).order() == m) {
                return p;
            }
        }
        throw new IllegalStateException("Required group element not found");
    }

    /**
     * Try to resolve the given list of permutations as a cyclic group
     */
    private static CombinatorialGroup resolveCyclic(int[] signature, Perm[] permutations) {
        return null;
    }

    /**
     * Try to resolve the given list of permutations as a doubled cyclic group
     */
    private static CombinatorialGroup resolveDoubleCyclic(int degree, int[] signature, Perm[] permutations) {
        return null;
    }

    /**
     * Try to resolve the given list of permutations as a dihedral group
     */
    private static CombinatorialGroup resolveDihedral(int degree, int[] signature, Perm[] permutations) {
        return null;
    }

    /**
     * Try to resolve the given list of permutations as a doubled dihedral group
     */
    private static CombinatorialGroup resolveDoubleDihedral(int degree, int[] signature, Perm[] permutations) {
        return null;
    }

    /**
     * Return the combinatorial group that corresponds to the given list of permutations and signature
     */
    private static CombinatorialGroup resolve(int degree, int[] signature, Perm[] permutations) {

        // first the infinite series
        CombinatorialGroup group = resolveCyclic(signature, permutations);
        if (group == null) {
            group = resolveDihedral(degree, signature, permutations);
        }
        if (group == null) {
            group = resolveDoubleCyclic(degree, signature, permutations);
        }
        if (group == null) {
            group = resolveDoubleDihedral(degree, signature, permutations);
        }
        if (group == null) {
            // now the remaining cases
            switch (permutations.length) {
                case 120: {
                    Perm g5i = elementOfOrder(10, permutations);
                    Perm g3 = elementOfOrder(3, g5i.pow(6), 2, permutations);
                    group = new DoubleAlt5(degree, g5i, g3);
                    break;
                }
                case 60: {
                    Perm g5 = elementOfOrder(5, permutations);
                    Perm g3 = elementOfOrder(3, g5, 2, permutations);
                    group = new Alt5(degree, g5, g3);
                    break;
                }
                case 48: {
                    //group = new DoubleSym4(degree, );
                    break;
                }
                case 24:
                    if (signature[3] == 7) {
                        //group = new DoubleAlt4()degree, ;
                    } else { // == 9
                        //group = new Sym4(degree, );
                    }
                    break;
            }
        }

        return group;

    }

}
