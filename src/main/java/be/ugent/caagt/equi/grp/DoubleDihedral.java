/* DoubleDihedral.java
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
import java.util.Collections;

/**
 * Product of a dihedral group and a central involution.
 */
public class DoubleDihedral extends AbstractCombinatorialGroup {

    private Perm gen;

    private Perm central;

    private Perm mirror;

    private Perm otherCentral;

    public DoubleDihedral(int degree, Perm gen, Perm mirror, Perm central) {
        super(4 * gen.order(), degree);
        this.gen = gen;
        this.central = central;
        this.mirror = mirror;
        this.otherCentral = gen.pow(order / 8).mul(central);
    }

    @Override
    public String toString() {
        return "2.Dih(" + order / 4 + ")";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this,
                new Dihedral(degree, gen, mirror),
                new DoubleCyclic(degree, gen, central),
                new Cyclic(degree, gen)
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        if (order % 8 == 0) {
            int quarterOrder = order / 4;
            for (int d : getDivisors(quarterOrder)) {
                list.add(new CombinedGroup("D" + num(quarterOrder, d) + "h", order, degree,
                        Arrays.asList(ExtendedPerm.rotation(gen, d),
                                new ExtendedPerm(mirror, PointGroupElement.REFLECT_V),
                                new ExtendedPerm(central, PointGroupElement.MINUS_ONE))
                ));
                list.add(new CombinedGroup("D" + num(quarterOrder, d) + "h'", order, degree,
                        Arrays.asList(ExtendedPerm.rotation(gen, d),
                                new ExtendedPerm(mirror, PointGroupElement.REFLECT_V),
                                new ExtendedPerm(otherCentral, PointGroupElement.MINUS_ONE))
                ));
            }
        }
        return list;
    }
}
