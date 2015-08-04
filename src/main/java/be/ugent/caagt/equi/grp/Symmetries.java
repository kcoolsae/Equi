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

    // TODO: move to another class with signature and permutations as fields AND permutation orders...

    /**
     * Is this an involution that commutes with every element of the group?
     */
    private static boolean isCentralInvolution(Perm g, Perm[] perms) {
        if (g.order() == 2) {
            for (Perm perm : perms) {
                if (!perm.mul(g).equals(g.mul(perm))){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

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
    private static CombinatorialGroup resolveCyclic(int degree, int[] signature, Perm[] permutations) {
        if (signature[signature.length - 2] == permutations.length) {
            return new Cyclic(degree, elementOfOrder(permutations.length, permutations));
        } else {
            return null;
        }
    }

    /**
     * Try to resolve the given list of permutations as a doubled cyclic group
     */
    private static CombinatorialGroup resolveDoubleCyclic(int degree, int[] signature, Perm[] permutations) {
        if (permutations.length % 4 != 0) {
            return null;  // cyclic should not be recognized as double cyclic
        } else {
            int halfOrder = permutations.length / 2;
            if (signature[signature.length - 2] == halfOrder && signature[3] == 3) {
                Perm g = elementOfOrder(halfOrder, permutations);
                Perm invol = elementOfOrder(2, g.pow(halfOrder / 2), 2, permutations);
                return new DoubleCyclic(degree, g, invol);
            } else {
                return null;
            }
        }
    }

    /**
     * Try to resolve the given list of permutations as a dihedral group
     */
    private static CombinatorialGroup resolveDihedral(int degree, int[] signature, Perm[] permutations) {
        int halfOrder = permutations.length / 2;
        if (signature[signature.length - 2] == halfOrder && signature[3] == halfOrder + (1 - halfOrder % 2)) {
            Perm g = elementOfOrder(halfOrder, permutations);
            Perm m = elementOfOrder(2, g, 2, permutations);
            return new Dihedral(degree, g, m);
        } else {
            return null;
        }
    }

    /**
     * Try to resolve the given list of permutations as a doubled dihedral group
     */
    private static CombinatorialGroup resolveDoubleDihedral(int degree, int[] signature, Perm[] permutations) {
        if (permutations.length % 8 != 0) {
            return null;
        } else {
            int quarterOrder = permutations.length / 4;
            if (signature[signature.length - 2] == quarterOrder && signature[3] == 2 * quarterOrder + 1 + 2 * (1 - quarterOrder % 2)) {
                Perm g = elementOfOrder(quarterOrder, permutations);
                Perm h = g.pow(quarterOrder / 2); // involution that belongs to the cyclic subgroup
                int pos = 1;
                while (!permutations[pos].equals(h) && !isCentralInvolution(permutations[pos], permutations)) {
                    pos++;
                }
                Perm g2 = permutations[pos];
                pos = 1;
                while (permutations[pos].order() != 2 ||
                       isCentralInvolution(permutations[pos], permutations)) {
                    pos++;
                }
                Perm g3 = permutations[pos];
                return new DoubleDihedral(degree, g, g3, g2);
            } else {
                return null;
            }
        }
    }

    /**
     * Return the combinatorial group that corresponds to the given list of permutations and signature
     */
    private static CombinatorialGroup resolve(int degree, int[] signature, Perm[] permutations) {

        if (permutations.length == 1) {
            return new Trivial(degree);
        }

        // first the infinite series
        CombinatorialGroup group = resolveCyclic(degree, signature, permutations);
        if (group == null) {
            group = resolveDihedral(degree, signature, permutations);
        }
        if (group == null) {
            group = resolveDoubleCyclic(degree, signature, permutations);
        }
        if (permutations.length == 8 && signature[3] == 7) {
            // special case for double dihedral
            Perm a = permutations[1];
            Perm b = permutations[2];
            Perm ab = a.mul(b);
            int pos = 3;
            while (!permutations[pos].equals(ab)) {
                pos ++;
            }
            group = new Cubed2(degree, a, b, permutations[pos]);
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
                    Perm g3i = elementOfOrder(6, permutations);
                    Perm g4 = elementOfOrder(4, g3i.mul(g3i), 2, permutations);
                    group = new DoubleSym4(degree, g3i, g4);
                    break;
                }
                case 24:
                    if (signature[3] == 7) {
                        Perm g3i = elementOfOrder(6, permutations);
                        Perm g2 = elementOfOrder(2, g3i.pow(3), 6, permutations);
                        group = new DoubleAlt4(degree, g3i, g2);
                    } else { //if (signature[3] == 9)
                        Perm g3 = elementOfOrder(3, permutations);
                        Perm g4 = elementOfOrder(4, g3, 2, permutations);
                        group = new Sym4(degree, g3, g4);
                    }
                    break;
                case 12:
                    if (signature[3] == 3 && signature[5] == 8) {
                        Perm g3 = elementOfOrder(3, permutations);
                        Perm g2 = elementOfOrder(2, permutations);
                        group = new Alt4(degree, g3, g2);
                    }
                    break;
            }
        }
        return group;

    }

}
