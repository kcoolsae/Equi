/* Dihedral.java
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

package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Dihedral group.
 */
public class Dihedral extends AbstractCombinatorialGroup {

    private Perm gen;

    private Perm mirror;

    public Dihedral(int degree, Perm gen, Perm mirror) {
        super(2 * gen.order(), degree);
        this.gen = gen;
        this.mirror = mirror;
    }

    @Override
    public String toString() {
        return "Dih(" + order / 2 + ")";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                new Cyclic(degree, gen)
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        for (int d : getDivisors(order / 2)) {
            list.add(new CombinedGroup("D" + num(order / 2, d), order, degree,
                    Arrays.asList(ExtendedPerm.rotation(gen, d),
                            new ExtendedPerm(mirror, PointGroupElement.ROT_D))
            ));
        }
        for (int d : getDivisors(order / 2)) {
            list.add(new CombinedGroup("C" + num(order / 2, d) + "v", order, degree,
                    Arrays.asList(ExtendedPerm.rotation(gen, d),
                            new ExtendedPerm(mirror, PointGroupElement.REFLECT_V))
            ));
        }
        if (order % 4 == 0) {
            for (int d : getDivisors(order / 2)) {
                list.add(new CombinedGroup("D" + order / 4 + "d", order, degree,
                        Arrays.asList(ExtendedPerm.rotoreflection(gen, d),
                            new ExtendedPerm(mirror, PointGroupElement.REFLECT_V))
                ));
            }
        }
        if (order % 8 == 4) {
            for (int d : getDivisors(order / 4)) {
                list.add(new CombinedGroup("D" + order / 4 + "h", order, degree,
                        Arrays.asList(ExtendedPerm.rotoreflection(gen, 2*d),
                            new ExtendedPerm(mirror, PointGroupElement.REFLECT_V))
                ));
            }
        }
        return list;
    }
}
