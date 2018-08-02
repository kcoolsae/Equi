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
 * Dihedral group of order 8
 */
public class Dih4 extends AbstractCombinatorialGroup {

    private Perm g4;

    private Perm ri;

    public Dih4(int degree, Perm g4, Perm ri) {
        super(8, degree);
        this.g4 = g4;
        this.ri = ri;
    }

    @Override
    public String toString() {
        return "Dih(4)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                new Cyclic(degree, g4) // TODO
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("D4", order, degree, Arrays.asList(
                        new ExtendedPerm(g4, PointGroupElement.ROT_G4),
                        new ExtendedPerm(ri, PointGroupElement.REFLECT_R.minus())
                )),
                new CombinedGroup("C4v", order, degree, Arrays.asList(
                        new ExtendedPerm(g4, PointGroupElement.ROT_G4),
                        new ExtendedPerm(ri, PointGroupElement.REFLECT_R)
                )),
                new CombinedGroup("D2d", order, degree, Arrays.asList(
                        new ExtendedPerm(g4, PointGroupElement.ROT_G4.minus()),
                        new ExtendedPerm(ri, PointGroupElement.REFLECT_R)
                ))
        );
    }
}
