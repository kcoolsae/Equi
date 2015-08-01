/* Realization.java
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

import be.ugent.caagt.equi.groups.Generator;
import be.ugent.caagt.perm.Perm;

import java.util.EnumMap;
import java.util.Map;

/**
 * A realisation of a combinatorial group (as a permutation group)
 */
public class Realization {

    private CombinatorialGroup group;

    public CombinatorialGroup getGroup() {
        return group;
    }

    public int getDegree() {
        return degree;
    }

    private int degree;

    /**
     * Create a partial realization of this group. Generators need still be set.
     */
    public Realization (CombinatorialGroup group, int degree) {
        this.group = group;
        this.degree = degree;
    }

    private Map<Generator,Perm> map = new EnumMap<> (Generator.class);

    public void setGenerator (Generator g, Perm p) {
        map.put(g,p);
    }

    public Perm getGenerator (Generator g) {
        return map.get(g);
    }

    public Iterable<Perm> getGenerators () {
        return map.values();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Realization of ").append(group).append("\n");
        for (Map.Entry<Generator, Perm> entry : map.entrySet()) {
            b.append(String.format("%6s: %s\n", entry.getKey(), entry.getValue()));
        }
        return b.toString();
    }
}
