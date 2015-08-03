/* DoubleAlt5.java
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
 * Combinatorial group of type 2.Alt(5).
 */
public class DoubleAlt5 extends AbstractCombinatorialGroup {

    private Perm g5i;

    private Perm g3;

    /**
     * Alternating group of order 5 generated by 'canonical' elements g5,g3.
     */
    public DoubleAlt5(int degree, Perm g5i, Perm g3) {
        super(120, degree);
        this.g5i = g5i;
        this.g3 = g3;
    }

    @Override
    public String toString() {
        return "2.Alt(5)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        Perm i = g5i.pow(5);
        Perm g5 = g5i.mul(i);
        Perm g3inv = g3.inv();
        Perm g2 = g3inv.mul(g5.pow(3)).mul(g3).mul(g5.pow(2)).mul(g3inv);
        Perm g2prime = g2.conj(g5.inv());
        return Arrays.asList(
                this,
                new Alt5(degree, g5, g3),
                new DoubleAlt4(degree, g3.mul(i), g2),
                new Alt4(degree, g3, g2),
                new Dih10(degree,g5i,g2),
                new Dih6(degree, g3.mul(i), g2prime),
                new Z10(degree, g5i),
                new Dihedral(degree, g5, g2), // TODO: alternative Dih5
                new Cubed2(degree, g2, i, g2.conj(g3)),
                // TODO: missing Sym(3) 2.Sym(3) etc.
                new Cyclic(degree, g5) // TODO: alternative Z5
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("Ih", order, degree, Arrays.asList(
                        new ExtendedPerm(g5i, PointGroupElement.ROT_5.minus()),
                        new ExtendedPerm(g3, PointGroupElement.ROT_3)
                )),
                new CombinedGroup("Ih*", order, degree, Arrays.asList(
                        new ExtendedPerm(g5i, PointGroupElement.ROT_5_STAR.minus()),
                        new ExtendedPerm(g3, PointGroupElement.ROT_3)
                ))
        );
    }
}
