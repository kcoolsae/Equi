/* Dih6.java
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

import be.ugent.caagt.perm.Perm;

import java.util.Arrays;

/**
 * Dihedral group of order 12 with point group realizations that use a special rotation axis of order 3
 */
public class Dih6 extends AbstractCombinatorialGroup {

    private Perm g3i;

    private Perm g2prime;

    public Dih6(int degree, Perm g3i, Perm g2prime) {
        super(12, degree);
        this.g3i = g3i;
        this.g2prime = g2prime;
    }

    @Override
    public String toString() {
        return "2Sym(3)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        Perm i = g3i.pow(3);
        Perm g3 = g3i.mul(i);
        return Arrays.asList(
                new Z6(degree, g3i),
                new Sym3Ico(degree, g3, g2prime),
                new Squared2(degree, g2prime, i),
                new Z3(degree, g3),
                new Z2(degree, i)
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
           new CombinedGroup("D3d", order, degree, Arrays.asList(
                   new ExtendedPerm(g3i, PointGroupElement.ROT_3.minus()),
                   new ExtendedPerm(g2prime, PointGroupElement.REFLECT_PHI)
           ))
        );
    }
}
