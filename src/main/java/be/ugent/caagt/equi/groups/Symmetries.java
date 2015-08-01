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
 * The Degage Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Degage Web Application (file LICENSE in the
 * distribution).  If not, see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.groups;

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

    private int[] signature;

    private CombinatorialGroup group;

    private Realization realization;

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
        Perm inv = Perm.create (sameCodes.get(0).labels).inv();
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
        SortedMap<Integer,Integer> map = new TreeMap<>();
        for (int order : orders) {
            if (map.containsKey(order)) {
                map.put(order,map.get(order) + 1);
            } else {
                map.put(order, 1);
            }
        }
        signature = new int[map.size()*2];
        pos = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            signature[pos] = entry.getKey();
            pos ++;
            signature[pos] = entry.getValue();
            pos ++;
        }

        // determine point group
        // first the infinite series
        group = Cyclic.fromSignature(groupOrder, signature);
        if (group == null) {
            group = Dihedral.fromSignature(groupOrder, signature);
        }
        if (group == null) {
            group = DoubleCyclic.fromSignature(groupOrder, signature);
        }
        if (group == null) {
            group = DoubleDihedral.fromSignature(groupOrder, signature);
        }
        // now the remaining cases
        switch (groupOrder) {
            case 120:
                group = new DoubleAlt5 ();
                break;
            case 60:
                group = new Alt5 ();
                break;
            case 48:
                group = new DoubleSym4 ();
                break;
            case 24:
                if (signature[3] == 7) {
                    group = new DoubleAlt4();
                } else { // == 9
                    group = new Sym4();
                }
        }
        //
        this.realization = group.findRealization(permutations, graph.getOrder());
    }

    // represents a breadth first generated labelling of a graph
    private class Labelling {
        int[] labels; // labels for the various vertices. Labels start a 1. 0 means (as yet) unlabelled
        int[] code; // corresponding 'code'

        /* Construct a labelling of the graph, starting with the given directed edge and proceeding either
           in clockwise or anti-clockwise direction
          */
        public Labelling (int begin, int end, boolean clockwise) {

            int[] queue =  new int[2*graph.getOrder()];
            this.code = new int[2*graph.getSize() + graph.getOrder()];
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
                head ++;
                // find starting position among neighbours of end
                int neighbours[] = graph.getNeighbours(begin);
                int degree = neighbours.length;
                int p=0;
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
                    codePos ++;
                }
                codePos ++;
            }
        }

        public boolean hasSameCodeAs (Labelling other) {
            return Arrays.equals(code, other.code);
        }
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (group != null) {
            b.append(String.format("Group %s (order=%d)\n", group, groupOrder));
        } else {
            b.append(String.format("Unknown group (order=%d)\n", groupOrder));
            b.append("  order signature =");
            for (int i = 0; i < signature.length; i += 2) {
                b.append(" ").append(signature[i]);
                if (signature[i + 1] > 1) {
                    b.append("^").append(signature[i + 1]);
                }
            }
            b.append("\n");
        }
        return b.toString();
    }

    public Realization getRealization() {
        return realization;
    }
}
