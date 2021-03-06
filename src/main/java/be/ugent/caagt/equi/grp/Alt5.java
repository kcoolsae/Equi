/* Alt5.java
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
 * Alternating group of order 5.
 */
public class Alt5 extends AbstractCombinatorialGroup {

    private Perm g5;

    private Perm g3;

    /**
     * Alternating group of order 5 generated by 'canonical' elements g5,g3.
     */
    public Alt5(int degree, Perm g5, Perm g3) {
        super(60, degree);
        this.g5 = g5;
        this.g3 = g3;
    }

    @Override
    public String toString() {
        return "Alt(5)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        Perm g3inv = g3.inv();
        Perm g2 = g3inv.mul(g5.pow(3)).mul(g3).mul(g5.pow(2)).mul(g3inv);
        return Arrays.asList(
                this,
                new Alt4(degree, g3, g2),
                new Dih5(degree, g5, g2),
                new Z5(degree, g5)
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("I", order, degree, Arrays.asList(
                        new ExtendedPerm(g5, PointGroupElement.ROT_5),
                        new ExtendedPerm(g3, PointGroupElement.ROT_3)
                )),
                new CombinedGroup("I*", order, degree, Arrays.asList(
                        new ExtendedPerm(g5, PointGroupElement.ROT_5_STAR),
                        new ExtendedPerm(g3, PointGroupElement.ROT_3)
                ))
        );
    }

}
