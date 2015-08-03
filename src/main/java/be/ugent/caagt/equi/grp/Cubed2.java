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

import java.util.Arrays;

/**
 * Dihedral group of order 8
 */
public class Cubed2 extends AbstractCombinatorialGroup {

    // TODO: replace by DoubleDihedral(2)?

    private Perm g2;

    private Perm i;

    private Perm g2star;

    public Cubed2(int degree, Perm g2, Perm i, Perm g2star) {
        super(8, degree);
        this.g2 = g2;
        this.i = i;
        this.g2star = g2star;
    }

    @Override
    public String toString() {
        return "2.2.2";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this // TODO
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
           new CombinedGroup("D2h", order, degree, Arrays.asList(
                   new ExtendedPerm(g2, PointGroupElement.ROT_G2),
                   new ExtendedPerm(i, PointGroupElement.ONE.minus()),
                   new ExtendedPerm(g2star, PointGroupElement.ROT_G2STAR)
           )) // TODO: others
        );
    }
}
