/* Dih10.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Dihedral group of order 20 with point group realizations that use a special rotation axis of order 5.
 */
public class Dih10 extends AbstractCombinatorialGroup {

    private Perm g5i;

    private Perm g2;

    private Perm[] mirrors;

    public Dih10(int degree, Perm g5i, Perm g2) {
        super(20, degree);
        this.g5i = g5i;
        this.g2 = g2;
        mirrors = new Perm[]{g2, g2.mul(g5i)};
    }

    @Override
    public String toString() {
        return "Dih(10)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this,
                new Cyclic(degree, g5i) // TODO
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        for (int d : getDivisors(10)) {
            for (Perm m : mirrors) {
                list.add(new CombinedGroup("D" + num(5, d) + "d", order, degree, Arrays.asList(
                                new ExtendedPerm(g5i, PointGroupElement.ROT_5.minus()),
                                new ExtendedPerm(m, PointGroupElement.ROT_G2)
                        ))
                );
            }
        }
        for (int d : getDivisors(5)) {
            for (Perm m : mirrors) {
                list.add(new CombinedGroup("[D" + num(5, d) + "h]", order, degree,
                        Arrays.asList(ExtendedPerm.rotoreflection(g5i, 2 * d),
                                new ExtendedPerm(m, PointGroupElement.REFLECT_V))
                ));
            }
        }
        for (int d : getDivisors(10)) {
            for (Perm m : mirrors) {
                list.add(new CombinedGroup("[D" + num(10, d) + "]", order, degree,
                        Arrays.asList(ExtendedPerm.rotation(g5i, d),
                                new ExtendedPerm(m, PointGroupElement.ROT_G2STAR))
                ));
            }
        }
        for (int d : getDivisors(10)) {
            for (Perm m : mirrors) {
                list.add(new CombinedGroup("[C" + num(10, d) + "v]", order, degree,
                        Arrays.asList(ExtendedPerm.rotation(g5i, d),
                                new ExtendedPerm(m, PointGroupElement.REFLECT_V))
                ));
            }
        }
        return list;
    }
}
